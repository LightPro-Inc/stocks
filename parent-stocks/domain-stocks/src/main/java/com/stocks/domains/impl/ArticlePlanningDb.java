package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticlePlanningMetadata;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Stocks;

public final class ArticlePlanningDb extends GuidKeyEntityDb<ArticlePlanning, ArticlePlanningMetadata> implements ArticlePlanning {

	private final Stocks module;
	
	public ArticlePlanningDb(final Base base, final UUID id, final Stocks module){
		super(base, id, "Plannification de l'article introuvable !");
		this.module = module;
	}

	@Override
	public Location location() throws IOException {
		UUID locationId = ds.get(dm.locationIdKey());
		return new LocationDb(base, locationId, module);
	}

	@Override
	public Article article() throws IOException {
		UUID articleId = ds.get(dm.articleIdKey());
		return new ArticleDb(base, articleId, module);
	}

	@Override
	public double maximumStock() throws IOException {
		return ds.get(dm.maximumStockKey());
	}

	@Override
	public double safetyStock() throws IOException {
		return ds.get(dm.safetyStockKey());
	}

	@Override
	public double minimumStock() throws IOException {
		return ds.get(dm.minimumStockKey());
	}

	@Override
	public void update(double maximumStock, double safetyStock, double minimumStock) throws IOException {
		
		if (maximumStock < 0) {
            throw new IllegalArgumentException("Invalid maximum stock : it can't be negative !");
        }
		
		if (safetyStock < 0) {
            throw new IllegalArgumentException("Invalid safety stock : it can't be negative !");
        }
		
		if (minimumStock < 0) {
            throw new IllegalArgumentException("Invalid minimum stock : it can't be negative !");
        }
		
		/*if(!(maximumStock >= safetyStock + minimumStock))
			throw new IllegalArgumentException("Stock maximum doit être supérieur ou égal à Stock d'alerte + Stock minimum  !");*/
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.maximumStockKey(), maximumStock);
		params.put(dm.safetyStockKey(), safetyStock);
		params.put(dm.minimumStockKey(), minimumStock);		
		
		ds.set(params);			
	}

	@Override
	public ArticleStock stock() throws IOException {
		return new ArticleStockDb(base, id, module);
	}
}
