package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface StockMovement extends Nonable {
	UUID id();
	double quantity() throws IOException;
	Article article() throws IOException;
	Operation operation() throws IOException;	
	
	void update(double quantity, Article article) throws IOException;
	void execute() throws IOException;
}
