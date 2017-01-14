package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.LocationTypes;

public class LocationTypesImpl implements LocationTypes {	
	
	public LocationTypesImpl(){
		
	}
	
	@Override
	public LocationType get(String id) throws IOException {
		List<LocationType> items = all();
		
		for (LocationType lt : items) {
			
			if(lt.id().equals(id))
				return lt;
		}
		
		return null;
	}

	@Override
	public List<LocationType> all() throws IOException {
		
		List<LocationType> items = new ArrayList<LocationType>();
		
		items.add(new LocationTypeImpl(LocationTypeImpl.INTERNAL   		, "Emplacement interne"));
		items.add(new LocationTypeImpl(LocationTypeImpl.FOURNISSEUR		, "Emplacement fournisseur"));
		items.add(new LocationTypeImpl(LocationTypeImpl.CLIENT     		, "Emplacement client"));		
		items.add(new LocationTypeImpl(LocationTypeImpl.PERTE_INVENTAIRE , "Perte d'inventaire"));
		items.add(new LocationTypeImpl(LocationTypeImpl.APPRO      		, "Approvisionnement"));
		items.add(new LocationTypeImpl(LocationTypeImpl.PROD       		, "Production"));
		items.add(new LocationTypeImpl(LocationTypeImpl.TRANSIT    		, "Emplacement de transit"));
		items.add(new LocationTypeImpl(LocationTypeImpl.RETOUR     		, "Retour"));
		items.add(new LocationTypeImpl(LocationTypeImpl.REBUT      		, "Rebut"));
		
		return items;
	}
}
