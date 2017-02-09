package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface ArticleFamily extends Recordable<UUID, ArticleFamily> {
	String name() throws IOException;
	String description() throws IOException;
	ArticleCategory category() throws IOException;
	ArticlesByFamily articles() throws IOException;
	void update(String name, String description) throws IOException;	
}
