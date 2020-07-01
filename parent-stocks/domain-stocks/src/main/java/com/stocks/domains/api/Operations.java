package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.stocks.domains.api.Operation.OperationStatut;

public interface Operations extends AdvancedQueryable<Operation, UUID> {
	void deleteAll() throws IOException;
	Operations with(OperationStatut statut) throws IOException;
	Operations of(OperationType type) throws IOException;
}
