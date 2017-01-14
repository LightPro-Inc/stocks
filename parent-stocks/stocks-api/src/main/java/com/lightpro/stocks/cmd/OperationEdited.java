package com.lightpro.stocks.cmd;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationEdited {
	private final String documentSource;
	private final UUID sourceLocationId;
	private final UUID destinationLocationId;
	private final Date movementDate;
	private final boolean delayed;
	private final List<StockMovementEdited> movements;
	
	public OperationEdited(){
		throw new UnsupportedOperationException("#OperationEdited()");
	}
	
	@JsonCreator
	public OperationEdited(@JsonProperty("documentSource") final String documentSource,
						   @JsonProperty("sourceLocationId") final UUID sourceLocationId,
				    	   @JsonProperty("destinationLocationId") final UUID destinationLocationId,
				    	   @JsonProperty("movementDate") final Date movementDate,
				    	   @JsonProperty("delayed") final boolean delayed,
				    	   @JsonProperty("movements") final List<StockMovementEdited> movements){
		
		this.documentSource = documentSource;
		this.sourceLocationId = sourceLocationId;
		this.destinationLocationId = destinationLocationId;
		this.movements = movements;
		this.delayed = delayed;
		this.movementDate = movementDate;
	}
	
	public String documentSource(){
		return documentSource;
	}
	
	public UUID sourceLocationId(){
		return sourceLocationId;
	}
	
	public UUID destinationLocationId(){
		return destinationLocationId;
	}
	
	public Date movementDate(){
		return movementDate;
	}
	
	public boolean delayed(){
		return delayed;
	}
	
	public List<StockMovementEdited> movements(){
		return movements;
	}
}
