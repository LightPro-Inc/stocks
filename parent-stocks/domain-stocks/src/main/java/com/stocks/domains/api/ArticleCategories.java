package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.securities.api.MesureUnit;

public interface ArticleCategories extends AdvancedQueryable<ArticleCategory, UUID> {
	ArticleCategory add(String name, MesureUnit mesureUnit) throws IOException;
}
