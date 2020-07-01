package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.GuidKeyQueryableDb;
import com.infrastructure.datasource.Base;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.ArticleStockMetadata;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;

public class ArticleStocksDb extends GuidKeyQueryableDb<ArticleStock, ArticleStockMetadata> implements ArticleStocks {

	private final transient Article article;
	private final transient Warehouse warehouse;
	private final transient Stocks module;
	
	public ArticleStocksDb(final Base base, final Article article, final Warehouse warehouse, final Stocks module){
		super(base, "Stock de l'article introuvable !");
		this.article = article;	
		this.warehouse = warehouse;
		this.module = module;
		
		if(this.article.id() == null)
			throw new IllegalArgumentException("Vous devez spécifier l'article dont le stock est géré !"); 
	}

	
	@Override
	public ArticleStock get(UUID id) throws IOException {
		ArticleStock stock = newOne(id);
		
		if(stock.id() == null)
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
		
		return newOne(planning.id());
	}

	@Override
	public List<ArticleStock> all() throws IOException {
		List<ArticleStock> items = new ArrayList<ArticleStock>();
		
		if(warehouse.id() == null){
			for (Warehouse wh : article.family().category().module().warehouses().all()) {
				for (Location location : wh.locations().of(LocationType.INTERNAL).all()) {
					items.add(get(location));			
				}
			}
		}else{
			for (Location location : warehouse.locations().of(LocationType.INTERNAL).all()) {
				items.add(get(location));			
			}
		}		
				
		return items;
	}

	private ArticleStock add(Location location) throws IOException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.quantityKey(), 0);
		
		ArticlePlanning planning = article.plannings().get(location);
		ds.set(planning.id(), params);
		
		return newOne(planning.id());
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

	@Override
	protected ArticleStock newOne(UUID id) {
		return new ArticleStockDb(base, id, module);
	}

	@Override
	public ArticleStock none() {
		return new ArticleStockNone();
	}

	@Override
	public ArticleStocks of(Warehouse wh) throws IOException {
		return new ArticleStocksDb(base, article, wh, module);
	}
	
	@Override
	public void delete(ArticleStock item) throws IOException {
		if(item.article().equals(article))
			ds.delete(item.id());
	}
}
