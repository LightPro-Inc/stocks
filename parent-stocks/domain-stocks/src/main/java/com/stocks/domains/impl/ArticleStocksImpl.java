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
import com.stocks.domains.api.Warehouse;

public class ArticleStocksImpl implements ArticleStocks {

	private transient final Base base;
	private final transient ArticleStockMetadata dm;
	private final transient Article article;
	private final transient DomainsStore ds;
	private final transient Warehouse warehouse;
	
	public ArticleStocksImpl(final Base base, final Article article, final Warehouse warehouse){
		this.base = base;
		this.article = article;
		this.dm = ArticleStockImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
		this.warehouse = warehouse;
	}

	@Override
	public ArticleStock get(UUID id) throws IOException {
		ArticleStock stock = new ArticleStockImpl(this.base, article.plannings().get(id));
		
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
		
		return new ArticleStockImpl(this.base, planning);
	}

	@Override
	public List<ArticleStock> all() throws IOException {
		List<ArticleStock> items = new ArrayList<ArticleStock>();
		
		for (Location location : warehouse.locations().internals()) {
			items.add(get(location));			
		}
				
		return items;
	}

	private ArticleStock add(Location location) throws IOException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.quantityKey(), 0);
		
		ArticlePlanning planning = article.plannings().get(location);
		ds.set(planning.id(), params);
		
		return new ArticleStockImpl(this.base, planning);
	}

	@Override
	public double totalCount() throws IOException {
		double count = 0;
		
		for (ArticleStock as : all()) {
			count += as.quantity();
		}
		
		return count;
	}

	@Override
	public Article article() throws IOException {
		return article;
	}
}
