package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.Location;

public final class ArticlePlanningNone extends EntityNone<ArticlePlanning, UUID> implements ArticlePlanning {

	@Override
	public Location location() {
		return new LocationNone();
	}

	@Override
	public Article article() {
		return new ArticleNone();
	}

	@Override
	public double maximumStock() throws IOException {
		return 0;
	}

	@Override
	public double safetyStock() throws IOException {
		return 0;
	}

	@Override
	public double minimumStock() throws IOException {
		return 0;
	}

	@Override
	public void update(double maximumStock, double safetyStock, double minimumStock) throws IOException {

	}

	@Override
	public ArticleStock stock() throws IOException {
		return new ArticleStockNone();
	}
}
