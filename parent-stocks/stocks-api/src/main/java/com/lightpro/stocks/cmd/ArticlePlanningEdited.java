package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticlePlanningEdited {
	
	private final UUID id;
	private final int maximumStock;
	private final int safetyStock;
	private final int minimumStock;	
	private final UUID locationId;
	
	public ArticlePlanningEdited(){
		throw new UnsupportedOperationException("#ArticlePlanningEdited()");
	}
	
	@JsonCreator
	public ArticlePlanningEdited( @JsonProperty("id") final UUID id,
								  @JsonProperty("maximumStock") final int maximumStock,
						    	  @JsonProperty("safetyStock") final int safetyStock, 
						    	  @JsonProperty("minimumStock") final int minimumStock,
						    	  @JsonProperty("locationId") final UUID locationId){
		
		this.id = id;
		this.maximumStock = maximumStock;
		this.safetyStock = safetyStock;
		this.minimumStock = minimumStock;
		this.locationId = locationId;
	}
	
	public UUID id(){
		return id;
	}
	
	public int maximumStock(){
		return maximumStock;
	}
	
	public int safetyStock(){
		return safetyStock;
	}
	
	public int minimumStock(){
		return minimumStock;
	}
	
	public UUID locationId(){
		return locationId;
	}
}
