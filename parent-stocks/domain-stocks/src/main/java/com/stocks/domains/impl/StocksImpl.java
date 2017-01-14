package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.infrastructure.datasource.Base;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleFamilies;
import com.stocks.domains.api.ArticleCategories;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.Articles;
import com.stocks.domains.api.LocationTypes;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.Operation.OperationStatut;
import com.stocks.domains.api.OperationCategories;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.StockMovements;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouses;

public class StocksImpl implements Stocks {

	private final transient Base base;
	
	public StocksImpl(Base base){
		this.base = base;
	}

	@Override
	public ArticleCategories articleCategories() {
		return new ArticleCategoriesImpl(this.base);
	}

	@Override
	public ArticleFamilies articleFamilies() {
		return new ArticleFamiliesImpl(this.base);
	}

	@Override
	public Articles articles() {
		return new ArticlesImpl(this.base);
	}

	@Override
	public Warehouses warehouses() {
		return new WarehousesImpl(this.base);
	}

	@Override
	public LocationTypes locationTypes() {
		return new LocationTypesImpl();
	}

	@Override
	public OperationTypes operationTypes() {
		return new OperationTypesImpl(this.base);
	}

	@Override
	public Locations locations() {
		return new LocationsImpl(this.base);
	}

	@Override
	public OperationCategories operationCategories() {
		return new OperationCategoriesImpl();
	}

	@Override
	public List<ArticleStocks> stocks() throws IOException {
		List<ArticleStocks> stocks = new ArrayList<ArticleStocks>();
		
		for (Article as : articles().all()) {
			stocks.add(as.stocks());
		}
		
		return stocks;
	}

	@Override
	public StockMovements stockMovements() throws IOException {
		return new StockMovementsImpl(this.base);
	}

	@Override
	public Operations operations() throws IOException {
		return new OperationsImpl(this.base, OperationStatut.NONE);
	}
}
