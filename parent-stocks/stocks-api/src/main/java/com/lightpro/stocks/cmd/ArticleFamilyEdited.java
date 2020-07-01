package com.lightpro.stocks.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleFamilyEdited {
	
	private final UUID id;
	private final String name;
	private final UUID categoryId;
	private final String description;
	
	public ArticleFamilyEdited(){
		throw new UnsupportedOperationException("#ArticleFamilyEdited()");
	}
	
	@JsonCreator
	public ArticleFamilyEdited(@JsonProperty("id") final UUID id,
						    	 @JsonProperty("name") final String name, 
						    	 @JsonProperty("categoryId") final UUID categoryId,
						    	 @JsonProperty("description") final String description){
		
		this.id = id;
		this.name = name;
		this.categoryId = categoryId;
		this.description = description;
	}
	
	public UUID id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	public UUID categoryId(){
		return categoryId;
	}
	
	public String description(){
		return description;
	}
}
