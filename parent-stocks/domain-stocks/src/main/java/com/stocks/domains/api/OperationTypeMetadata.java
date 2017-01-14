package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class OperationTypeMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public OperationTypeMetadata(){
		this.domainName = "stocks.operationtypes";
		this.keyName = "id";
	}
	
	public OperationTypeMetadata(final String domainName, final String keyName){
		this.domainName = domainName;
		this.keyName = keyName;
	}
	
	@Override
	public String domainName() {
		return this.domainName;
	}

	@Override
	public String keyName() {
		return this.keyName;
	}

	public String nameKey(){
		return "name";
	}
	
	public String defaultSourceLocationKey(){
		return "defaultsourcelocationid";
	}

	public String defaultDestinationLocationKey(){
		return "defaultdestinationlocationid";
	}
	
	public String warehouseIdKey(){
		return "warehouseid";
	}
	
	public String operationCategoryIdKey(){
		return "operationcategoryid";
	}
	
	public String sequenceIdKey(){
		return "sequenceid";
	}
}
