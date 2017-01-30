package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface ArticleFamilies extends AdvancedQueryable<ArticleFamily, UUID>, Updatable<ArticleFamily> {
	ArticleFamily add(String name, String description, UUID categoryId) throws IOException;	
}
