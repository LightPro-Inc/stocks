package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.Location;

public class LocationVm {
	private final transient Location origin;
	
	public LocationVm(){
		throw new UnsupportedOperationException("#LocationVm()");
	}
	
	public LocationVm(final Location origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public String getName() throws IOException {
		return origin.name();
	}
	
	@JsonGetter
	public String getShortName() throws IOException {
		return origin.shortName();
	}
	
	@JsonGetter
	public UUID getWarehouseId() throws IOException {
		return origin.warehouse().id();
	}
	
	@JsonGetter
	public String getWarehouse() throws IOException {
		return origin.warehouse().id() == null ? "" : origin.warehouse().name();
	}
	
	@JsonGetter
	public int getTypeId() throws IOException {
		return origin.type().id();
	}
	
	@JsonGetter
	public String getType() throws IOException {
		return origin.type().name();
	}
	
	@JsonGetter
	public boolean getActive() throws IOException {
		return origin.active();
	}		
}
