package com.stocks.domains.impl;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.securities.api.Contact;
import com.securities.api.Sequence;
import com.securities.impl.SequenceNone;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.OperationCategory;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.Warehouse;

public final class OperationTypeNone extends EntityNone<OperationType, UUID> implements OperationType {

	@Override
	public String name() throws IOException {
		return "Aucun type d'opération";
	}

	@Override
	public Location defaultSourceLocation() throws IOException {
		return new LocationNone();
	}

	@Override
	public Location defaultDestinationLocation() throws IOException {
		return new LocationNone();
	}

	@Override
	public OperationCategory category() throws IOException {
		return OperationCategory.NONE;
	}

	@Override
	public Warehouse warehouse() throws IOException {
		return new WarehouseNone();
	}

	@Override
	public Sequence sequence() throws IOException {
		return new SequenceNone();
	}

	@Override
	public OperationType preparationOpType() throws IOException {
		return new OperationTypeNone();
	}

	@Override
	public OperationType returnOpType() throws IOException {
		return new OperationTypeNone();
	}

	@Override
	public Operations unfinishedOperations() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Operations todoOperations() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Operations operationsDone() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void update(String name, Location defaultSourceLocation, Location defaultDestinationLocation,
			OperationCategory category, Sequence sequence, OperationType preparationOpType, OperationType returnOpType)
			throws IOException {

	}

	@Override
	public Operation addOperation(String documentSource, UUID sourceLocationId, UUID destinationLocationId,
			Date movementDate, boolean delayed, Contact partner) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Operation addOperation(String documentSource, Date movementDate, boolean delayed, Contact partner)
			throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Operation addOperation(UUID id, String documentSource, UUID sourceLocationId, UUID destinationLocationId,
			Date movementDate, boolean delayed, Contact partner) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Operation addOperation(UUID id, String documentSource, Date movementDate, boolean delayed, Contact partner)
			throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}
}
