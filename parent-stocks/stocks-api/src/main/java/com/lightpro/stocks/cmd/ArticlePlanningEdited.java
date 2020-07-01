package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticlePlanningEdited {
	
	private final UUID id;
	private final double maximumStock;
	private final double safetyStock;
	private final double minimumStock;	
	private final UUID locationId;
	
	public ArticlePlanningEdited(){
		throw new UnsupportedOperationException("#ArticlePlanningEdited()");
	}
	
	@JsonCreator
	public ArticlePlanningEdited( @JsonProperty("id") final UUID id,
								  @JsonProperty("maximumStock") final double maximumStock,
						    	  @JsonProperty("safetyStock") final double safetyStock, 
						    	  @JsonProperty("minimumStock") final double minimumStock,
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
	
	public double maximumStock(){
		return maximumStock;
	}
	
	public double safetyStock(){
		return safetyStock;
	}
	
	public double minimumStock(){
		return minimumStock;
	}
	
	public UUID locationId(){
		return locationId;
	}
}
