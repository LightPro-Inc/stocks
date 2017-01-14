package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationTypeEdited {

	private final UUID id;
	private final String name;
	private final UUID defaultSourceLocationId;
	private final UUID defaultDestinationLocationId;
	private final String categoryId;
	private final UUID sequenceId;
	
	public OperationTypeEdited(){
		throw new UnsupportedOperationException("#OperationTypeEdited()");
	}
	
	@JsonCreator
	public OperationTypeEdited(@JsonProperty("id") final UUID id,
					    	   @JsonProperty("name") final String name, 
					    	   @JsonProperty("defaultSourceLocationId") final UUID defaultSourceLocationId,
					    	   @JsonProperty("defaultDestinationLocationId") final UUID defaultDestinationLocationId,
					    	   @JsonProperty("categoryId") final String categoryId,
					    	   @JsonProperty("sequenceId") final UUID sequenceId){
		
		this.id = id;
		this.name = name;
		this.defaultSourceLocationId = defaultSourceLocationId;
		this.defaultDestinationLocationId = defaultDestinationLocationId;
		this.categoryId = categoryId;
		this.sequenceId = sequenceId;
	}
	
	public UUID id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	public UUID defaultSourceLocationId(){
		return defaultSourceLocationId;
	}
	
	public UUID defaultDestinationLocationId(){
		return defaultDestinationLocationId;
	}
	
	public String categoryId(){
		return categoryId;
	}
	
	public UUID sequenceId(){
		return sequenceId;
	}
}
