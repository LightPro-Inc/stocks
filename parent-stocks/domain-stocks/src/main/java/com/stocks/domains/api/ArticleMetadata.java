package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class ArticleMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public ArticleMetadata() {
		this.domainName = "stocks.articles";
		this.keyName = "id";
	}
	
	public ArticleMetadata(final String domainName, final String keyName){
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
	
	public String internalReferenceKey(){
		return "internalreference";
	}
	
	public String descriptionKey(){
		return "description";
	}
	
	public String costKey(){
		return "cost";
	}
	
	public String familyIdKey(){
		return "familyid";
	}
	
	public String barCodeKey(){
		return "barcode";
	}
	
	public String quantityKey(){
		return "quantity";
	}
	
	public String emballageKey(){
		return "emballage";
	}
	
	public static ArticleMetadata create(){
		return new ArticleMetadata();
	}
}
