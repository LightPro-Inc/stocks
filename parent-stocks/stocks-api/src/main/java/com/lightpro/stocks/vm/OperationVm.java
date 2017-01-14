package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.Operation;

public class OperationVm {
	private final transient Operation origin;
	
	public OperationVm(){
		throw new UnsupportedOperationException("#OperationVm()");
	}
	
	public OperationVm(final Operation origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public String getReference() throws IOException {
		return origin.reference();
	}
	
	@JsonGetter
	public Date getMovementDate() throws IOException {
		return origin.movementDate();
	}
	
	@JsonGetter
	public String getDocumentSource() throws IOException {
		return origin.documentSource();
	}	
	
	@JsonGetter
	public String getSourceLocation() throws IOException {
		return origin.sourceLocation().name();
	}
	
	@JsonGetter
	public UUID getSourceLocationId() throws IOException {
		return origin.sourceLocation().id();
	}
	
	@JsonGetter
	public String getDestinationLocation() throws IOException {
		return origin.destinationLocation().name();
	}
	
	@JsonGetter
	public UUID getDestinationLocationId() throws IOException {
		return origin.destinationLocation().id();
	}
	
	@JsonGetter
	public String getType() throws IOException {
		return origin.type().name();
	}
	
	@JsonGetter
	public UUID getTypeId() throws IOException {
		return origin.type().id();
	}
	
	@JsonGetter
	public List<StockMovementVm> getMovements() throws IOException {
		return origin.movements().all()
					 .stream()
			 		 .map(m -> new StockMovementVm(m))
			 		 .collect(Collectors.toList());
	}
	
	@JsonGetter
	public boolean getDelayed() throws IOException {
		return origin.delayed();
	}
}
