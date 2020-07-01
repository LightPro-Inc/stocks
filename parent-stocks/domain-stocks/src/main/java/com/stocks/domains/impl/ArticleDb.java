package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleMetadata;
import com.stocks.domains.api.ArticlePlannings;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.Articles;
import com.stocks.domains.api.StockMovements;
import com.stocks.domains.api.Stocks;

public final class ArticleDb extends GuidKeyEntityDb<Article, ArticleMetadata> implements Article {
	
	private final Stocks module;
	
	public ArticleDb(final Base base, final UUID id, final Stocks module){
		super(base, id, "Article introuvable !");
		this.module = module;
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public String internalReference() throws IOException {
		return ds.get(dm.internalReferenceKey());
	}

	@Override
	public double quantity() throws IOException {
		return ds.get(dm.quantityKey());
	}
	
	@Override
	public double cost() throws IOException {
		return ds.get(dm.costKey());
	}

	@Override
	public ArticleFamily family() throws IOException {
		UUID familyId = ds.get(dm.familyIdKey());
		return new ArticleFamilyDb(base, familyId, module);
	}

	@Override
	public String description() throws IOException {
		return ds.get(dm.descriptionKey());
	}

	@Override
	public void update(String name, String internalReference, String barCode, double quantity, double cost, String description, String emballage) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if(StringUtils.isBlank(emballage))
			throw new IllegalArgumentException("Emballage invalide : il ne peut pas être vide !");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(!StringUtils.isBlank(internalReference)) {
			internalReference = internalReference.trim();
			
			Articles articles = family().category().module().articles().withInternalReference(internalReference);
			
			if(!articles.isEmpty() && !articles.first().equals(this))
				throw new IllegalArgumentException("Cette référence interne existe déjà !");
			
			params.put(dm.internalReferenceKey(), internalReference);
		}
				
		params.put(dm.nameKey(), name);		
		params.put(dm.quantityKey(), quantity);		
		params.put(dm.costKey(), cost);		
		params.put(dm.descriptionKey(), description);
		params.put(dm.familyIdKey(), family().id());
		params.put(dm.barCodeKey(), barCode);
		params.put(dm.emballageKey(), emballage);
		
		ds.set(params);					
	}

	@Override
	public String barCode() throws IOException {
		return ds.get(dm.barCodeKey());
	}

	@Override
	public ArticlePlannings plannings() throws IOException {
		return new ArticlePlanningsDb(this.base, this, module);
	}

	@Override
	public ArticleStocks stocks() throws IOException {
		return new ArticleStocksDb(this.base, this, new WarehouseNone(), module);
	}

	@Override
	public String emballage() throws IOException {
		return ds.get(dm.emballageKey());
	}

	@Override
	public StockMovements movements() throws IOException {
		return family().category().module().stockMovements().of(this);
	}
	
	@Override
	public void changeFamily(ArticleFamily newFamily) throws IOException {
		
		if(newFamily.isNone())
			throw new IllegalArgumentException("Aucune famille n'a été spécifiée !");
		
		if(family().equals(newFamily))
			return;
		
		ds.set(dm.familyIdKey(), newFamily.id());
	}
}
