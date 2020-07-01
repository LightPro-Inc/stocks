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
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.Operation.OperationStatut;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.StockMovement;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.WarehouseMetadata;

public class OperationsDb extends AdvancedQueryableDb<Operation, UUID, OperationMetadata> implements Operations {

	private final transient OperationStatut statut;
	private final transient OperationType type;
	private final transient Stocks module;
	
	public OperationsDb(final Base base, final OperationStatut statut, final Stocks module, final OperationType type){
		super(base, "Opération introuvable !");
		this.statut = statut;
		this.type = type;
		this.module = module;
	}

	@Override
	public void delete(Operation item) throws IOException {
		
		if(contains(item)) {			
			if(item.statut() == OperationStatut.EXECUTED)
				throw new IllegalArgumentException("Vous ne pouvez pas supprimer une opération exécutée !");
			
			if(item.statut() == OperationStatut.VALIDE)
			{
				// 1 - exécuter l'opération de retour
				OperationType returnOpType = item.type().returnOpType();
				if(returnOpType.id() != null){
					Operation op = returnOpType.addOperation(item.documentSource(), null, false, item.partner());
					for (StockMovement mvt : item.movements().all()) {
						op.addMovement(mvt.quantity(), mvt.article());
					}
					
					op.validate();
					op.execute();
				}
				
				ds.createDs(item.id()).set(dm.statutIdKey(), OperationStatut.CANCELLED.id());
				return;
			}
			
			if(item.statut() == OperationStatut.BROUILLON){
				item.movements().deleteAll();
				ds.delete(item.id());
				return;
			}			
		}
	}
	
	@Override
	public void deleteAll() throws IOException {
		for (Operation op : all()) {			
			delete(op);
		}
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		OperationTypeMetadata otDm = new OperationTypeMetadata();
		WarehouseMetadata whDm = new WarehouseMetadata();
		String statement = String.format("%s op "
				+ "JOIN %s ot ON ot.%s=op.%s "
				+ "LEFT JOIN %s wh ON wh.%s=ot.%s "
				+ "WHERE (op.%s ILIKE ? OR op.%s ILIKE ?) AND wh.%s=?",
				dm.domainName(), 
				otDm.domainName(), otDm.keyName(), dm.operationTypeIdKey(),
				whDm.domainName(), whDm.keyName(), otDm.warehouseIdKey(),
				dm.referenceKey(), dm.documentSourceKey(), whDm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(type.id() != null) {
			statement = String.format("%s AND ot.%s=?", statement, otDm.keyName());
			params.add(type.id());
		}
		
		if(statut != OperationStatut.NONE){
			statement = String.format("%s AND op.%s=?", statement, dm.statutIdKey());
			params.add(statut.id());
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY op.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("op.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected UUID convertKey(Object id) {
		return UUIDConvert.fromObject(id);
	}

	@Override
	protected Operation newOne(UUID id) {
		return new OperationDb(base, id, module);
	}

	@Override
	public Operation none() {
		return new OperationNone();
	}

	@Override
	public Operations with(OperationStatut statut) throws IOException {
		return new OperationsDb(base, statut, module, type);
	}

	@Override
	public Operations of(OperationType type) throws IOException {
		return new OperationsDb(base, statut, module, type);
	}
}
