package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationMetadata;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.WarehouseMetadata;

public class WarehouseImpl implements Warehouse {

	private final transient Base base;
	private final transient Object id;
	private final transient WarehouseMetadata dm;
	private final transient DomainStore ds;
	
	public WarehouseImpl(final Base base, final Object id){
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
		return new WarehouseLocationsImpl(this.base, this.id);
	}

	@Override
	public OperationType addOperationType(String name, UUID defaultSourceLocationId, UUID defaultDestinationLocationId, String categoryId, UUID sequenceId) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (defaultSourceLocationId == null) {
            throw new IllegalArgumentException("Invalid default source location : it can't be empty!");
        }
		
		if (defaultDestinationLocationId == null) {
            throw new IllegalArgumentException("Invalid default destination location : it can't be empty!");
        }
		
		if (categoryId == null || categoryId.isEmpty()) {
            throw new IllegalArgumentException("Invalid category : it can't be empty!");
        }
		
		if (sequenceId == null) {
            throw new IllegalArgumentException("Invalid sequence : it can't be empty!");
        }
		
		OperationTypeMetadata dm = OperationTypeImpl.dm();
		DomainsStore ds = base.domainsStore(dm);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.defaultSourceLocationKey(), defaultSourceLocationId);
		params.put(dm.defaultDestinationLocationKey(), defaultDestinationLocationId);
		params.put(dm.warehouseIdKey(), this.id);
		params.put(dm.operationCategoryIdKey(), categoryId);
		params.put(dm.sequenceIdKey(), sequenceId);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new OperationTypeImpl(this.base, id);
	}

	@Override
	public OperationTypes operationTypes() {
		return new WarehouseOperationTypes(this.base, this.id);
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
		params.put(dm.typeKey(), LocationTypeImpl.INTERNAL);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new LocationImpl(this.base, id);	
	}

	@Override
	public boolean isPresent() throws IOException {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public List<ArticleStocks> stocks() throws IOException {
		List<ArticleStocks> stocks = new ArrayList<ArticleStocks>();
				
		for(Article article : new StocksImpl(this.base).articles().all()) {
			stocks.add(new ArticleStocksImpl(this.base, article.id()));
		}
		
		return stocks;
	}
}
