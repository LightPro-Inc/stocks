package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface ArticleFamilies extends Queryable<ArticleFamily> {
	ArticleFamily get(UUID id) throws IOException;
	ArticleFamily add(String name, String description, UUID categoryId) throws IOException;	
	void delete(ArticleFamily item) throws IOException;	
}
