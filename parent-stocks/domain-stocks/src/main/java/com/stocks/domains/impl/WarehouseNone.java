package com.stocks.domains.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.securities.api.Sequence;
import com.stocks.domains.api.ArticleStocks;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.OperationCategory;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;

public final class WarehouseNone extends EntityNone<Warehouse, UUID> implements Warehouse {

	@Override
	public String name() throws IOException {
		return "Aucun entrepôt";
	}

	@Override
	public String shortName() throws IOException {
		return null;
	}

	@Override
	public void update(String name, String shortName) throws IOException {

	}

	@Override
	public OperationType addOperationType(String name, Location defaultSourceLocation,
			Location defaultDestinationLocation, OperationCategory category, Sequence sequence,
			OperationType preparationOpType, OperationType returnOpType) throws IOException {
		
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Location addLocation(String name, String shortName) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Stocks moduleStocks() throws IOException {
		return new StocksNone();
	}

	@Override
	public OperationTypes operationTypes() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Locations locations() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public List<ArticleStocks> stocks() throws IOException {
		return Arrays.asList();
	}
}
