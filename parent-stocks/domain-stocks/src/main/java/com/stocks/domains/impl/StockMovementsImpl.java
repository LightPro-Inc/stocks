package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.StockMovement;
import com.stocks.domains.api.StockMovementMetadata;
import com.stocks.domains.api.StockMovements;

public class StockMovementsImpl implements StockMovements {

	private transient final Base base;
	private final transient StockMovementMetadata dm;
	private final transient DomainsStore ds;
	
	public StockMovementsImpl(final Base base){
		this.base = base;
		this.dm = StockMovementImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
	}
	
	@Override
	public List<StockMovement> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public List<StockMovement> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<StockMovement> find(int page, int pageSize, String filter) throws IOException {
		List<StockMovement> values = new ArrayList<StockMovement>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		OperationMetadata odm = OperationImpl.dm();
		String statement = String.format("SELECT %s FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s ILIKE ? OR %s ILIKE ?) ORDER BY %s DESC LIMIT ? OFFSET ?", dm.keyName(), dm.domainName(), dm.operationIdKey(), odm.keyName(), odm.domainName(), odm.referenceKey(), odm.documentSourceKey(), hm.dateCreatedKey());
		
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
			values.add(new StockMovementImpl(this.base, domainStore.key())); 
		}		
		
		return values;
	}

	@Override
	public int totalCount(String filter) throws IOException {
		OperationMetadata odm = OperationImpl.dm();
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s ILIKE ? OR %s ILIKE ?)", dm.keyName(), dm.domainName(), dm.operationIdKey(), odm.keyName(), odm.domainName(), odm.referenceKey(), odm.documentSourceKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());
	}

	@Override
	public StockMovement get(UUID id) throws IOException {
		StockMovement op = new StockMovementImpl(this.base, id);
		
		if(!op.isPresent())
			throw new NotFoundException("L'article n'a pas été trouvé !");
		
		return op;
	}

	@Override
	public void delete(StockMovement item) throws IOException {
		ds.delete(item.id());
	}

	@Override
	public boolean contains(StockMovement item) {
		try {
			get(item.id());
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public void deleteAll() throws IOException {
		for (StockMovement sm : all()) {
			delete(sm);			
		}
	}

	@Override
	public StockMovement build(UUID id) {
		return new StockMovementImpl(base, id);
	}

}
