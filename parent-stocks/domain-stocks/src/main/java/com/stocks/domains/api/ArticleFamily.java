package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface ArticleFamily extends Nonable {
	UUID id();
	String name() throws IOException;
	String description() throws IOException;
	ArticleCategory category() throws IOException;
	Articles articles() throws IOException;
	void update(String name, String description) throws IOException;	
    void changeCategory(ArticleCategory newCategory) throws IOException;
}
