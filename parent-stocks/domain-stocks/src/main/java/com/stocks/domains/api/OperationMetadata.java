package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class OperationMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public OperationMetadata() {
		this.domainName = "stocks.operations";
		this.keyName = "id";
	}
	
	public OperationMetadata(final String domainName, final String keyName){
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

	public String referenceKey(){
		return "reference";
	}
	
	public String documentSourceKey(){
		return "documentsource";
	}
	
	public String sourceLocationIdKey(){
		return "sourcelocationid";
	}
	
	public String destinationLocationIdKey(){
		return "destinationlocationid";
	}
	
	public String statutIdKey(){
		return "statutid";
	}
	
	public String operationTypeIdKey(){
		return "operationtypeid";
	}
	
	public String movementDateKey(){
		return "movementdate";
	}
	
	public String delayedKey(){
		return "delayed";
	}
}
