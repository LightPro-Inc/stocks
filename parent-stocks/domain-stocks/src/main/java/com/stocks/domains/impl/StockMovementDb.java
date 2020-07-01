package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleStock;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.StockMovement;
import com.stocks.domains.api.StockMovementMetadata;
import com.stocks.domains.api.Stocks;

public final class StockMovementDb extends GuidKeyEntityDb<StockMovement, StockMovementMetadata> implements StockMovement {
	
	private final Stocks module;
	
	public StockMovementDb(final Base base, final UUID id, final Stocks module){
		super(base, id, "Mouvement de stock introuvable !");
		this.module = module;
	}

	@Override
	public double quantity() throws IOException {
		return ds.get(dm.quantityKey());
	}

	@Override
	public Article article() throws IOException {
		UUID articleId = ds.get(dm.articleIdKey());
		return new ArticleDb(base, articleId, module);
	}

	@Override
	public Operation operation() throws IOException {
		UUID operationId = ds.get(dm.operationIdKey());
		return new OperationDb(this.base, operationId, module);
	}

	@Override
	public void update(double quantity, Article article) throws IOException {
		
		if(quantity == 0)
			throw new IllegalArgumentException("Invalid quantity : it can't be equal to zero!");
		
		if(article.id() == null)
			throw new IllegalArgumentException("Invalid article : it can't be empty!");
		
		ds.set(dm.quantityKey(), quantity);	
		ds.set(dm.articleIdKey(), article.id());	
	}

	@Override
	public void execute() throws IOException {		
		Location source = operation().sourceLocation();
		Location destination = operation().destinationLocation();
		
		if(source.isInternal())
		{
			ArticleStock stock = this.article().stocks().of(source.warehouse()).get(source);
			double quantity = stock.quantity() - this.quantity();
			
			if(quantity < 0)
				throw new IllegalArgumentException(String.format("Le mouvement de stock sur l'article %s n'est pas valide! Quantité en stock : %d / Quantité à retirer : %d", this.article().name(), (long)stock.quantity(), this.quantity()));
			
			stock.updateQuantity(quantity);
		}
		
		if(destination.isInternal()){
			ArticleStock stock = this.article().stocks().of(destination.warehouse()).get(destination);
			stock.updateQuantity(stock.quantity() + this.quantity());
		}
	}
}
