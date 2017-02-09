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
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.Operation.OperationStatut;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.WarehouseMetadata;

public class OperationsImpl implements Operations {

	private transient final Base base;
	private final transient OperationMetadata dm;
	private final transient OperationStatut statut;
	private final transient DomainsStore ds;
	private final transient Stocks module;
	
	public OperationsImpl(final Base base, final OperationStatut statut, Stocks module){
		this.base = base;
		this.statut = statut;
		this.dm = OperationImpl.dm();
		this.ds = this.base.domainsStore(this.dm);
		this.module = module;
	}
	
	@Override
	public List<Operation> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public List<Operation> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<Operation> find(int page, int pageSize, String filter) throws IOException {
		List<Operation> values = new ArrayList<Operation>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		String statement;
		
		List<Object> params = new ArrayList<Object>();
		
		OperationTypeMetadata dmOt = OperationTypeImpl.dm();
		WarehouseMetadata dmWh = WarehouseImpl.dm();
		
		if(statut == OperationStatut.NONE)
		{					
			statement = String.format("SELECT op.%s FROM %s op "
					+ "JOIN %s ot ON ot.%s=op.%s "
					+ "left JOIN %s wh ON wh.%s=ot.%s "
					+ "WHERE (op.%s ILIKE ? OR op.%s ILIKE ?) AND wh.%s=? "
					+ "ORDER BY op.%s DESC LIMIT ? OFFSET ?", 
					dm.keyName(), dm.domainName(),
					dmOt.domainName(), dmOt.keyName(), dm.operationTypeIdKey(),
					dmWh.domainName(), dmWh.keyName(), dmOt.warehouseIdKey(),
					dm.referenceKey(), dm.documentSourceKey(), dmWh.moduleIdKey(),
					hm.dateCreatedKey());
		}
		else {
			statement = String.format("SELECT op.%s FROM %s op "
					+ "JOIN %s ot ON ot.%s=op.%s "
					+ "left JOIN %s wh ON wh.%s=ot.%s "
					+ "WHERE op.%s=? AND (op.%s ILIKE ? OR op.%s ILIKE ?) AND wh.%s=? "
					+ "ORDER BY op.%s DESC LIMIT ? OFFSET ?", 
					dm.keyName(), dm.domainName(),
					dmOt.domainName(), dmOt.keyName(), dm.operationTypeIdKey(),
					dmWh.domainName(), dmWh.keyName(), dmOt.warehouseIdKey(),
					dm.statutIdKey(), dm.referenceKey(), dm.documentSourceKey(), dmWh.moduleIdKey(),
					hm.dateCreatedKey());
			
			params.add(statut.id());
		}		
		
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
			values.add(new OperationImpl(this.base, UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;
	}

	@Override
	public int totalCount(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		
		String statement;
		
		OperationTypeMetadata dmOt = OperationTypeImpl.dm();
		WarehouseMetadata dmWh = WarehouseImpl.dm();
		
		if(statut == OperationStatut.NONE)
		{					
			statement = String.format("SELECT COUNT(op.%s) FROM %s op "
					+ "JOIN %s ot ON ot.%s=op.%s "
					+ "left JOIN %s wh ON wh.%s=ot.%s "
					+ "WHERE (op.%s ILIKE ? OR op.%s ILIKE ?) AND wh.%s=? ",
					dm.keyName(), dm.domainName(),
					dmOt.domainName(), dmOt.keyName(), dm.operationTypeIdKey(),
					dmWh.domainName(), dmWh.keyName(), dmOt.warehouseIdKey(),
					dm.referenceKey(), dm.documentSourceKey(), dmWh.moduleIdKey());
		}
		else {
			statement = String.format("SELECT COUNT(op.%s) FROM %s op "
					+ "JOIN %s ot ON ot.%s=op.%s "
					+ "left JOIN %s wh ON wh.%s=ot.%s "
					+ "WHERE op.%s=? AND (op.%s ILIKE ? OR op.%s ILIKE ?) AND wh.%s=? ",
					dm.keyName(), dm.domainName(),
					dmOt.domainName(), dmOt.keyName(), dm.operationTypeIdKey(),
					dmWh.domainName(), dmWh.keyName(), dmOt.warehouseIdKey(),
					dm.statutIdKey(), dm.referenceKey(), dm.documentSourceKey(), dmWh.moduleIdKey());
			
			params.add(statut.id());
		}		
		
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());		
	}

	@Override
	public Operation get(UUID id) throws IOException {
		Operation op = new OperationImpl(this.base, id);
		
		if(!contains(op))
			throw new NotFoundException("L'opération n'a pas été trouvée !");
		
		return op;
	}

	@Override
	public void delete(Operation item) throws IOException {
		if(contains(item)) {
			item.movements().deleteAll();
			ds.delete(item.id());
		}
	}

	@Override
	public boolean contains(Operation item) {
		try {
			return item.isPresent()
					&& (statut == OperationStatut.NONE || item.statut() == statut) 
					&& item.type().warehouse().moduleStocks().isEqual(module);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Operation build(UUID id) {
		return new OperationImpl(this.base, id);
	}
	
	@Override
	public void deleteAll() throws IOException {
		for (Operation op : all()) {			
			delete(op);
		}
	}
}
