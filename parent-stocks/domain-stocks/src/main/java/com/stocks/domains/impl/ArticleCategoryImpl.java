package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.Horodate;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.securities.api.MesureUnit;
import com.securities.impl.MesureUnitImpl;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.ArticleCategory;
import com.stocks.domains.api.ArticleCategoryMetadata;

public class ArticleCategoryImpl implements ArticleCategory {

	private final transient Base base;
	private final transient Object id;
	private final transient ArticleCategoryMetadata dm;
	private final transient DomainStore ds;
	
	public ArticleCategoryImpl(final Base base, final Object id) {
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
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public MesureUnit mesureUnit() throws IOException {
		Object mesureUnitId = ds.get(dm.mesureUnitIdKey());
		return new MesureUnitImpl(this.base, mesureUnitId);
	}

	@Override
	public void update(String name, UUID mesureUnitId) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (mesureUnitId == null) {
            throw new IllegalArgumentException("Invalid mesure unit : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.mesureUnitIdKey(), mesureUnitId);
		
		ds.set(params);		
	}

	@Override
	public List<ArticleFamily> families() throws IOException {
		
		List<ArticleFamily> values = new ArrayList<ArticleFamily>();
			
		ArticleFamilyMetadata articleFamilydm = ArticleFamilyImpl.dm();
		HorodateMetadata hm = HorodateImpl.dm();
		String statement = String.format("SELECT %s FROM %s WHERE %s = ? ORDER BY %s DESC", articleFamilydm.keyName(), articleFamilydm.domainName(), articleFamilydm.categoryIdKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(this.id);
		
		List<DomainStore> results = base.domainsStore(dm).findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new ArticleFamilyImpl(this.base, domainStore.key())); 
		}		
		
		return values;
	}

	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}
	
	public static ArticleCategoryMetadata dm(){
		return new ArticleCategoryMetadata();
	}

	@Override
	public boolean isPresent() throws IOException {
		return base.domainsStore(dm).exists(id);
	}
	
	@Override
	public boolean isEqual(ArticleCategory item) throws IOException {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(ArticleCategory item) throws IOException {
		return !isEqual(item);
	}
}
