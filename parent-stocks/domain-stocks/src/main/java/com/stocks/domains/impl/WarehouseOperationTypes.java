package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.OperationTypes;

public class WarehouseOperationTypes implements OperationTypes {

	private final transient Base base;
	private final transient Object warehouseId;
	private final transient OperationTypeMetadata dm;	
	private final transient DomainsStore ds;
	
	public WarehouseOperationTypes(final Base base, final Object warehouseId){
		this.base = base;
		this.warehouseId = warehouseId;
		this.dm = OperationTypeImpl.dm();
		this.ds = base.domainsStore(dm);
	}
	
	@Override
	public List<OperationType> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public List<OperationType> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<OperationType> find(int page, int pageSize, String filter) throws IOException {
		List<OperationType> values = new ArrayList<OperationType>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		String statement = String.format("SELECT %s FROM %s WHERE %s ILIKE ? AND %s=? ORDER BY %s DESC LIMIT ? OFFSET ?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.warehouseIdKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add(warehouseId);
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new OperationTypeImpl(this.base, domainStore.key())); 
		}		
		
		return values;
	}

	@Override
	public int totalCount(String filter) throws IOException {
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s ILIKE ? AND %s=?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.warehouseIdKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add(warehouseId);
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());	
	}

	@Override
	public OperationType get(Object id) throws IOException {
		boolean exists = ds.exists(id);
		OperationType item = null;
		
		if(exists)
			item = new OperationTypeImpl(this.base, id);
		
		if(exists && item.warehouse().id().equals(id))
			return item;
		else
			throw new NotFoundException("Le type d'op�ration n'a pas �t� trouv� !");			
	}

	@Override
	public void delete(OperationType item) throws IOException {
		
		OperationType origin = get(item.id());		
		ds.delete(origin.id());
	}

	@Override
	public boolean contains(OperationType item) throws IOException {
		try {
			get(item.id());
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public OperationType build(Object id) {
		return new OperationTypeImpl(base, id);
	}
}
