package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;

public interface Articles extends AdvancedQueryable<Article, UUID> {
	Article add(String name, String internalReference, String barCode, double quantity, double cost, String description, String emballage) throws IOException;
	Articles of(ArticleFamily family);
	Articles withInternalReference(String internalReference);
}
