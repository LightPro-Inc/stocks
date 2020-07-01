package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.Articles;

public final class ArticleFamilyNone extends EntityNone<ArticleFamily, UUID> implements ArticleFamily {

	@Override
	public String name() throws IOException {
		return "Aucune famille d'article";
	}

	@Override
	public String description() throws IOException {
		return null;
	}

	@Override
	public ArticleCategory category() throws IOException {
		return null;
	}

	@Override
	public Articles articles() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void update(String name, String description) throws IOException {

	}

	@Override
	public void changeCategory(ArticleCategory newCategory) throws IOException {

	}

}
