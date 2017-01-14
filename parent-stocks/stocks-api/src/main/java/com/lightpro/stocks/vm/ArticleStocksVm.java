package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.ArticleStocks;

public class ArticleStocksVm {
	private final transient ArticleStocks origin;
	
	public ArticleStocksVm(){
		throw new UnsupportedOperationException("#ArticleStocksVm()");
	}
	
	public ArticleStocksVm(final ArticleStocks origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public List<ArticleStockVm> getStocks() throws IOException {
		return origin.all()
					 .stream()
			 		 .map(m -> new ArticleStockVm(m))
			 		 .collect(Collectors.toList());
	}
	
	@JsonGetter
	public UUID getArticleId() throws IOException {
		return origin.article().id();
	}
	
	@JsonGetter
	public String getArticle() throws IOException {
		return origin.article().name();
	}
	
	@JsonGetter
	public double getTotalCount() throws IOException {
		return origin.totalCount();
	}
}
