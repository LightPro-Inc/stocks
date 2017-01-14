package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationEdited {
	
	private final UUID id;
	private final String name;
	private final String shortName;
	private final UUID warehouseId;
	private final String typeId;
	
	public LocationEdited(){
		throw new UnsupportedOperationException("#LocationEdited()");
	}
	
	@JsonCreator
	public LocationEdited(@JsonProperty("id") final UUID id,
				    	  @JsonProperty("name") final String name, 
				    	  @JsonProperty("shortName") final String shortName,
				    	  @JsonProperty("warehouseId") final UUID warehouseId,
				    	  @JsonProperty("typeId") final String typeId){
		
		this.id = id;
		this.name = name;
		this.shortName = shortName;
		this.warehouseId = warehouseId;
		this.typeId = typeId;
	}
	
	public UUID id(){
		return id;
	}
	
	public String shortName(){
		return shortName;
	}
	
	public String name(){
		return name;
	}
	
	public UUID warehouseId(){
		return warehouseId;
	}
	
	public String typeId(){
		return typeId;
	}
}
