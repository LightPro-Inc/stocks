package com.stocks.domains.api;

import java.io.IOException;

public interface AllArticleFamilies extends ArticleFamilies {
	ArticleFamily add(String name, String description, ArticleCategory category) throws IOException;
}
