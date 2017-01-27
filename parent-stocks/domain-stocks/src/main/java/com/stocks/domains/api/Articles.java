package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface Articles extends AdvancedQueryable<Article>, Updatable<Article> {
	Article add(String name, String internalReference, String barCode, int quantity, double cost, String description, UUID familyId) throws IOException;
}
