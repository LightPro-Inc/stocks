package com.stocks.domains.impl;

import java.io.IOException;

import com.infrastructure.core.GuidKeyEntityNone;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Warehouse;

public final class ArticleStockNone extends GuidKeyEntityNone<ArticleStock> implements ArticleStock {

	@Override
	public double quantity() throws IOException {
		return 0;
	}

	@Override
	public void updateQuantity(double quantity) throws IOException {

	}

	@Override
	public StockAlert alert() throws IOException {
		return StockAlert.NORMAL;
	}

	@Override
	public Article article() throws IOException {
		return new ArticleNone();
	}

	@Override
	public Warehouse warehouse() throws IOException {
		return new WarehouseNone();
	}

	@Override
	public Location location() throws IOException {
		return new LocationNone();
	}

	@Override
	public ArticlePlanning planning() {
		return new ArticlePlanningNone();
	}
}
