package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticlePlannings;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.StockMovements;

public final class ArticleNone extends EntityNone<Article, UUID> implements Article {

	@Override
	public String name() throws IOException {
		return "Aucun article";
	}

	@Override
	public String internalReference() throws IOException {
		return null;
	}

	@Override
	public double quantity() throws IOException {
		return 0;
	}

	@Override
	public double cost() throws IOException {
		return 0;
	}

	@Override
	public String barCode() throws IOException {
		return null;
	}

	@Override
	public String emballage() throws IOException {
		return null;
	}

	@Override
	public String description() throws IOException {
		return null;
	}

	@Override
	public ArticleFamily family() throws IOException {
		return new ArticleFamilyNone();
	}

	@Override
	public ArticlePlannings plannings() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public ArticleStocks stocks() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void update(String name, String internalReference, String barCode, double quantity, double cost,
			String description, String emballage) throws IOException {
		
	}

	@Override
	public StockMovements movements() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void changeFamily(ArticleFamily newFamily) throws IOException {

	}
}
