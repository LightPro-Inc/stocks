package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;

public final class LocationNone extends EntityNone<Location, UUID> implements Location {

	@Override
	public LocationType type() throws IOException {
		return LocationType.NONE;
	}

	@Override
	public String name() throws IOException {
		return "Aucun emplacement";
	}

	@Override
	public String shortName() throws IOException {
		return null;
	}

	@Override
	public boolean active() throws IOException {
		return false;
	}

	@Override
	public boolean isInternal() throws IOException {
		return false;
	}

	@Override
	public Warehouse warehouse() throws IOException {
		return null;
	}

	@Override
	public Stocks module() throws IOException {
		return null;
	}

	@Override
	public void activate(boolean activate) throws IOException {

	}

	@Override
	public void update(String name, String shortName) throws IOException {

	}
}
