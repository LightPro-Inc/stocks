package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleCategoryEdited {
	
	private final UUID id;
	private final String name;
	private final UUID mesureUnitId;
	
	public ArticleCategoryEdited(){
		throw new UnsupportedOperationException("#ArticleCategoryEdited()");
	}
	
	@JsonCreator
	public ArticleCategoryEdited(@JsonProperty("id") final UUID id,
						    @JsonProperty("name") final String name, 
						    @JsonProperty("mesureUnitId") final UUID mesureUnitId){
		
		this.id = id;
		this.name = name;
		this.mesureUnitId = mesureUnitId;
	}
	
	public UUID id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	public UUID mesureUnitId(){
		return mesureUnitId;
	}
}
