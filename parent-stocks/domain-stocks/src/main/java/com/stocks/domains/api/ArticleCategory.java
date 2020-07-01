package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.securities.api.MesureUnit;

public interface ArticleCategory extends Nonable {
	UUID id();
	String name() throws IOException;
	MesureUnit mesureUnit() throws IOException;
	ArticleFamilies families() throws IOException;
	void update(String name, MesureUnit mesureUnit) throws IOException;
	Stocks module() throws IOException;
}
