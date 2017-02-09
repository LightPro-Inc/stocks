package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticlePlanning;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.ArticleStockMetadata;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Warehouse;

public class ArticleStockImpl implements ArticleStock {

	private final transient Base base;
	private final transient ArticleStockMetadata dm;
	private final transient DomainStore ds;
	private final transient ArticlePlanning planning;
	
	public ArticleStockImpl(final Base base, final ArticlePlanning planning){
		this.base = base;
		this.planning = planning;
		this.dm = dm();
		this.ds = this.base.domainsStore(this.dm).createDs(planning.id());	
	}
	
	@Override
	public UUID id() {
		return planning.id();
	}

	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
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
		int maximumStock = planning.maximumStock();
		int safetyStock = planning.safetyStock();
		int minimumStock = planning.minimumStock();
		
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
		return planning;
	}

	@Override
	public boolean isPresent(){
		return base.domainsStore(dm).exists(planning.id());
	}
	
	@Override
	public boolean isEqual(ArticleStock item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(ArticleStock item) {
		return !isEqual(item);
	}

	@Override
	public Warehouse warehouse() throws IOException {
		return location().warehouse();
	}
}
