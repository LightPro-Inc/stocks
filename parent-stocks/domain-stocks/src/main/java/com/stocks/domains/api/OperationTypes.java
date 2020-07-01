package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;

public interface OperationTypes extends AdvancedQueryable<OperationType, UUID> {
	void deleteAll() throws IOException;
	OperationTypes of(Warehouse wh) throws IOException;
}
