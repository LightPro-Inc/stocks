package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class StockMovementMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public StockMovementMetadata(){
		this.domainName = "stocks.stockmovements";
		this.keyName = "id";
	}
	
	public StockMovementMetadata(final String domainName, final String keyName){
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

	public String operationIdKey() {
		return "operationid";
	}
	
	public String quantityKey() {
		return "quantity";
	}
	
	public String articleIdKey() {
		return "articleid";
	}
}
