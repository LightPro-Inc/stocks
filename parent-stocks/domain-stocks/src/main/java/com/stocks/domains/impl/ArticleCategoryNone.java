package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.securities.api.MesureUnit;
import com.securities.impl.MesureUnitNone;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleFamilies;
import com.stocks.domains.api.Stocks;

public final class ArticleCategoryNone extends EntityNone<ArticleCategory, UUID> implements ArticleCategory {

	@Override
	public String name() throws IOException {
		return "Aucune catégorie";
	}

	@Override
	public MesureUnit mesureUnit() throws IOException {
		return new MesureUnitNone();
	}

	@Override
	public ArticleFamilies families() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !"); 
	}

	@Override
	public void update(String name, MesureUnit mesureUnit) throws IOException {
	
	}

	@Override
	public Stocks module() throws IOException {
		return new StocksNone();
	}
}
