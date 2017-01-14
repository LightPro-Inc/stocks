package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationMetadata;
import com.stocks.domains.api.Locations;

public class LocationsImpl implements Locations {

	private final transient Base base;
	private final transient LocationMetadata dm;
	private final transient DomainsStore ds;
	
	public LocationsImpl(final Base base){
		this.base = base;
		this.dm = LocationImpl.dm();
		this.ds = base.domainsStore(dm);
	}
	
	@Override
	public List<Location> all() throws IOException {
		List<Location> values = new ArrayList<Location>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		LocationMetadata locationdm = LocationImpl.dm();
		String statement = String.format("SELECT %s FROM %s ORDER BY %s ASC", locationdm.keyName(), locationdm.domainName(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new LocationImpl(this.base, domainStore.key())); 
		}		
		
		return values;		
	}
	
	@Override
	public List<Location> internals() throws IOException {
		List<Location> all = all();		
		List<Location> internals = new ArrayList<Location>();
		
		for (Location lt : all) {
			if(!(lt.warehouse().id() == null))
				internals.add(lt);
		}
		
		return internals;
	}

	@Override
	public Location get(UUID id) throws IOException {
		Location item = getOrDefault(id);
		if(item == null)
			throw new NotFoundException("L'emplacement n'a pas été trouvé !");
		
		return new LocationImpl(this.base, id);
	}

	@Override
	public Location getOrDefault(UUID id) throws IOException {
		if(!ds.exists(id))
			return null;
		
		return new LocationImpl(this.base, id);
	}

	@Override
	public void delete(Location location) throws IOException {
		ds.delete(location.id());
	}
}
