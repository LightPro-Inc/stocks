package com.stocks.domains.api;

import java.io.IOException;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface Warehouses extends AdvancedQueryable<Warehouse>, Updatable<Warehouse> {
	Warehouse add(String name, String shortName) throws IOException;
}
