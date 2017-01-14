package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Recordable;
import com.securities.api.MesureUnit;

public interface ArticleCategory extends Recordable<UUID> {
	String name() throws IOException;
	MesureUnit mesureUnit() throws IOException;
	List<ArticleFamily> families() throws IOException;
	void update(String name, UUID mesureUnitId) throws IOException;
}
