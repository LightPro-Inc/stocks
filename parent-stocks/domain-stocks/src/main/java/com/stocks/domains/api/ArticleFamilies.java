package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;

public interface ArticleFamilies extends AdvancedQueryable<ArticleFamily, UUID> {
	ArticleFamily add(String name, String description) throws IOException;
	ArticleFamilies of(ArticleCategory category);
}
