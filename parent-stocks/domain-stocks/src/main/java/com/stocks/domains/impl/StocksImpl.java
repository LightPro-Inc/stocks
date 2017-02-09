package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Horodate;
import com.infrastructure.datasource.Base;
import com.securities.api.Company;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.Sequences;
import com.securities.impl.BasisModule;
import com.stocks.domains.api.AllArticleFamilies;
import com.stocks.domains.api.AllArticles;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleCategories;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.Operation.OperationStatut;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.StockMovements;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.Warehouses;

public class StocksImpl implements Stocks {

	private final transient Base base;
	private final transient Module origin;
	
	public StocksImpl(final Base base, final UUID id){
		this.base = base;
		this.origin = new BasisModule(base, id);
	}

	@Override
	public ArticleCategories articleCategories() {
		return new ArticleCategoriesImpl(this.base, this);
	}

	@Override
	public AllArticleFamilies families() {
		return new AllArticleFamiliesImpl(this.base, this);
	}

	@Override
	public AllArticles articles() {
		return new AllArticlesImpl(this.base, this);
	}

	@Override
	public Warehouses warehouses() {
		return new WarehousesImpl(this.base, this);
	}

	@Override
	public List<ArticleStocks> stocks(final Warehouse warehouse) throws IOException {
		List<ArticleStocks> stocks = new ArrayList<ArticleStocks>();
		
		for (Article as : articles().all()) {
			stocks.add(as.stocks(warehouse));
		}
		
		return stocks;
	}

	@Override
	public StockMovements stockMovements() throws IOException {
		return new StockMovementsImpl(this.base);
	}

	@Override
	public Operations operations() throws IOException {
		return new OperationsImpl(this.base, OperationStatut.NONE, this);
	}

	@Override
	public Company company() throws IOException {
		return origin.company();
	}

	@Override
	public String description() throws IOException {
		return origin.description();
	}

	@Override
	public void install() throws IOException {
		origin.install();
	}

	@Override
	public boolean isAvailable() {
		return origin.isAvailable();
	}

	@Override
	public boolean isInstalled() {
		return origin.isInstalled();
	}

	@Override
	public boolean isSubscribed() {
		return origin.isSubscribed();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public int order() throws IOException {
		return origin.order();
	}

	@Override
	public ModuleType type() throws IOException {
		return origin.type();
	}

	@Override
	public void uninstall() throws IOException {
		origin.uninstall();
	}

	@Override
	public Horodate horodate() {
		return origin.horodate();
	}

	@Override
	public UUID id() {
		return origin.id();
	}

	@Override
	public boolean isEqual(Module item) {
		return origin.isEqual(item);
	}

	@Override
	public boolean isNotEqual(Module item) {
		return origin.isNotEqual(item);
	}

	@Override
	public boolean isPresent() {
		return origin.isPresent();
	}

	@Override
	public String shortName() throws IOException {
		return origin.shortName();
	}

	@Override
	public Locations locations() {
		return new LocationsImpl(base, this);
	}

	@Override
	public OperationTypes operationTypes() {
		return new OperationTypesImpl(base, this);
	}

	@Override
	public Sequences sequences() throws IOException {
		return company().sequences();
	}
}
