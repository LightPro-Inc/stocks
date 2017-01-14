package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.ArticleStockMetadata;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.Location;

public class ArticleStocksImpl implements ArticleStocks {

	private transient final Base base;
	private final transient ArticleStockMetadata dm;
	private final transient Article article;
	private final transient DomainsStore ds;
	
	public ArticleStocksImpl(final Base base, final Object articleId){
		this.base = base;
		this.article = new ArticleImpl(this.base, articleId);
		this.dm = ArticleStockImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
	}

	@Override
	public ArticleStock get(UUID id) throws IOException {
		ArticleStock stock = new ArticleStockImpl(this.base, id);
		
		if(!stock.isPresent())
		{
			Location location = article.plannings().get(id).location();
			stock = get(location);
		}
		
		return stock;
	}

	@Override
	public ArticleStock get(Location location) throws IOException {
		ArticlePlanning planning = article.plannings().get(location);
		
		if(!ds.exists(planning.id()))
			add(location);
		
		return new ArticleStockImpl(this.base, planning.id());
	}

	@Override
	public List<ArticleStock> all() throws IOException {
		List<ArticleStock> items = new ArrayList<ArticleStock>();
		
		List<Location> locations = new LocationsImpl(this.base).internals();
		for (Location location : locations) {
			items.add(get(location));			
		}
		
		return items;
	}

	private ArticleStock add(Location location) throws IOException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.quantityKey(), 0);
		
		ArticlePlanning planning = new ArticleImpl(this.base, article.id()).plannings().get(location);
		ds.set(planning.id(), params);
		
		return new ArticleStockImpl(this.base, planning.id());
	}

	@Override
	public Article article() {
		return article;
	}

	@Override
	public double totalCount() throws IOException {
		double count = 0;
		
		for (ArticleStock as : all()) {
			count += as.quantity();
		}
		
		return count;
	}
}
