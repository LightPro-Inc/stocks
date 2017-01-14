package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class ArticlePlanningMetadata implements DomainMetadata {
	
	private final transient String domainName;
	private final transient String keyName;
	
	public ArticlePlanningMetadata() {
		this.domainName = "stocks.articleplannings";
		this.keyName = "id";
	}
	
	public ArticlePlanningMetadata(final String domainName, final String keyName){
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

	public String articleIdKey(){
		return "articleid";
	}
	
	public String locationIdKey(){
		return "locationid";
	}
	
	public String maximumStockKey(){
		return "maximumstock";
	}
	
	public String safetyStockKey(){
		return "safetystock";
	}
	
	public String minimumStockKey(){
		return "minimumstock";
	}
}
