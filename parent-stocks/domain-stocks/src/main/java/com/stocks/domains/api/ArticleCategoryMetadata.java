package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class ArticleCategoryMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public ArticleCategoryMetadata() {
		this.domainName = "stocks.articlecategories";
		this.keyName = "id";
	}
	
	public ArticleCategoryMetadata(final String domainName, final String keyName){
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
	
	public String mesureUnitIdKey(){
		return "mesureunitid";
	}		
}
