package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface ArticlePlanning extends Nonable {
	UUID id();
	Location location() throws IOException;	
	Article article() throws IOException;
	ArticleStock stock() throws IOException;
	double maximumStock() throws IOException;
	double safetyStock() throws IOException;
	double minimumStock() throws IOException;
	
	void update(double maximumStock, double safetyStock, double minimumStock) throws IOException;
}
