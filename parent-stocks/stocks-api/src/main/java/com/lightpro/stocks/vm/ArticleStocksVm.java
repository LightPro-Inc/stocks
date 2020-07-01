package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.stocks.domains.api.ArticleStocks;

public final class ArticleStocksVm {
	
	public final List<ArticleStockVm> stocks;
	public final UUID articleId;
	public final String article;
	public final double totalCount;
	public final String stockEmballage;
	
	public ArticleStocksVm(){
		throw new UnsupportedOperationException("#ArticleStocksVm()");
	}
	
	public ArticleStocksVm(final ArticleStocks origin) {
		try {
			this.stocks = origin.all()
					 .stream()
			 		 .map(m -> new ArticleStockVm(m))
			 		 .collect(Collectors.toList());
			
			this.articleId = origin.article().id();
			this.article = origin.article().name();
	        this.totalCount = origin.totalCount();
	        this.stockEmballage = origin.article().emballage();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }
}
