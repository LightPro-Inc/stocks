package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface Article extends Recordable<UUID, Article> {
	String name() throws IOException;
	String internalReference() throws IOException;
	int quantity() throws IOException;
	double cost() throws IOException;
	String barCode() throws IOException;
	String description() throws IOException;
	ArticleFamily family() throws IOException;
	ArticlePlannings plannings() throws IOException;
	ArticleStocks stocks() throws IOException;
	
	void update(String name, String internalReference, String barCode, int quantity, double cost, String description, UUID familyId) throws IOException;
}
