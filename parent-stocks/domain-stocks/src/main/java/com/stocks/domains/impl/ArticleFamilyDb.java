package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.Articles;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.ArticleCategory;

public final class ArticleFamilyDb extends GuidKeyEntityDb<ArticleFamily, ArticleFamilyMetadata> implements ArticleFamily {
	
	private final Stocks module;
	
	public ArticleFamilyDb(final Base base, final UUID id, final Stocks module){
		super(base, id, "Famille d'article introuvable !");
		this.module = module;
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
		return new ArticleCategoryDb(base, categoryId, module);
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
	public Articles articles() throws IOException {
		return category().module().articles().of(this);
	}

	@Override
	public void changeCategory(ArticleCategory newCategory) throws IOException {
		
		if(newCategory.isNone())
			throw new IllegalArgumentException("Aucune catégorie n'a été spécifiée !");
		
		if(category().equals(newCategory))
			return;
		
		ds.set(dm.categoryIdKey(), newCategory.id());
	}
}
