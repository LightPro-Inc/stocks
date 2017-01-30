package com.stocks.domains.api;


import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface StockMovements extends AdvancedQueryable<StockMovement, UUID>, Updatable<StockMovement> {
	void deleteAll() throws IOException;
}
