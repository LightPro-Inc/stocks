package com.stocks.domains.impl;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.securities.api.Contact;
import com.securities.impl.ContactNone;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.StockMovement;
import com.stocks.domains.api.StockMovements;

public final class OperationNone extends EntityNone<Operation, UUID> implements Operation {

	@Override
	public String reference() throws IOException {
		return null;
	}

	@Override
	public OperationType type() throws IOException {
		return new OperationTypeNone();
	}

	@Override
	public String documentSource() throws IOException {
		return null;
	}

	@Override
	public Date movementDate() throws IOException {
		return null;
	}

	@Override
	public boolean delayed() throws IOException {
		return false;
	}

	@Override
	public Location sourceLocation() throws IOException {
		return new LocationNone();
	}

	@Override
	public Location destinationLocation() throws IOException {
		return new LocationNone();
	}

	@Override
	public OperationStatut statut() throws IOException {
		return OperationStatut.NONE;
	}

	@Override
	public StockMovements movements() throws IOException {
		throw new UnsupportedOperationException("Opération non supporté !"); 
	}

	@Override
	public Contact partner() throws IOException {
		return new ContactNone();
	}

	@Override
	public void update(String documentSource, Location source, Location destination, Date movement, boolean delayed,
			Contact partner) throws IOException {
		
	}

	@Override
	public StockMovement addMovement(double quantity, Article article) throws IOException {
		throw new UnsupportedOperationException("Opération non supporté !"); 
	}

	@Override
	public void validate() throws IOException {
		
	}

	@Override
	public void execute() throws IOException {

	}
}
