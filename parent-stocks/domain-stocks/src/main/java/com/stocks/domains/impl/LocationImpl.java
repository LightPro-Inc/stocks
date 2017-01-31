package com.stocks.domains.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationMetadata;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.Warehouse;

public class LocationImpl implements Location {
	
	private final transient Base base;
	private final transient UUID id;
	private final transient LocationMetadata dm;
	private final transient DomainStore ds;
	
	public LocationImpl(final Base base, final UUID id){
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

	public static LocationMetadata dm(){
		return new LocationMetadata();
	}

	@Override
	public Warehouse warehouse() throws IOException {
		UUID warehouseId = ds.get(dm.warehouseIdKey());
		return new WarehouseImpl(base, warehouseId);
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
		params.put(dm.typeKey(), LocationTypeImpl.INTERNAL);
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
		String typeId = ds.get(dm.typeKey());
		return new LocationTypesImpl().get(typeId);
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public boolean isInternal() throws IOException {
		return warehouse().id() != null;
	}
	
	@Override
	public boolean isEqual(Location item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(Location item) {
		return !isEqual(item);
	}
}
