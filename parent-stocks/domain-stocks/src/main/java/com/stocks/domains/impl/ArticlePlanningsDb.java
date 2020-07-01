package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.QueryableDb;
import com.infrastructure.datasource.Base;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticlePlanningMetadata;
import com.stocks.domains.api.ArticlePlannings;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.Stocks;

public class ArticlePlanningsDb extends QueryableDb<ArticlePlanning, UUID, ArticlePlanningMetadata> implements ArticlePlannings {

	private final transient Article article;
	private final transient Stocks module;
	
	public ArticlePlanningsDb(final Base base, final Article article, final Stocks module){
		super(base, "Plannification de l'article introuvable !");
		this.article = article;	
		this.module = module;
		
		if(article.id() == null)
			throw new IllegalArgumentException("Vous devez spécifier l'article planifié !");
	}
	
	@Override
	public ArticlePlanning get(Location location) throws IOException {
		
		String statement = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?", dm.keyName(), dm.domainName(), dm.articleIdKey(), dm.locationIdKey());
		List<Object> results = ds.find(statement, Arrays.asList(article.id(), location.id()));
		
		Object id;
		if(results.isEmpty()) {
			ArticlePlanning added = add(0, 0, 0, location);
			id = added.id();
		} else
			id = results.get(0);
		
		return newOne(UUIDConvert.fromObject(id));
	}

	private ArticlePlanning add(int maximumStock, int safetyStock, int minimumStock, Location location) throws IOException {
		
		if (maximumStock < 0) {
            throw new IllegalArgumentException("Invalid maximum stock : it can't be negative !");
        }
		
		if (safetyStock < 0) {
            throw new IllegalArgumentException("Invalid safety stock : it can't be negative !");
        }
		
		if (minimumStock < 0) {
            throw new IllegalArgumentException("Invalid minimum stock : it can't be negative !");
        }
		
		if (location == null || location.id() == null){
            throw new IllegalArgumentException("Invalid location : it can't be empty !");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.maximumStockKey(), maximumStock);
		params.put(dm.safetyStockKey(), safetyStock);
		params.put(dm.minimumStockKey(), minimumStock);
		params.put(dm.locationIdKey(), location.id());
		params.put(dm.articleIdKey(), article.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new ArticlePlanningDb(this.base, id, module);
	}

	@Override
	public List<ArticlePlanning> all() throws IOException {
		List<ArticlePlanning> items = new ArrayList<ArticlePlanning>();
		
		List<Location> locations = article.family().category().module().locations().of(LocationType.INTERNAL).all();		
		for (Location location : locations) {
			items.add(get(location)); 
		}
		
		return items;
	}

	@Override
	protected ArticlePlanning newOne(UUID id) {
		return new ArticlePlanningDb(base, id, module);
	}

	@Override
	public ArticlePlanning none() {
		return new ArticlePlanningNone();
	}

	@Override
	public void deleteAll() throws IOException {		
		for (ArticlePlanning item : all()) {
			delete(item);
		}
	}
	
	@Override
	public void delete(ArticlePlanning item) throws IOException {
		if(item.article().equals(article)){
			// suppression du stock de l'article
			article.stocks().delete(item.stock());
			
			// suppression des données de plannification
			ds.delete(item.id());
		}
	}
}
