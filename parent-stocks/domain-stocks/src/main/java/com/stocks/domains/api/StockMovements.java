package com.stocks.domains.api;


import java.io.IOException;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface StockMovements extends AdvancedQueryable<StockMovement>, Updatable<StockMovement> {
	void deleteAll() throws IOException;
}
