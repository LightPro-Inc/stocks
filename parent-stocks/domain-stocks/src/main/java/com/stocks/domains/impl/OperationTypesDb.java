package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.AdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.securities.api.Sequence;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.WarehouseMetadata;

public class OperationTypesDb extends AdvancedQueryableDb<OperationType, UUID, OperationTypeMetadata> implements OperationTypes {

	private final transient Stocks module;
	private final transient Warehouse warehouse;
	
	public OperationTypesDb(final Base base, final Stocks module, final Warehouse warehouse){
		super(base, "Type d'opération introuvable !");
		this.module = module;
		this.warehouse = warehouse;
	}

	@Override
	public void delete(OperationType item) throws IOException {
		if(contains(item))
		{
			item.unfinishedOperations().deleteAll();
			Sequence sequence = item.sequence();
			ds.delete(item.id());
			
			try {
				module.sequences().delete(sequence);
			} catch (Exception ignore) {
			}			
		}
	}

	@Override
	public void deleteAll() throws IOException {
		for (OperationType type : all()) {
			delete(type);
		}
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		WarehouseMetadata whDm = new WarehouseMetadata();		
		String statement = String.format("%s ot "
				+ "JOIN %s wh ON wh.%s=ot.%s "
				+ "WHERE (ot.%s ILIKE ?) AND wh.%s=?",
				dm.domainName(), 
				whDm.domainName(), whDm.keyName(), dm.warehouseIdKey(),
				dm.nameKey(), whDm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(warehouse.id() != null) {
			statement = String.format("%s AND wh.%s=?", statement, whDm.keyName());
			params.add(warehouse.id());
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY ot.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("ot.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected UUID convertKey(Object id) {
		return UUIDConvert.fromObject(id);
	}

	@Override
	protected OperationType newOne(UUID id) {
		return new OperationTypeDb(base, id, module);
	}

	@Override
	public OperationType none() {
		return new OperationTypeNone();
	}

	@Override
	public OperationTypes of(Warehouse wh) throws IOException {
		return new OperationTypesDb(base, module, wh);
	}
}
