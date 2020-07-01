package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StockMovementEdited {
	private final UUID id;
	private final double quantity;
	private final UUID articleId;
	private final boolean deleted;
	
	public StockMovementEdited(){
		throw new UnsupportedOperationException("#StockMovementEdited()");
	}
	
	@JsonCreator
	public StockMovementEdited( @JsonProperty("id") final UUID id,
								@JsonProperty("articleId") final UUID articleId,
				    	 		@JsonProperty("quantity") final double quantity, 
				    	 		@JsonProperty("deleted") final boolean deleted){
		
		this.id = id;
		this.articleId = articleId;
		this.quantity = quantity;
		this.deleted = deleted;
	}
	
	public UUID id(){
		return id;
	}
	
	public UUID articleId(){
		return articleId;
	}
	
	public double quantity(){
		return quantity;
	}
	
	public boolean deleted(){
		return deleted;
	}
}
