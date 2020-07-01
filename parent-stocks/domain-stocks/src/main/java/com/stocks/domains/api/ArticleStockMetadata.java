package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class ArticleStockMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public ArticleStockMetadata() {
		this.domainName = "stocks.articlestocks";
		this.keyName = "id";
	}
	
	public ArticleStockMetadata(final String domainName, final String keyName){
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

	public String quantityKey(){
		return "quantity";
	}
	
	public static ArticleStockMetadata create(){
		return new ArticleStockMetadata();
	}
}
