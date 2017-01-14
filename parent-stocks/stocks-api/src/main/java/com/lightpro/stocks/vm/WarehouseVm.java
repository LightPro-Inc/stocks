package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.Warehouse;

public class WarehouseVm {
	private final transient Warehouse origin;
	
	public WarehouseVm(){
		throw new UnsupportedOperationException("#WarehouseVm()");
	}
	
	public WarehouseVm(final Warehouse origin) {
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
	public int getNumberOfLocations() throws IOException {
		return origin.locations().all().size();
	}	
	
	@JsonGetter
	public int getNumberOfOperationTypes() throws IOException {
		return origin.operationTypes().all().size();
	}	
}
