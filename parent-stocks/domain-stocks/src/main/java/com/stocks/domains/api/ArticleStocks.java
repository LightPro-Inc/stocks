package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface ArticleStocks extends Queryable<ArticleStock, UUID> {
	
	Article article() throws IOException;
	ArticleStock get(Location location) throws IOException;
	
	double totalCount() throws IOException;
	ArticleStocks of(Warehouse wh) throws IOException;
}
