package com.lightpro.stocks.vm;

import com.stocks.domains.api.LocationType;

public final class LocationTypeVm {
	
	public final int id;
	public final String name;	
	
	public LocationTypeVm(){
		throw new UnsupportedOperationException("#LocationTypeVm()");
	}
	
	public LocationTypeVm(final LocationType origin) {
		this.id = origin.id();
		this.name = origin.name();	
    }
}
