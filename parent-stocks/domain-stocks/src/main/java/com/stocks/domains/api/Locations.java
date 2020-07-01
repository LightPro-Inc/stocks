package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface Locations extends Queryable<Location, UUID> {
	void deleteAll() throws IOException;
	
	Location addVirtual(String name, String shortName, LocationType type) throws IOException;
	
	Locations of(Warehouse warehouse);
	Locations of(LocationType type);
}
