package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WarehouseEdited {
	private final UUID id;
	private final String name;
	private final String shortName;
	
	public WarehouseEdited(){
		throw new UnsupportedOperationException("#WarehouseEdited()");
	}
	
	@JsonCreator
	public WarehouseEdited(@JsonProperty("id") final UUID id,
						    	 @JsonProperty("name") final String name, 
						    	 @JsonProperty("shortName") final String shortName){
		
		this.id = id;
		this.name = name;
		this.shortName = shortName;
	}
	
	public UUID id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	public String shortName(){
		return shortName;
	}
}
