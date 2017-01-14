package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface ArticleFamily extends Recordable<UUID> {
	String name() throws IOException;
	String description() throws IOException;
	ArticleCategory category() throws IOException;
	List<Article> articles() throws IOException;
	void update(String name, String description, UUID categoryId) throws IOException;	
}
