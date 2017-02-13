package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationMetadata;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.Stocks;

public class LocationsImpl implements Locations {

	private final transient Base base;
	private final transient LocationMetadata dm;
	private final transient DomainsStore ds;
	private final transient Stocks module;
	
	public LocationsImpl(final Base base, final Stocks module){
		this.base = base;
		this.dm = LocationImpl.dm();
		this.ds = base.domainsStore(dm);
		this.module = module;
	}
	
	@Override
	public List<Location> all() throws IOException {
		List<Location> values = new ArrayList<Location>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		LocationMetadata locationdm = LocationImpl.dm();
		String statement = String.format("SELECT %s FROM %s "
				+ "WHERE %s=? "
				+ "ORDER BY %s ASC", 
				locationdm.keyName(), locationdm.domainName(), 
				locationdm.moduleIdKey(), 
				hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(module.id());
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new LocationImpl(this.base, UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;		
	}
	
	@Override
	public List<Location> internals() throws IOException {
		List<Location> all = all();		
		List<Location> internals = new ArrayList<Location>();
		
		for (Location lt : all) {
			if(lt.isInternal())
				internals.add(lt);
		}
		
		return internals;
	}

	@Override
	public Location get(UUID id) throws IOException {
		Location item = getOrDefault(id);
		
		if(item == null)
			throw new NotFoundException("L'emplacement n'a pas été trouvé !");
		
		return item; 
	}

	@Override
	public Location getOrDefault(UUID id) throws IOException {
		Location item = new LocationImpl(this.base, id);
		
		if(!item.isPresent() || item.module().isNotEqual(module))
			return null;
		
		return item;
	}

	@Override
	public void delete(Location item) throws IOException {
		
		if(item.isPresent() && item.module().isEqual(module))
			ds.delete(item.id());
	}

	@Override
	public void deleteAll() throws IOException {
		for (Location location : all()) {
			delete(location);
		}
	}
}
