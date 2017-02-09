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
import com.stocks.domains.api.AllArticles;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleCategoryMetadata;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.ArticleMetadata;
import com.stocks.domains.api.ArticlesByFamily;
import com.stocks.domains.api.Stocks;

public class AllArticlesImpl implements AllArticles {

	private transient final Base base;
	private final transient ArticleMetadata dm;
	private final transient DomainsStore ds;
	private final transient Stocks module;
	
	public AllArticlesImpl(final Base base, final Stocks module){
		this.base = base;
		this.dm = ArticleImpl.dm();
		this.ds = this.base.domainsStore(this.dm);	
		this.module = module;
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
		ArticleFamilyMetadata dmFa = ArticleFamilyImpl.dm();
		ArticleCategoryMetadata dmCat = ArticleCategoryImpl.dm();
		String statement = String.format("SELECT art.%s FROM %s art "
				+ "JOIN %s fa ON fa.%s=art.%s "
				+ "left JOIN %s ca ON ca.%s=fa.%s "
				+ "WHERE (art.%s ILIKE ? OR art.%s ILIKE ?) AND ca.%s=? "
				+ "ORDER BY art.%s DESC LIMIT ? OFFSET ?", 
				dm.keyName(), dm.domainName(), 
				dmFa.domainName(), dmFa.keyName(), dm.familyIdKey(),
				dmCat.domainName(), dmCat.keyName(), dmFa.categoryIdKey(),
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
		
		ArticleFamilyMetadata dmFa = ArticleFamilyImpl.dm();
		ArticleCategoryMetadata dmCat = ArticleCategoryImpl.dm();
		String statement = String.format("SELECT COUNT(art.%s) FROM %s art "
				+ "JOIN %s fa ON fa.%s=art.%s "
				+ "left JOIN %s ca ON ca.%s=fa.%s "
				+ "WHERE (art.%s ILIKE ? OR art.%s ILIKE ?) AND ca.%s=? ",
				dm.keyName(), dm.domainName(), 
				dmFa.domainName(), dmFa.keyName(), dm.familyIdKey(),
				dmCat.domainName(), dmCat.keyName(), dmFa.categoryIdKey(),
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
	public Article get(UUID id) throws IOException {
		Article item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("L'article n'a pas été trouvé !");
		
		return build(id);
	}

	@Override
	public Article add(String name, String internalReference, String barCode, int quantity, double cost, String description, ArticleFamily family) throws IOException {
		ArticlesByFamily articles = new ArticlesImpl(base, family);
		return articles.add(name, internalReference, barCode, quantity, cost, description);	
	}

	@Override
	public void delete(Article item) throws IOException {
		if(contains(item))
			ds.delete(item.id());
	}

	@Override
	public boolean contains(Article item) {
		try {
			return ds.exists(item.id()) && item.family().category().module().isEqual(module);
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
