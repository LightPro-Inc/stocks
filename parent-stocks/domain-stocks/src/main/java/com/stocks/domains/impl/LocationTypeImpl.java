package com.stocks.domains.impl;

import com.stocks.domains.api.LocationType;

public class LocationTypeImpl implements LocationType {

	public final static String  INTERNAL = "INTERNAL";
	public final static String FOURNISSEUR = "FOURNISSEUR";
	public final static String CLIENT = "CLIENT";
	public final static String PERTE_INVENTAIRE = "PERTEINV";
	public final static String APPRO = "APPRO";
	public final static String PROD = "PROD";
	public final static String TRANSIT = "TRANSIT";
	public final static String RETOUR = "RETOUR";
	public final static String REBUT = "REBUT";
	
	private final transient String id;
	private final transient String name;
	
	public LocationTypeImpl(final String id, final String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}
}
