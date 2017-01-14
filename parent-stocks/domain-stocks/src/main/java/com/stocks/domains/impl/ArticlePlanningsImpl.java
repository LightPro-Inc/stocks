package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticlePlanningMetadata;
import com.stocks.domains.api.ArticlePlannings;
import com.stocks.domains.api.Location;

public class ArticlePlanningsImpl implements ArticlePlannings {

	private transient final Base base;
	private final transient ArticlePlanningMetadata dm;
	private final transient Object articleId;
	private final transient DomainsStore ds;
	
	public ArticlePlanningsImpl(final Base base, final Object articleId){
		this.base = base;
		this.articleId = articleId;
		this.dm = ArticlePlanningImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
	}
	
	@Override
	public ArticlePlanning get(Location location) throws IOException {
		String statement = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?", dm.keyName(), dm.domainName(), dm.articleIdKey(), dm.locationIdKey());
		List<Object> results = ds.find(statement, Arrays.asList(articleId, location.id()));
		
		Object id;
		if(results.isEmpty())
		{
			ArticlePlanning added = add(0, 0, 0, location);
			id = added.id();
		}else
			id = results.get(0);
		
		return new ArticlePlanningImpl(this.base, id);
	}

	@Override
	public List<ArticlePlanning> all() throws IOException {
		List<ArticlePlanning> items = new ArrayList<ArticlePlanning>();
		
		List<Location> locations = new LocationsImpl(this.base).internals();
		for (Location location : locations) {
			items.add(get(location));			
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
		params.put(dm.articleIdKey(), articleId);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new ArticlePlanningImpl(this.base, id);
	}

	@Override
	public ArticlePlanning get(UUID id) throws IOException {
		
		ArticlePlanning planning = new ArticlePlanningImpl(this.base, id);
		
		if(ds.exists(id) && planning.article().id().equals(articleId))
			return planning;
		
		throw new NotFoundException("Les données de plannification n'ont pas été retrouvées pour l'article !");
	}

}
