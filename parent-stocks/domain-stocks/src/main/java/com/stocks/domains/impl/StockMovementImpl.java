package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.StockMovement;
import com.stocks.domains.api.StockMovementMetadata;

public class StockMovementImpl implements StockMovement {

	private final transient Base base;
	private final transient Object id;
	private final transient StockMovementMetadata dm;
	private final transient DomainStore ds;
	
	public StockMovementImpl(final Base base, final Object id){
		this.base = base;
		this.id = id;
		this.dm = dm();
		this.ds = this.base.domainsStore(this.dm).createDs(id);	
	}
	
	@Override
	public UUID id() {
		return UUIDConvert.fromObject(this.id);
	}

	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public int quantity() throws IOException {
		return ds.get(dm.quantityKey());
	}

	@Override
	public Article article() throws IOException {
		UUID articleId = ds.get(dm.articleIdKey());
		return new ArticleImpl(this.base, articleId);
	}

	@Override
	public Operation operation() throws IOException {
		UUID operationId = ds.get(dm.operationIdKey());
		return new OperationImpl(this.base, operationId);
	}

	@Override
	public void update(int quantity, Article article) throws IOException {
		
		if(quantity == 0)
			throw new IllegalArgumentException("Invalid quantity : it can't be equal to zero!");
		
		if(!article.isPresent())
			throw new IllegalArgumentException("Invalid article : it can't be empty!");
		
		ds.set(dm.quantityKey(), quantity);	
		ds.set(dm.articleIdKey(), article.id());	
	}

	public static StockMovementMetadata dm(){
		return new StockMovementMetadata();
	}

	@Override
	public void execute() throws IOException {		
		Location source = operation().sourceLocation();
		Location destination = operation().destinationLocation();
		
		if(source.isInternal())
		{
			ArticleStock stock = this.article().stocks().get(source);
			double quantity = stock.quantity() - this.quantity();
			
			if(quantity < 0)
				throw new IllegalArgumentException(String.format("Le mouvement de stock sur l'article %s n'est pas valide! Quantité en stock : %d / Quantité à retirer : %d", this.article().name(), (long)stock.quantity(), this.quantity()));
			
			stock.updateQuantity(quantity);
		}
		
		if(destination.isInternal()){
			ArticleStock stock = this.article().stocks().get(destination);
			stock.updateQuantity(stock.quantity() + this.quantity());
		}
	}
	
	@Override
	public boolean isEqual(StockMovement item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(StockMovement item) {
		return !isEqual(item);
	}
}
