package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface ArticleCategories extends Queryable<ArticleCategory> {
	ArticleCategory get(UUID id) throws IOException;
	ArticleCategory add(String name, UUID mesureUnitId) throws IOException;
	void delete(ArticleCategory item) throws IOException;
}
