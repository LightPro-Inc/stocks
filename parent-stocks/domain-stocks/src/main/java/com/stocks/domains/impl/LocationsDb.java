package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.QueryableDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationMetadata;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.WarehouseMetadata;

public final class LocationsDb extends QueryableDb<Location, UUID, LocationMetadata> implements Locations {

	private final transient Stocks module;
	private final transient Warehouse warehouse;
	private final transient LocationType type;
	
	public LocationsDb(final Base base, final Stocks module, Warehouse warehouse, LocationType type){
		super(base, "Emplacement introuvable !");
		this.module = module;
		this.warehouse = warehouse;
		this.type = type;
	}
	
	private QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
					
		String statement = String.format("%s loc "
				+ "{clause-wh-loc} "
				+ "WHERE (loc.%s ILIKE ? OR loc.%s ILIKE ?) AND loc.%s=?",
				dm.domainName(),			
				dm.nameKey(), dm.shortNameKey(), dm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(warehouse.id() != null) {
			WarehouseMetadata whDm = new WarehouseMetadata();
			statement = statement.replace("{clause-wh-loc}", String.format("LEFT JOIN %s wh ON wh.%s=loc.%s", whDm.domainName(), whDm.keyName(), dm.warehouseIdKey()));
			
			switch (type) {
			case INTERNAL:
				statement = String.format("%s AND wh.%s=?", statement, whDm.keyName());
				params.add(warehouse.id());
				break;
			case NONE:
				statement = String.format("%s AND (wh.%s=? OR wh.%s IS NULL)", statement, whDm.keyName(), whDm.keyName());
				params.add(warehouse.id());
				break;
			default:
				statement = String.format("%s AND (wh.%s=? AND loc.%s=?)", statement, whDm.keyName(), dm.typeKey());
				params.add(warehouse.id());
				params.add(type.id());
				break;
			}		
		}else{
			statement = statement.replace("{clause-wh-loc}", "");
		}		
		
		if(type != LocationType.NONE) {
			statement = String.format("%s AND loc.%s=?", statement, dm.typeKey());
			params.add(type.id());
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY loc.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("loc.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}
	
	@Override
	public List<Location> all() throws IOException {
		return buildQuery(StringUtils.EMPTY).find()
				.stream()
				.map(m -> newOne(UUIDConvert.fromObject(m)))
				.collect(Collectors.toList());				
	}

	@Override
	public void deleteAll() throws IOException {
		for (Location location : all()) {
			delete(location);
		}
	}

	@Override
	public Location addVirtual(String name, String shortName, LocationType type) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (StringUtils.isBlank(shortName)) {
            throw new IllegalArgumentException("Invalid short name : it can't be empty!");
        }
				
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.shortNameKey(), shortName);
		params.put(dm.warehouseIdKey(), null);
		params.put(dm.typeKey(), type.id());
		params.put(dm.moduleIdKey(), module.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return newOne(id);
	}

	@Override
	protected Location newOne(UUID id) {
		return new LocationDb(base, id, module);
	}

	@Override
	public Location none() {
		return new LocationNone();
	}

	@Override
	public Locations of(Warehouse warehouse) {
		return new LocationsDb(base, module, warehouse, type);
	}

	@Override
	public Locations of(LocationType type) {
		return new LocationsDb(base, module, warehouse, type);
	}
}
