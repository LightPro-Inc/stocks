package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleMetadata;
import com.stocks.domains.api.ArticlePlannings;
import com.stocks.domains.api.ArticleStocks;

public class ArticleImpl implements Article {

	private final transient Base base;
	private final transient UUID id;
	private final transient ArticleMetadata dm;
	private final transient DomainStore ds;
	
	public ArticleImpl(final Base base, final UUID id){
		this.base = base;
		this.id = id;
		this.dm = dm();
		this.ds = this.base.domainsStore(this.dm).createDs(id);	
	}
	
	@Override
	public UUID id() {
		return this.id;
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
	public int quantity() throws IOException {
		return ds.get(dm.quantityKey());
	}
	
	@Override
	public double cost() throws IOException {
		return ds.get(dm.costKey());
	}

	@Override
	public ArticleFamily family() throws IOException {
		UUID familyId = ds.get(dm.familyIdKey());		
		return new ArticleFamilyImpl(this.base, familyId);
	}

	@Override
	public String description() throws IOException {
		return ds.get(dm.descriptionKey());
	}

	@Override
	public void update(String name, String internalReference, String barCode, int quantity, double cost, String description, UUID familyId) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (familyId == null) {
            throw new IllegalArgumentException("Invalid article family : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.internalReferenceKey(), internalReference);
		params.put(dm.quantityKey(), quantity);		
		params.put(dm.costKey(), cost);		
		params.put(dm.descriptionKey(), description);
		params.put(dm.familyIdKey(), familyId);
		params.put(dm.barCodeKey(), barCode);
		
		ds.set(params);					
	}

	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}
	
	public static ArticleMetadata dm(){
		return new ArticleMetadata();
	}

	@Override
	public String barCode() throws IOException {
		return ds.get(dm.barCodeKey());
	}

	@Override
	public ArticlePlannings plannings() throws IOException {
		return new ArticlePlanningsImpl(this.base, this.id);
	}

	@Override
	public ArticleStocks stocks() throws IOException {
		return new ArticleStocksImpl(this.base, this.id);
	}

	@Override
	public boolean isPresent() {
		try {
			return base.domainsStore(dm).exists(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean isEqual(Article item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(Article item) {
		return !isEqual(item);
	}
}
