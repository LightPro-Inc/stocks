package com.stocks.domains.api;

import com.infrastructure.core.DomainMetadata;

public class LocationMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public LocationMetadata() {
		this.domainName = "stocks.locations";
		this.keyName = "id";
	}
	
	public LocationMetadata(final String domainName, final String keyName){
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
	
	public String activeKey(){
		return "active";
	}
	
	public String warehouseIdKey(){
		return "warehouseid";
	}
	
	public String shortNameKey(){
		return "shortname";
	}
	
	public String typeKey(){
		return "locationtypeid";
	}
}
