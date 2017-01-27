package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface ArticleCategories extends AdvancedQueryable<ArticleCategory>, Updatable<ArticleCategory> {
	ArticleCategory add(String name, UUID mesureUnitId) throws IOException;
}
