package com.stocks.domains.api;

import java.io.IOException;

public interface ArticleFamiliesByCategory extends ArticleFamilies {
	ArticleFamily add(String name, String description) throws IOException;
}
