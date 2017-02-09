package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.AllArticleFamilies;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleCategoryMetadata;
import com.stocks.domains.api.ArticleFamiliesByCategory;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.Stocks;

public class AllArticleFamiliesImpl implements AllArticleFamilies {

	private transient final Base base;
	private final transient ArticleFamilyMetadata dm;
	private final transient DomainsStore ds;
	private final transient Stocks module;
	
	public AllArticleFamiliesImpl(final Base base, final Stocks module){
		this.base = base;
		this.dm = ArticleFamilyImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
		this.module = module;
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
		ArticleCategoryMetadata dmCat = ArticleCategoryImpl.dm();
		String statement = String.format("SELECT fa.%s FROM %s fa "
				+ "JOIN %s ca ON ca.%s=fa.%s "
				+ "WHERE (fa.%s ILIKE ? OR fa.%s ILIKE ?) AND ca.%s=? "
				+ "ORDER BY fa.%s DESC LIMIT ? OFFSET ?", 
				dm.keyName(), dm.domainName(), 
				dmCat.domainName(), dmCat.keyName(), dm.categoryIdKey(),
				dm.nameKey(), dm.descriptionKey(), dmCat.moduleIdKey(), 
				hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
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
		
		ArticleCategoryMetadata dmCat = ArticleCategoryImpl.dm();
		String statement = String.format("SELECT COUNT(fa.%s) FROM %s fa "
				+ "JOIN %s ca ON ca.%s=fa.%s "
				+ "WHERE (fa.%s ILIKE ? OR fa.%s ILIKE ?) AND ca.%s=? ",
				dm.keyName(), dm.domainName(), 
				dmCat.domainName(), dmCat.keyName(), dm.categoryIdKey(),
				dm.nameKey(), dm.descriptionKey(), dmCat.moduleIdKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());			
	}

	@Override
	public ArticleFamily get(UUID id) throws IOException {
		ArticleFamily item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("La catégorie d'article n'a pas été trouvée !");
		
		return build(id);
	}

	@Override
	public ArticleFamily add(String name, String description, ArticleCategory category) throws IOException {
		ArticleFamiliesByCategory families = new ArticleFamiliesImpl(base, category);
		return families.add(name, description);		
	}

	@Override
	public void delete(ArticleFamily item) throws IOException {
		if(contains(item))
			ds.delete(item.id());
	}

	@Override
	public boolean contains(ArticleFamily item) {
		try {
			return item.isPresent() && item.category().module().isEqual(module);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ArticleFamily build(UUID id) {
		return new ArticleFamilyImpl(base, id);
	}
}
