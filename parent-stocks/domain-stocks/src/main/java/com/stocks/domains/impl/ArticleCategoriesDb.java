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
import com.securities.api.MesureUnit;
import com.stocks.domains.api.ArticleCategories;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleCategoryMetadata;
import com.stocks.domains.api.Stocks;

public class ArticleCategoriesDb extends AdvancedQueryableDb<ArticleCategory, UUID, ArticleCategoryMetadata> implements ArticleCategories {

	private final transient Stocks module;
	
	public ArticleCategoriesDb(final Base base, final Stocks module){
		super(base, "Catégorie de famille introuvable !");
		this.module = module;
	}

	@Override
	public ArticleCategory add(String name, MesureUnit mesureUnit) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (mesureUnit.id() == null) {
            throw new IllegalArgumentException("Vous devez renseigner l'unité de mesure !");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.mesureUnitIdKey(), mesureUnit.id());
		params.put(dm.moduleIdKey(), module.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);			
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		String statement = String.format("%s cat "
				+ "WHERE cat.%s ILIKE ? AND cat.%s=?",
				dm.domainName(), 
				dm.nameKey(), dm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add(module.id());
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY cat.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("cat.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected UUID convertKey(Object id) {
		return UUIDConvert.fromObject(id);
	}

	@Override
	protected ArticleCategory newOne(UUID id) {
		return new ArticleCategoryDb(base, id, module);
	}

	@Override
	public ArticleCategory none() {
		return new ArticleCategoryNone();
	}
}
