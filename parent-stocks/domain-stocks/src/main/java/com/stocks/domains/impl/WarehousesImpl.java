package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.securities.api.Sequence;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.OperationCategory;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.WarehouseMetadata;
import com.stocks.domains.api.Warehouses;

public class WarehousesImpl implements Warehouses {

	private transient final Base base;
	private final transient WarehouseMetadata dm;
	private final transient DomainsStore ds;
	private final transient Stocks module;
	
	public WarehousesImpl(final Base base, final Stocks module){
		this.base = base;
		this.dm = WarehouseImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
		this.module = module;
	}
	
	@Override
	public Warehouse get(UUID id) throws IOException {
		Warehouse item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("L'entrepôt n'a pas été trouvé !");
		
		return build(id);
	}

	@Override
	public Warehouse add(String name, String shortName) throws IOException {
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (StringUtils.isBlank(shortName)) {
            throw new IllegalArgumentException("Invalid shortName : it can't be empty!");
        }
		
		// 1 - création de l'entrepôt
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.shortNameKey(), shortName);
		params.put(dm.moduleIdKey(), module.id());		
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		Warehouse warehouse = build(id);
		// 2 - création d'un emplacement principal		
		Location mainLocation = warehouse.addLocation(String.format("%s/Stock/principal", warehouse.shortName()), String.format("%s/SP", warehouse.shortName()));
		
		
		// 3 - création des types d'opérations
		// 3 - 1 - création type réception
		Sequence sequenceReceptions = module.sequences().add(String.format("Entrepôt %s séquence d'entrée", warehouse.shortName()), warehouse.shortName(), "REC", 9, 1, 1);
		Location fournisseurLocation = module.locations().all()
										     .stream().filter(m -> {
												try {
													return m.type() == LocationType.FOURNISSEUR;
												} catch (IOException e) {
													e.printStackTrace();
												}
												return false;
											})
										     .findFirst()
										     .get();
		
		warehouse.addOperationType("Réceptions", fournisseurLocation, mainLocation, OperationCategory.FOURNISSEUR, sequenceReceptions);
		
		// 3 - 2 - création transfert interne
		Sequence sequenceTransfer = module.sequences().add(String.format("Entrepôt %s séquence interne", warehouse.shortName()), warehouse.shortName(), "TRANS", 9, 1, 1);		
		warehouse.addOperationType("Transferts internes", mainLocation, mainLocation, OperationCategory.INTERNAL, sequenceTransfer);
		
		// 3 - 3 - création livraisons
		Sequence sequenceLivraisons = module.sequences().add(String.format("Entrepôt %s séquence de sortie", warehouse.shortName()), warehouse.shortName(), "LIV", 9, 1, 1);
		Location clientLocation = module.locations().all()
										     .stream().filter(m -> {
												try {
													return m.type() == LocationType.CLIENT;
												} catch (IOException e) {
													e.printStackTrace();
												}
												return false;
											})
										     .findFirst()
										     .get();
		
		warehouse.addOperationType("Livraisons", mainLocation, clientLocation, OperationCategory.CLIENT, sequenceLivraisons);
		
		return warehouse;
	}

	@Override
	public void delete(Warehouse item) throws IOException {
		ds.delete(item.id());
	}

	@Override
	public List<Warehouse> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public List<Warehouse> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<Warehouse> find(int page, int pageSize, String filter) throws IOException {
		List<Warehouse> values = new ArrayList<Warehouse>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		String statement = String.format("SELECT %s FROM %s WHERE (%s ILIKE ? OR %s ILIKE ?) AND %s=? ORDER BY %s ASC LIMIT ? OFFSET ?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.shortNameKey(), dm.moduleIdKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(build(UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;	
	}

	@Override
	public int totalCount(String filter) throws IOException {
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE (%s ILIKE ? OR %s ILIKE ?) AND %s=?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.shortNameKey(), dm.moduleIdKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());	
	}

	@Override
	public boolean contains(Warehouse item) {
		try {
			return item.isPresent() && item.moduleStocks().isEqual(module);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Warehouse build(UUID id) {
		return new WarehouseImpl(base, id);
	}
}
