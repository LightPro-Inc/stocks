package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.AdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.securities.api.Sequence;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.OperationCategory;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.WarehouseMetadata;
import com.stocks.domains.api.Warehouses;

public final class WarehousesDb extends AdvancedQueryableDb<Warehouse, UUID, WarehouseMetadata> implements Warehouses {

	private final transient Stocks module;
	
	public WarehousesDb(final Base base, final Stocks module){
		super(base, "Entrepôt introuvable !");	
		this.module = module;
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
		
		OperationType defaultPreparationOpType = warehouse.operationTypes().build(null);
		OperationType defaultReturnOpType = warehouse.operationTypes().build(null);
		
		warehouse.addOperationType("Réceptions", fournisseurLocation, mainLocation, OperationCategory.FOURNISSEUR, sequenceReceptions, defaultPreparationOpType, defaultReturnOpType);
		
		// 3 - 2 - création transfert interne
		Sequence sequenceTransfer = module.sequences().add(String.format("Entrepôt %s séquence interne", warehouse.shortName()), warehouse.shortName(), "TRANS", 9, 1, 1);		
		warehouse.addOperationType("Transferts internes", mainLocation, mainLocation, OperationCategory.INTERNAL, sequenceTransfer, defaultPreparationOpType, defaultReturnOpType);
		
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
		
		warehouse.addOperationType("Livraisons", mainLocation, clientLocation, OperationCategory.CLIENT, sequenceLivraisons, defaultPreparationOpType, defaultReturnOpType);
		
		return warehouse;
	}

	@Override
	public void delete(Warehouse item) throws IOException {
		item.operationTypes().deleteAll();
		item.locations().deleteAll();			
		ds.delete(item.id());
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		String statement = String.format("%s wh "
				+ "WHERE wh.%s ILIKE ? AND wh.%s=?",
				dm.domainName(), 
				dm.nameKey(), dm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add(module.id());
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY wh.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("wh.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected UUID convertKey(Object id) {
		return UUIDConvert.fromObject(id);
	}

	@Override
	protected Warehouse newOne(UUID id) {
		return new WarehouseDb(base, id, module);
	}

	@Override
	public Warehouse none() {
		return new WarehouseNone();
	}
}
