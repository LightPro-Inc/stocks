package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




/**
 * Les emplacements d'un entrepôt
 * @author oob
 */
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

public class WarehouseLocationsImpl implements Locations {

	private final transient Base base;
	private final transient UUID warehouseId;
	private final transient LocationMetadata dm;
	private final transient DomainsStore ds;
	
	public WarehouseLocationsImpl(final Base base, Object warehouseId){
		this.base = base;
		this.dm = LocationImpl.dm();
		this.ds = base.domainsStore(dm);
		this.warehouseId = UUIDConvert.fromObject(warehouseId);
	}
	
	@Override
	public List<Location> all() throws IOException {
		List<Location> values = new ArrayList<Location>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		LocationMetadata locationdm = LocationImpl.dm();
		String statement = String.format("SELECT %s FROM %s WHERE %s = ? ORDER BY %s ASC", locationdm.keyName(), locationdm.domainName(), locationdm.warehouseIdKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(this.warehouseId);
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(new LocationImpl(this.base, UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;			
	}
	
	@Override
	public List<Location> internals() throws IOException {
		return all();		
	}

	@Override
	public Location get(UUID id) throws IOException {
		Location item = getOrDefault(id);
		if(item == null)
			throw new NotFoundException("L'emplacement n'a pas été trouvé !");
		
		return new LocationImpl(this.base, id);
	}	

	@Override
	public void delete(Location location) throws IOException {
		
		if(location.warehouse().id().equals(warehouseId))					
			ds.delete(location.id());
		else
			throw new NotFoundException("L'emplacement n'a pas été trouvé");
	}

	@Override
	public Location getOrDefault(UUID id) throws IOException {
		
		if(!ds.exists(id))
			return null;
		
		Location item = new LocationImpl(this.base, id);
		
		if(item.warehouse().id().equals(warehouseId))
			return item;		
		else
			return null;
	}
}
