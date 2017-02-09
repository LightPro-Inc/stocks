package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
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

public class WarehouseImpl implements Warehouse {

	private final transient Base base;
	private final transient UUID id;
	private final transient WarehouseMetadata dm;
	private final transient DomainStore ds;
	
	public WarehouseImpl(final Base base, final UUID id){
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
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public String shortName() throws IOException {
		return ds.get(dm.shortNameKey());
	}

	public static WarehouseMetadata dm(){
		return new WarehouseMetadata();
	}

	@Override
	public void update(String name, String shortName) throws IOException {
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("Invalid internal shortName : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.shortNameKey(), shortName);

		ds.set(params);
	}

	@Override
	public Locations locations() {
		return new WarehouseLocationsImpl(this.base, this);
	}

	@Override
	public OperationType addOperationType(String name, Location defaultSourceLocation, Location defaultDestinationLocation, OperationCategory category, Sequence sequence) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (!defaultSourceLocation.isPresent()) {
            throw new IllegalArgumentException("Invalid default source location : it can't be empty!");
        }
		
		if (!defaultDestinationLocation.isPresent()) {
            throw new IllegalArgumentException("Invalid default destination location : it can't be empty!");
        }
		
		if (category == null) {
            throw new IllegalArgumentException("Invalid category : it can't be empty!");
        }
		
		if (!sequence.isPresent()) {
            throw new IllegalArgumentException("Invalid sequence : it can't be empty!");
        }
		
		OperationTypeMetadata dm = OperationTypeImpl.dm();
		DomainsStore ds = base.domainsStore(dm);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.defaultSourceLocationKey(), defaultSourceLocation.id());
		params.put(dm.defaultDestinationLocationKey(), defaultDestinationLocation.id());
		params.put(dm.warehouseIdKey(), this.id);
		params.put(dm.operationCategoryIdKey(), category.id());
		params.put(dm.sequenceIdKey(), sequence.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new OperationTypeImpl(this.base, id);
	}

	@Override
	public OperationTypes operationTypes() {
		return new WarehouseOperationTypes(this.base, this);
	}
	
	@Override
	public Location addLocation(String name, String shortName) throws IOException {
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("Invalid short name : it can't be empty!");
        }
		
		LocationMetadata dm = LocationImpl.dm();
		DomainsStore ds = base.domainsStore(dm);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.shortNameKey(), shortName);
		params.put(dm.warehouseIdKey(), this.id);
		params.put(dm.typeKey(), LocationType.INTERNAL.id());
		params.put(dm.moduleIdKey(), moduleStocks().id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new LocationImpl(this.base, id);	
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public List<ArticleStocks> stocks() throws IOException {
		List<ArticleStocks> stocks = new ArrayList<ArticleStocks>();
				
		for(Article article : moduleStocks().articles().all()) {
			stocks.add(new ArticleStocksImpl(this.base, article, this));
		}
		
		return stocks;
	}
	
	@Override
	public boolean isEqual(Warehouse item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(Warehouse item) {
		return !isEqual(item);
	}

	@Override
	public Stocks moduleStocks() throws IOException {
		UUID moduleId = ds.get(dm.moduleIdKey());
		return new StocksImpl(base, moduleId);
	}
}
