package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface Article extends Nonable {
	UUID id();
	String name() throws IOException;
	String internalReference() throws IOException;
	double quantity() throws IOException;
	double cost() throws IOException;
	String barCode() throws IOException;
	String emballage() throws IOException;
	String description() throws IOException;
	ArticleFamily family() throws IOException;
	ArticlePlannings plannings() throws IOException;
	ArticleStocks stocks() throws IOException;
	StockMovements movements() throws IOException;
	
	void update(String name, String internalReference, String barCode, double quantity, double cost, String description, String emballage) throws IOException;
	void changeFamily(ArticleFamily newFamily) throws IOException;
}
