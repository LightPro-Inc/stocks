package com.stocks.domains.api;

import java.io.IOException;

public interface AllArticles extends Articles {
	Article add(String name, String internalReference, String barCode, int quantity, double cost, String description, ArticleFamily family) throws IOException;
}
