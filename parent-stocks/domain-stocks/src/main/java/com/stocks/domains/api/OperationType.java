package com.stocks.domains.api;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.infrastructure.core.Recordable;
import com.securities.api.Sequence;

public interface OperationType extends Recordable<UUID> {
	String name() throws IOException;
	Location defaultSourceLocation() throws IOException;
	Location defaultDestinationLocation() throws IOException;
	OperationCategory category() throws IOException;
	Warehouse warehouse() throws IOException;
	Sequence sequence() throws IOException;
	
	Operations unfinishedOperations();
	Operations operationsDone();
	
	void update(String name, UUID defaultSourceLocationId, UUID defaultDestinationLocationId, String categoryId, UUID sequenceId) throws IOException;
	Operation addOperation(String documentSource, UUID sourceLocationId, UUID destinationLocationId, Date movementDate, boolean delayed) throws IOException;
}
