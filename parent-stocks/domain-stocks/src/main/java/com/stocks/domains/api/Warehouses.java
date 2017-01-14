package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface Warehouses extends Queryable<Warehouse> {
	Warehouse get(UUID id) throws IOException;
	Warehouse add(String name, String shortName) throws IOException;
	void delete(Warehouse wh) throws IOException;
}
