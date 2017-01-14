package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface OperationTypes extends Queryable<OperationType> {
	OperationType get(UUID id) throws IOException;	
	void delete(OperationType item) throws IOException;
}
