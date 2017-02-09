package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Recordable;
import com.securities.api.MesureUnit;

public interface ArticleCategory extends Recordable<UUID, ArticleCategory> {
	String name() throws IOException;
	MesureUnit mesureUnit() throws IOException;
	ArticleFamiliesByCategory families() throws IOException;
	void update(String name, UUID mesureUnitId) throws IOException;
	Stocks module() throws IOException;
}
