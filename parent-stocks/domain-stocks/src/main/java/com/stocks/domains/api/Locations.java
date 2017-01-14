package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface Locations {
	List<Location> all() throws IOException;
	List<Location> internals() throws IOException;
	Location get(UUID id) throws IOException;
	Location getOrDefault(UUID id) throws IOException;
	void delete(Location location) throws IOException;
}
