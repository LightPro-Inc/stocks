package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.securities.api.MesureUnit;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleCategoryMetadata;
import com.stocks.domains.api.ArticleFamilies;
import com.stocks.domains.api.Stocks;

public final class ArticleCategoryDb extends GuidKeyEntityDb<ArticleCategory, ArticleCategoryMetadata> implements ArticleCategory {
	
	private final Stocks module;
	
	public ArticleCategoryDb(final Base base, final UUID id, final Stocks module) {
		super(base, id, "Catégorie de famille introuvable !");	
		this.module = module;
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public MesureUnit mesureUnit() throws IOException {
		UUID mesureUnitId = ds.get(dm.mesureUnitIdKey());
		return module().mesureUnits().get(mesureUnitId);
	}

	@Override
	public void update(String name, MesureUnit mesureUnit) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (mesureUnit.id() == null) {
            throw new IllegalArgumentException("Invalid mesure unit : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.mesureUnitIdKey(), mesureUnit.id());
		
		ds.set(params);		
	}

	@Override
	public ArticleFamilies families() throws IOException {
		return module().articleFamilies().of(this);
	}

	@Override
	public Stocks module() throws IOException {
		return module;
	}
}
