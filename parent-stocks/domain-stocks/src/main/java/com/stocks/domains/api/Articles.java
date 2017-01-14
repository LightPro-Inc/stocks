package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface Articles extends Queryable<Article> {
	Article get(UUID id) throws IOException;
	Article add(String name, String internalReference, String barCode, int quantity, double cost, String description, UUID familyId) throws IOException;
	void delete(Article article) throws IOException;
}
