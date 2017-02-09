package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.ArticleCategories;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleCategoryMetadata;
import com.stocks.domains.api.Stocks;

public class ArticleCategoriesImpl implements ArticleCategories {

	private final transient Base base;
	private final transient ArticleCategoryMetadata dm;
	private final transient DomainsStore ds;
	private final transient Stocks module;
	
	public ArticleCategoriesImpl(final Base base, final Stocks module){
		this.base = base;
		this.dm = ArticleCategoryImpl.dm();
		this.ds = base.domainsStore(dm);
		this.module = module;
	}

	@Override
	public ArticleCategory get(UUID id) throws IOException {
		
		ArticleCategory item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("La catégorie d'article n'a pas été trouvée !");
		
		return build(id);
	}

	@Override
	public ArticleCategory add(String name, UUID mesureUnitId) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (mesureUnitId == null) {
            throw new IllegalArgumentException("Invalid mesureUnitId : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.mesureUnitIdKey(), mesureUnitId);
		params.put(dm.moduleIdKey(), module.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);			
	}

	@Override
	public void delete(ArticleCategory item) throws IOException {
		if(contains(item))
			ds.delete(item.id());
	}

	@Override
	public List<ArticleCategory> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public List<ArticleCategory> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<ArticleCategory> find(int page, int pageSize, String filter) throws IOException {
		List<ArticleCategory> values = new ArrayList<ArticleCategory>();
				
		HorodateMetadata hm = HorodateImpl.dm();
		String statement = String.format("SELECT %s FROM %s WHERE %s ILIKE ? AND %s=? ORDER BY %s DESC LIMIT ? OFFSET ?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.moduleIdKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(build(UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;		
	}

	@Override
	public int totalCount(String filter) throws IOException {	
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s ILIKE ? AND %s=?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.moduleIdKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add(module.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());		
	}

	@Override
	public boolean contains(ArticleCategory item) {
		try {
			return item.isPresent() && item.module().isEqual(module);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false; 
	}

	@Override
	public ArticleCategory build(UUID id) {
		return new ArticleCategoryImpl(base, id);
	}
}
