package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.AdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleCategoryMetadata;
import com.stocks.domains.api.ArticleFamilies;
import com.stocks.domains.api.ArticleFamiliesByCategory;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.Stocks;

public class ArticleFamiliesDb extends AdvancedQueryableDb<ArticleFamily, UUID, ArticleFamilyMetadata> implements ArticleFamiliesByCategory {

	private final transient ArticleCategory category;
	private final transient Stocks module;
	
	public ArticleFamiliesDb(final Base base, final Stocks module, final ArticleCategory category){
		super(base, "Famille d'article introuvable !");
		this.module = module;	
		this.category = category;
	}

	@Override
	public ArticleFamily add(String name, String description) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if(category.id() == null)
			throw new IllegalArgumentException("Vous devez renseigner la catégorie de la famille !");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.descriptionKey(), description);
		params.put(dm.categoryIdKey(), category.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public ArticleFamilies of(ArticleCategory category) {
		return new ArticleFamiliesDb(base, module, category);
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		ArticleCategoryMetadata catDm = new ArticleCategoryMetadata();
		String statement = String.format("%s fa "
				+ "JOIN %s cat ON cat.%s=fa.%s "
				+ "WHERE fa.%s ILIKE ? AND cat.%s=?",
				dm.domainName(), 
				catDm.domainName(), catDm.keyName(), dm.categoryIdKey(),
				dm.nameKey(), catDm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(category.id() != null){
			statement = String.format("%s AND cat.%s=?", statement, catDm.keyName());
			params.add(category.id());
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY fa.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("fa.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected UUID convertKey(Object id) {
		return UUIDConvert.fromObject(id);
	}

	@Override
	protected ArticleFamily newOne(UUID id) {
		return new ArticleFamilyDb(base, id, module);
	}

	@Override
	public ArticleFamily none() {
		return new ArticleFamilyNone();
	}
}
