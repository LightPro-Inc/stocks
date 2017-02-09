package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticlePlanningMetadata;
import com.stocks.domains.api.ArticlePlannings;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Warehouse;

public class ArticlePlanningsImpl implements ArticlePlannings {

	private transient final Base base;
	private final transient ArticlePlanningMetadata dm;
	private final transient DomainsStore ds;
	private final transient Article article;
	
	public ArticlePlanningsImpl(final Base base, final Article article){
		this.base = base;
		this.article = article;
		this.dm = ArticlePlanningImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
	}
	
	@Override
	public ArticlePlanning get(Location location) throws IOException {
		String statement = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?", dm.keyName(), dm.domainName(), dm.articleIdKey(), dm.locationIdKey());
		List<Object> results = ds.find(statement, Arrays.asList(article.id(), location.id()));
		
		Object id;
		if(results.isEmpty())
		{
			ArticlePlanning added = add(0, 0, 0, location);
			id = added.id();
		}else
			id = results.get(0);
		
		return new ArticlePlanningImpl(this.base, UUIDConvert.fromObject(id), article, location);
	}

	@Override
	public List<ArticlePlanning> all() throws IOException {
		List<ArticlePlanning> items = new ArrayList<ArticlePlanning>();
		
		for (Warehouse ws : article.family().category().module().warehouses().all()) {
			for (Location location : ws.locations().internals()) {
				items.add(get(location));			
			}
		}
				
		return items;
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
		
		return new ArticlePlanningImpl(this.base, id, article, location);
	}

	@Override
	public ArticlePlanning get(UUID id) throws IOException {
		
		for (ArticlePlanning ap : all()) {
			if(ap.id() == id) {
				return ap;
			}
		}
		
		throw new NotFoundException("Les données de plannification n'ont pas été retrouvées pour l'article !");
	}

}
