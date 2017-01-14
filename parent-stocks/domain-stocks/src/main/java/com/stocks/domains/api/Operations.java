package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface Operations extends Queryable<Operation> {
	Operation get(UUID id) throws IOException;	
	void delete(Operation item) throws IOException;	
}
