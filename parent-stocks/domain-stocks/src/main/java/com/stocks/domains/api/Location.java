package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface Location extends Nonable {
	UUID id();
	LocationType type() throws IOException;
	String name() throws IOException;
	String shortName() throws IOException;
	boolean active() throws IOException;
	boolean isInternal() throws IOException;
	Warehouse warehouse() throws IOException;
	Stocks module() throws IOException;
	
	void activate(boolean activate) throws IOException;
	void update(String name, String shortName) throws IOException;
}
