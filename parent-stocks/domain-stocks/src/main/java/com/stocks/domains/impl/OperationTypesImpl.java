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

public class OperationTypesImpl implements OperationTypes {

	private final transient Base base;
	private final transient OperationTypeMetadata dm;
	private final transient DomainsStore ds;
	
	public OperationTypesImpl(final Base base){
		this.base = base;
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
		String statement = String.format("SELECT %s FROM %s WHERE %s ILIKE ? ORDER BY %s DESC LIMIT ? OFFSET ?", dm.keyName(), dm.domainName(), dm.nameKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
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
			values.add(new OperationTypeImpl(this.base, domainStore.key())); 
		}		
		
		return values;
	}

	@Override
	public int totalCount(String filter) throws IOException {
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s ILIKE ?", dm.keyName(), dm.domainName(), dm.nameKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());	
	}

	@Override
	public OperationType get(UUID id) throws IOException {
		if(!ds.exists(id))
			throw new NotFoundException("Le type d'opération n'a pas été trouvé !");
		
		return new OperationTypeImpl(this.base, id);
	}

	@Override
	public void delete(OperationType item) throws IOException {
		ds.delete(item.id());
	}

	@Override
	public boolean exists(Object id) throws IOException {
		try {
			get(UUIDConvert.fromObject(id));
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}

}
