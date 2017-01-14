package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.ArticleFamilies;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;

public class ArticleFamiliesImpl implements ArticleFamilies {

	private transient final Base base;
	private final transient ArticleFamilyMetadata dm;
	private final transient DomainsStore ds;
	
	public ArticleFamiliesImpl(final Base base){
		this.base = base;
		this.dm = ArticleFamilyImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
	}
	
	@Override
	public List<ArticleFamily> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public List<ArticleFamily> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<ArticleFamily> find(int page, int pageSize, String filter) throws IOException {
		List<ArticleFamily> values = new ArrayList<ArticleFamily>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		String statement = String.format("SELECT %s FROM %s WHERE %s ILIKE ? OR %s ILIKE ? ORDER BY %s DESC LIMIT ? OFFSET ?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.descriptionKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new ArticleFamilyImpl(this.base, domainStore.key())); 
		}		
		
		return values;			
	}

	@Override
	public int totalCount(String filter) throws IOException {
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s ILIKE ? OR %s ILIKE ?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.descriptionKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());			
	}

	@Override
	public ArticleFamily get(UUID id) throws IOException {
		if(!ds.exists(id))
			throw new NotFoundException("La catégorie d'article n'a pas été trouvée !");
		
		return new ArticleFamilyImpl(this.base, id);
	}

	@Override
	public ArticleFamily add(String name, String description, UUID categoryId) throws IOException {
		
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
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new ArticleFamilyImpl(this.base, id);
	}

	@Override
	public void delete(ArticleFamily category) throws IOException {
		ds.delete(category.id());
	}

	@Override
	public boolean exists(Object id) throws IOException {
		return ds.exists(id);
	}
}
