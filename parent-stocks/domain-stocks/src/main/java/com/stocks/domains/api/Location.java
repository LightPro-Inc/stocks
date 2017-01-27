package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface Location extends Recordable<UUID, Location> {
	LocationType type() throws IOException;
	String name() throws IOException;
	String shortName() throws IOException;
	boolean active() throws IOException;
	boolean isInternal() throws IOException;
	Warehouse warehouse() throws IOException;
	
	void activate(boolean activate) throws IOException;
	void update(String name, String shortName) throws IOException;
}
