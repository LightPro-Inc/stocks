package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface StockMovement extends Recordable<UUID> {
	int quantity() throws IOException;
	Article article() throws IOException;
	Operation operation() throws IOException;	
	
	void update(int quantity, Article article) throws IOException;
	void execute() throws IOException;
}
