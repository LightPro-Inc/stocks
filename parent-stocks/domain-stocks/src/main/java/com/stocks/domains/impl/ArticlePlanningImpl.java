package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticlePlanningMetadata;
import com.stocks.domains.api.Location;

public class ArticlePlanningImpl implements ArticlePlanning {

	private final transient Base base;
	private final transient Object id;
	private final transient ArticlePlanningMetadata dm;
	private final transient DomainStore ds;
	
	public ArticlePlanningImpl(final Base base, final Object id){
		this.base = base;
		this.id = id;
		this.dm = dm();
		this.ds = this.base.domainsStore(this.dm).createDs(id);	
	}
	
	@Override
	public UUID id() {
		return UUIDConvert.fromObject(this.id);
	}

	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}

	@Override
	public Location location() throws IOException {
		UUID locationId = ds.get(dm.locationIdKey());
		return new LocationImpl(this.base, locationId);
	}

	@Override
	public Article article() throws IOException {
		UUID articleId = ds.get(dm.articleIdKey());
		return new ArticleImpl(this.base, articleId);
	}

	@Override
	public int maximumStock() throws IOException {
		return ds.get(dm.maximumStockKey());
	}

	@Override
	public int safetyStock() throws IOException {
		return ds.get(dm.safetyStockKey());
	}

	@Override
	public int minimumStock() throws IOException {
		return ds.get(dm.minimumStockKey());
	}

	@Override
	public void update(int maximumStock, int safetyStock, int minimumStock) throws IOException {
		
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

	public static ArticlePlanningMetadata dm(){
		return new ArticlePlanningMetadata();
	}

	@Override
	public boolean isPresent() throws IOException {
		return base.domainsStore(dm).exists(id);
	}
	
	@Override
	public boolean isEqual(ArticlePlanning item) throws IOException {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(ArticlePlanning item) throws IOException {
		return !isEqual(item);
	}
}
