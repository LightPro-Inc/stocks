package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleEdited {
	
	private final UUID id;
	private final String name;
	private final String internalReference;
	private final int quantity;
	private final double cost;
	private final UUID familyId;
	private final String description;
	private final String barCode;	
	
	public ArticleEdited(){
		throw new UnsupportedOperationException("#ArticleEdited()");
	}
	
	@JsonCreator
	public ArticleEdited(@JsonProperty("id") final UUID id,
				    	 @JsonProperty("name") final String name,
				    	 @JsonProperty("internalReference") final String internalReference,
				    	 @JsonProperty("quantity") final int quantity,
				    	 @JsonProperty("cost") final double cost,
				    	 @JsonProperty("familyId") final UUID familyId,
				    	 @JsonProperty("description") final String description,
				    	 @JsonProperty("barCode") final String barCode){
		
		this.id = id;
		this.name = name;
		this.internalReference = internalReference;
		this.quantity = quantity;
		this.cost = cost;
		this.familyId = familyId;
		this.description = description;
		this.barCode = barCode;
	}
	
	public UUID id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	public String internalReference(){
		return internalReference;
	}
	
	public int quantity(){
		return quantity;
	}
	
	public double cost(){
		return cost;
	}
	
	public UUID familyId(){
		return familyId;
	}
	
	public String description(){
		return description;
	}
	
	public String barCode(){
		return barCode;
	}
}
