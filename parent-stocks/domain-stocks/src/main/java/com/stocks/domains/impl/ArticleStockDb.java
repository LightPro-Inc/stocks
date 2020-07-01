package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.ArticleStockMetadata;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;

public class ArticleStockDb extends GuidKeyEntityDb<ArticleStock, ArticleStockMetadata> implements ArticleStock {
	
	private final Stocks module;
	
	public ArticleStockDb(final Base base, final UUID id, Stocks module){
		super(base, id, "Stock d'article introuvable !");
		this.module = module;
	}

	@Override
	public Location location() throws IOException {
		return planning().location();
	}

	@Override
	public Article article() throws IOException {
		return planning().article();
	}

	@Override
	public double quantity() throws IOException {
		return ds.get(dm.quantityKey());
	}

	public static ArticleStockMetadata dm(){
		return new ArticleStockMetadata();
	}

	@Override
	public void updateQuantity(double quantity) throws IOException {
		ds.set(dm.quantityKey(), quantity);
	}

	@Override
	public StockAlert alert() throws IOException {
		StockAlert alert;
		
		ArticlePlanning planning = planning();
		
		double quantity = quantity();
		double maximumStock = planning.maximumStock();
		double safetyStock = planning.safetyStock();
		double minimumStock = planning.minimumStock();
		
		if(maximumStock != 0 && quantity >= maximumStock)
			alert = StockAlert.MAXIMUM;
		else if(quantity <= minimumStock)
			alert = StockAlert.MINIMUM;
		else if(quantity <= safetyStock + minimumStock)
			alert = StockAlert.SAFETY;
		else
			alert = StockAlert.NORMAL;
		
		return alert;
	}

	@Override
	public ArticlePlanning planning() {
		return new ArticlePlanningDb(base, id, module);
	}

	@Override
	public Warehouse warehouse() throws IOException {
		return location().warehouse();
	}
}
