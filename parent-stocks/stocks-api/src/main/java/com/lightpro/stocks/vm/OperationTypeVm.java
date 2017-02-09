package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.OperationType;

public class OperationTypeVm {
	
	private final transient OperationType origin;
	
	public OperationTypeVm(){
		throw new UnsupportedOperationException("#OperationTypeVm()");
	}
	
	public OperationTypeVm(final OperationType origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public String getName() throws IOException {
		return origin.name();
	}
	
	@JsonGetter
	public UUID getWarehouseId() throws IOException {
		return origin.warehouse().id();
	}
	
	@JsonGetter
	public String getWarehouse() throws IOException {
		return origin.warehouse().name();
	}
	
	@JsonGetter
	public UUID getDefaultSourceLocationId() throws IOException {
		return origin.defaultSourceLocation().id();
	}
	
	@JsonGetter
	public String getDefaultSourceLocation() throws IOException {
		return origin.defaultSourceLocation().name();
	}
	
	@JsonGetter
	public UUID getDefaultDestinationLocationId() throws IOException {
		return origin.defaultDestinationLocation().id();
	}
	
	@JsonGetter
	public String getDefaultDestinationLocation() throws IOException {
		return origin.defaultDestinationLocation().name();
	}	
	
	@JsonGetter
	public int getCategoryId() throws IOException {
		return origin.category().id();
	}	
	
	@JsonGetter
	public String getCategory() throws IOException {
		return origin.category().toString();
	}	
	
	@JsonGetter
	public UUID getSequenceId() throws IOException {
		return origin.sequence().id();
	}
	
	@JsonGetter
	public String getSequence() throws IOException {
		return origin.sequence().name();
	}	
	
	@JsonGetter
	public int getNumberOfUnfinishedOperations() throws IOException {
		return origin.unfinishedOperations().all().size();
	}
}
