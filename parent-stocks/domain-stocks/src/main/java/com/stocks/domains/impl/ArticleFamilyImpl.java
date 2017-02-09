package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticlesByFamily;

public class ArticleFamilyImpl implements ArticleFamily {

	private final transient Base base;
	private final transient UUID id;
	private final transient ArticleFamilyMetadata dm;
	private final transient DomainStore ds;
	
	public ArticleFamilyImpl(final Base base, final UUID id){
		this.base = base;
		this.id = id;
		this.dm = dm();
		this.ds = this.base.domainsStore(this.dm).createDs(id);	
	}
	
	@Override
	public UUID id() {
		return this.id;
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public String description() throws IOException {
		return ds.get(dm.descriptionKey());
	}

	@Override
	public ArticleCategory category() throws IOException {
		UUID categoryId = ds.get(dm.categoryIdKey());
		return new ArticleCategoryImpl(base, categoryId);
	}

	@Override
	public void update(String name, String description) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.descriptionKey(), description);
		params.put(dm.categoryIdKey(), category().id());
		
		ds.set(params);			
	}

	@Override
	public ArticlesByFamily articles() throws IOException {
		return new ArticlesImpl(base, this);		
	}

	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}

	public static ArticleFamilyMetadata dm(){
		return new ArticleFamilyMetadata();
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}	
	
	@Override
	public boolean isEqual(ArticleFamily item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(ArticleFamily item) {
		return !isEqual(item);
	}
}
