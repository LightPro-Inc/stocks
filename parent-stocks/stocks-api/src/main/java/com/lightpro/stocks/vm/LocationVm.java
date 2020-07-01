package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.stocks.domains.api.Location;

public final class LocationVm {

	public final UUID id;
	public final String name;
	public final String shortName;
	public final UUID warehouseId;
	public final String warehouse;
	public final int typeId;
	public final String type;
	public final boolean active;	
	
	public LocationVm(){
		throw new UnsupportedOperationException("#LocationVm()");
	}
	
	public LocationVm(final Location origin) {
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.shortName = origin.shortName();
	        this.warehouseId = origin.warehouse().id();
	        this.warehouse = origin.warehouse().name();
	        this.typeId = origin.type().id();
	        this.type = origin.type().toString();
			this.active = origin.active();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
