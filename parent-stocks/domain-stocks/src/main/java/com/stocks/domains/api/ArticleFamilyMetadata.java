package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class ArticleFamilyMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public ArticleFamilyMetadata() {
		this.domainName = "stocks.articlefamilies";
		this.keyName = "id";
	}
	
	public ArticleFamilyMetadata(final String domainName, final String keyName){
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
	
	public String descriptionKey(){
		return "description";
	}
	
	public String categoryIdKey(){
		return "categoryid";
	}	
}
