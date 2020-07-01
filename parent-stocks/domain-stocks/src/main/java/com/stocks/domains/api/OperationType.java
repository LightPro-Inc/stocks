package com.stocks.domains.api;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.securities.api.Contact;
import com.securities.api.Sequence;

public interface OperationType extends Nonable {
	
	UUID id();
	String name() throws IOException;
	Location defaultSourceLocation() throws IOException;
	Location defaultDestinationLocation() throws IOException;
	OperationCategory category() throws IOException;
	Warehouse warehouse() throws IOException;
	Sequence sequence() throws IOException;
	OperationType preparationOpType() throws IOException;
	OperationType returnOpType() throws IOException;
	
	Operations unfinishedOperations() throws IOException;
	Operations todoOperations() throws IOException;
	Operations operationsDone() throws IOException;
	
	void update(String name, Location defaultSourceLocation, Location defaultDestinationLocation, OperationCategory category, Sequence sequence, OperationType preparationOpType, OperationType returnOpType) throws IOException;
	
	Operation addOperation(UUID id, String documentSource, UUID sourceLocationId, UUID destinationLocationId, Date movementDate, boolean delayed, Contact partner) throws IOException;
	Operation addOperation(String documentSource, UUID sourceLocationId, UUID destinationLocationId, Date movementDate, boolean delayed, Contact partner) throws IOException;
	Operation addOperation(String documentSource, Date movementDate, boolean delayed, Contact partner) throws IOException;
	Operation addOperation(UUID id, String documentSource, Date movementDate, boolean delayed, Contact partner) throws IOException;
}
