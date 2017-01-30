package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.Horodate;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleMetadata;

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
		
		return new ArticleCategoryImpl(this.base, categoryId);
	}

	@Override
	public void update(String name, String description, UUID categoryId) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (categoryId == null) {
            throw new IllegalArgumentException("Invalid article category : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.descriptionKey(), description);
		params.put(dm.categoryIdKey(), categoryId);
		
		ds.set(params);			
	}

	@Override
	public List<Article> articles() throws IOException {
		
		List<Article> values = new ArrayList<Article>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		ArticleMetadata articledm = ArticleImpl.dm();
		String statement = String.format("SELECT %s FROM %s WHERE %s = ? ORDER BY %s DESC", articledm.keyName(), articledm.domainName(), articledm.familyIdKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(this.id);
		
		List<DomainStore> results = base.domainsStore(dm).findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new ArticleImpl(this.base, UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;		
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
		try {
			return base.domainsStore(dm).exists(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
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
