package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.securities.api.Sequence;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationMetadata;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.OperationCategory;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.WarehouseMetadata;

public final class WarehouseDb extends GuidKeyEntityDb<Warehouse, WarehouseMetadata> implements Warehouse {
	
	private final Stocks module;
	
	public WarehouseDb(final Base base, final UUID id, Stocks module){
		super(base, id, "Entrepôt introuvable !");
		this.module = module;
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public String shortName() throws IOException {
		return ds.get(dm.shortNameKey());
	}

	@Override
	public void update(String name, String shortName) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (StringUtils.isBlank(shortName)) {
            throw new IllegalArgumentException("Invalid internal shortName : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.shortNameKey(), shortName);

		ds.set(params);
	}

	@Override
	public Locations locations() throws IOException {
		return moduleStocks().locations().of(this);
	}

	@Override
	public OperationType addOperationType(String name, Location defaultSourceLocation, Location defaultDestinationLocation, OperationCategory category, Sequence sequence, OperationType preparationOpType, OperationType returnOpType) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (defaultSourceLocation.id() == null) {
            throw new IllegalArgumentException("Invalid default source location : it can't be empty!");
        }
		
		if (defaultDestinationLocation.id() == null) {
            throw new IllegalArgumentException("Invalid default destination location : it can't be empty!");
        }
		
		if (category == OperationCategory.NONE) {
            throw new IllegalArgumentException("Invalid category : it can't be empty!");
        }
		
		if (sequence.id() == null) {
            throw new IllegalArgumentException("Invalid sequence : it can't be empty!");
        }
		
		OperationTypeMetadata dm = new OperationTypeMetadata();
		DomainsStore ds = base.domainsStore(dm);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.defaultSourceLocationKey(), defaultSourceLocation.id());
		params.put(dm.defaultDestinationLocationKey(), defaultDestinationLocation.id());
		params.put(dm.warehouseIdKey(), this.id);
		params.put(dm.operationCategoryIdKey(), category.id());
		params.put(dm.sequenceIdKey(), sequence.id());
		params.put(dm.preparationOpTypeIdKey(), preparationOpType.id());
		params.put(dm.returnOpTypeIdKey(), returnOpType.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new OperationTypeDb(this.base, id, module);
	}

	@Override
	public OperationTypes operationTypes() throws IOException {
		return moduleStocks().operationTypes().of(this);		
	}
	
	@Override
	public Location addLocation(String name, String shortName) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (StringUtils.isBlank(shortName)) {
            throw new IllegalArgumentException("Invalid short name : it can't be empty!");
        }
		
		LocationMetadata dm = new LocationMetadata();
		DomainsStore ds = base.domainsStore(dm);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.shortNameKey(), shortName);
		params.put(dm.warehouseIdKey(), this.id);
		params.put(dm.typeKey(), LocationType.INTERNAL.id());
		params.put(dm.moduleIdKey(), moduleStocks().id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new LocationDb(this.base, id, module);	
	}

	@Override
	public List<ArticleStocks> stocks() throws IOException {
		List<ArticleStocks> stocks = new ArrayList<ArticleStocks>();
				
		for(Article article : moduleStocks().articles().all()) {
			stocks.add(new ArticleStocksDb(this.base, article, this, module));
		}
		
		return stocks;
	}

	@Override
	public Stocks moduleStocks() throws IOException {
		return module;
	}
}
