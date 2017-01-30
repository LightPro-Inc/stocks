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
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleMetadata;
import com.stocks.domains.api.Articles;

public class ArticlesImpl implements Articles {

	private transient final Base base;
	private final transient ArticleMetadata dm;
	private final transient DomainsStore ds;
	
	public ArticlesImpl(final Base base){
		this.base = base;
		this.dm = ArticleImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
	}
	
	@Override
	public List<Article> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public List<Article> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<Article> find(int page, int pageSize, String filter) throws IOException {
		List<Article> values = new ArrayList<Article>();
		
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
			values.add(new ArticleImpl(this.base, UUIDConvert.fromObject(domainStore.key()))); 
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
	public Article get(UUID id) throws IOException {
		if(!ds.exists(id))
			throw new NotFoundException("L'article n'a pas été trouvé !");
		
		return new ArticleImpl(this.base, id);
	}

	@Override
	public Article add(String name, String internalReference, String barCode, int quantity, double cost, String description, UUID familyId) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (familyId == null) {
            throw new IllegalArgumentException("Invalid article family : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.internalReferenceKey(), internalReference);
		params.put(dm.quantityKey(), quantity);		
		params.put(dm.costKey(), cost);		
		params.put(dm.descriptionKey(), description);
		params.put(dm.familyIdKey(), familyId);
		params.put(dm.barCodeKey(), barCode);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new ArticleImpl(this.base, id);		
	}

	@Override
	public void delete(Article article) throws IOException {
		ds.delete(article.id());
	}

	@Override
	public boolean contains(Article item) {
		try {
			return ds.exists(item.id());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Article build(UUID id) {
		return new ArticleImpl(base, id);
	}
}
