package com.stocks.domains.api;


import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.stocks.domains.api.Operation.OperationStatut;

public interface StockMovements extends AdvancedQueryable<StockMovement, UUID> {
	void deleteAll() throws IOException;
	StockMovements of(Operation operation) throws IOException;
	StockMovements withStatus(OperationStatut operationStatut) throws IOException;
	StockMovements of(OperationType type) throws IOException;
	StockMovements of(Warehouse wh) throws IOException;
	StockMovements of(Article article) throws IOException;
}
