package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.WarehouseMetadata;
import com.stocks.domains.api.Warehouses;

public class WarehousesImpl implements Warehouses {

	private transient final Base base;
	private final transient WarehouseMetadata dm;
	private final transient DomainsStore ds;
	
	public WarehousesImpl(final Base base){
		this.base = base;
		this.dm = WarehouseImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
	}
	
	@Override
	public Warehouse get(Object id) throws IOException {
		if(!ds.exists(id))
			throw new NotFoundException("L'entrepôt n'a pas été trouvé !");
		
		return new WarehouseImpl(this.base, id);
	}

	@Override
	public Warehouse add(String name, String shortName) throws IOException {
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("Invalid shortName : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.shortNameKey(), shortName);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new WarehouseImpl(this.base, id);
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
		String statement = String.format("SELECT %s FROM %s WHERE %s ILIKE ? OR %s ILIKE ? ORDER BY %s DESC LIMIT ? OFFSET ?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.shortNameKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new WarehouseImpl(this.base, domainStore.key())); 
		}		
		
		return values;	
	}

	@Override
	public int totalCount(String filter) throws IOException {
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s ILIKE ? OR %s ILIKE ?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.shortNameKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());	
	}

	@Override
	public boolean contains(Warehouse item) throws IOException {
		try {
			get(item.id());
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public Warehouse build(Object id) {
		return new WarehouseImpl(base, id);
	}
}
