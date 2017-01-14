package com.stocks.domains.api;


import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface StockMovements extends Queryable<StockMovement> {
	StockMovement get(UUID id) throws IOException;	
	void delete(StockMovement item) throws IOException;	
	void deleteAll() throws IOException;
}
