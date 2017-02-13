package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Warehouse;

public class WarehouseOperationTypes implements OperationTypes {

	private final transient Base base;
	private final transient OperationTypeMetadata dm;
	private final transient DomainsStore ds;
	private final transient Warehouse warehouse;
	
	public WarehouseOperationTypes(final Base base, final Warehouse warehouse){
		this.base = base;
		this.dm = OperationTypeImpl.dm();
		this.ds = base.domainsStore(dm);
		this.warehouse = warehouse;
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
		params.add(warehouse.id());
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new OperationTypeImpl(this.base, UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;
	}

	@Override
	public int totalCount(String filter) throws IOException {
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s ILIKE ? AND %s=?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.warehouseIdKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add(warehouse.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());	
	}

	@Override
	public OperationType get(UUID id) throws IOException {
		
		OperationType item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("Le type d'opération n'a pas été trouvé !");
		
		return item;
	}

	@Override
	public void delete(OperationType item) throws IOException {
		if(contains(item))
		{
			item.unfinishedOperations().deleteAll();
			ds.delete(item.id());
		}
	}

	@Override
	public boolean contains(OperationType item) {
		try {
			return item.isPresent() && item.warehouse().isEqual(warehouse);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public OperationType build(UUID id) {
		return new OperationTypeImpl(base, id);
	}

	@Override
	public void deleteAll() throws IOException {
		for (OperationType type : all()) {
			delete(type);
		}
	}
}
