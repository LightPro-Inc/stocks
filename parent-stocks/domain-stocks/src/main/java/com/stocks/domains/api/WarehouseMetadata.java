package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class WarehouseMetadata implements DomainMetadata {
	
	private final transient String domainName;
	private final transient String keyName;
	
	public WarehouseMetadata(){
		this.domainName = "stocks.warehouses";
		this.keyName = "id";
	}
	
	public WarehouseMetadata(final String domainName, final String keyName){
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
	
	public String shortNameKey(){
		return "shortname";
	}	
	
	public String moduleIdKey(){
		return "moduleid";
	}	
}
