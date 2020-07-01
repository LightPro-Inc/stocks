package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationMetadata;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;

public final class LocationDb extends GuidKeyEntityDb<Location, LocationMetadata> implements Location {
	
	private final Stocks module;
	
	public LocationDb(final Base base, final UUID id, final Stocks module){
		super(base, id, "Emplacement introuvable !");
		this.module = module;
	}

	@Override
	public String name() throws IOException {
		return this.ds.get(dm.nameKey());
	}

	@Override
	public boolean active() throws IOException {
		return this.ds.get(dm.activeKey());
	}

	@Override
	public void activate(boolean activate) throws IOException {		
		ds.set(dm.activeKey(), activate);
	}

	@Override
	public Warehouse warehouse() throws IOException {
		UUID warehouseId = ds.get(dm.warehouseIdKey());
		return module().warehouses().build(warehouseId);
	}

	@Override
	public void update(String name, String shortName) throws IOException {
		
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("Invalid shortName : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.typeKey(), LocationType.INTERNAL.id());
		params.put(dm.shortNameKey(), shortName);
		params.put(dm.nameKey(), name);
		
		ds.set(params);	
	}

	@Override
	public String shortName() throws IOException {
		return ds.get(dm.shortNameKey());
	}

	@Override
	public LocationType type() throws IOException {
		int typeId = ds.get(dm.typeKey());
		return LocationType.get(typeId);
	}

	@Override
	public boolean isInternal() throws IOException {
		return warehouse().id() != null;
	}

	@Override
	public Stocks module() throws IOException {
		return module;
	}
}
