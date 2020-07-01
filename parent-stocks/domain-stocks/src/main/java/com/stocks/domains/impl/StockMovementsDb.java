package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.Operation.OperationStatut;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.StockMovement;
import com.stocks.domains.api.StockMovementMetadata;
import com.stocks.domains.api.StockMovements;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.WarehouseMetadata;

public final class StockMovementsDb extends GuidKeyAdvancedQueryableDb<StockMovement, StockMovementMetadata> implements StockMovements {

	private final transient Stocks module;
	private final transient Operation operation;
	private final transient OperationType operationType;
	private final transient Warehouse warehouse;
	private final transient Article article;
	private final transient OperationStatut operationStatut;
	
	public StockMovementsDb(final Base base, final Stocks module, final Operation operation, final OperationType operationType, final Warehouse warehouse, final Article article, final OperationStatut operationStatut){
		super(base, "Mouvement de stock introuvable !");	
		this.module = module;
		this.operation = operation;
		this.operationType = operationType;
		this.warehouse = warehouse;
		this.article = article;
		this.operationStatut = operationStatut;
	}

	@Override
	public void deleteAll() throws IOException {
		for (StockMovement sm : all()) {
			delete(sm);			
		}
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		OperationMetadata opdm = new OperationMetadata();
		OperationTypeMetadata otdm = new OperationTypeMetadata();
		WarehouseMetadata whdm = new WarehouseMetadata();
		
		String statement = String.format("%s mvt "
				+ "JOIN %s op ON op.%s=mvt.%s "
				+ "LEFT JOIN %s ot ON ot.%s=op.%s "
				+ "LEFT JOIN %s wh ON wh.%s=ot.%s "
				+ "WHERE (op.%s ILIKE ? OR op.%s ILIKE ?) AND wh.%s=?",
				dm.domainName(), 
				opdm.domainName(), opdm.keyName(), dm.operationIdKey(),
				otdm.domainName(), otdm.keyName(), opdm.operationTypeIdKey(),
				whdm.domainName(), whdm.keyName(), otdm.warehouseIdKey(),
				opdm.referenceKey(), opdm.documentSourceKey(), whdm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(!operation.isNone()){
			statement = String.format("%s AND op.%s=?", statement, opdm.keyName());
			params.add(operation.id());
		}
		
		if(!operationType.isNone()){
			statement = String.format("%s AND ot.%s=?", statement, otdm.keyName());
			params.add(operationType.id());
		}
		
		if(operationStatut != OperationStatut.NONE){
			statement = String.format("%s AND op.%s=?", statement, opdm.statutIdKey());
			params.add(operationStatut.id());
		}
		
		if(!warehouse.isNone()){
			statement = String.format("%s AND wh.%s=?", statement, whdm.keyName());
			params.add(warehouse.id());
		}
		
		if(!article.isNone()){
			statement = String.format("%s AND mvt.%s=?", statement, dm.articleIdKey());
			params.add(article.id());
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause;
		
		if(operation.isNone())
			orderClause = String.format("ORDER BY mvt.%s DESC", horodateDm.dateCreatedKey());
		else
			orderClause = String.format("ORDER BY mvt.%s ASC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("mvt.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected StockMovement newOne(UUID id) {
		return new StockMovementDb(base, id, module);
	}

	@Override
	public StockMovement none() {
		return new StockMovementNone();
	}

	@Override
	public StockMovements of(Operation operation) throws IOException {
		return new StockMovementsDb(base, module, operation, operationType, warehouse, article, operationStatut);
	}

	@Override
	public StockMovements of(OperationType type) throws IOException {
		return new StockMovementsDb(base, module, operation, operationType, warehouse, article, operationStatut);
	}

	@Override
	public StockMovements of(Warehouse wh) throws IOException {
		return new StockMovementsDb(base, module, operation, operationType, warehouse, article, operationStatut);
	}

	@Override
	public StockMovements of(Article article) throws IOException {
		return new StockMovementsDb(base, module, operation, operationType, warehouse, article, operationStatut);
	}

	@Override
	public StockMovements withStatus(OperationStatut operationStatut) throws IOException {
		return new StockMovementsDb(base, module, operation, operationType, warehouse, article, operationStatut);
	}
}
