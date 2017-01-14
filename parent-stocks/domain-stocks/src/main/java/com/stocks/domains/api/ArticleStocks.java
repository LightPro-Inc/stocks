package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ArticleStocks {
	
	Article article();
	ArticleStock get(UUID id) throws IOException;
	ArticleStock get(Location location) throws IOException;
	List<ArticleStock> all() throws IOException;
	
	double totalCount() throws IOException;
}
