package com.lightpro.stocks.vm;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.LocationType;

public class LocationTypeVm {
	
	private final transient LocationType origin;
	
	public LocationTypeVm(){
		throw new UnsupportedOperationException("#LocationTypeVm()");
	}
	
	public LocationTypeVm(final LocationType origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public String getId(){
		return origin.id();
	}
	
	@JsonGetter
	public String getName() throws IOException {
		return origin.name();
	}	
}
