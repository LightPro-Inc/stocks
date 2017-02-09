package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Recordable;
import com.securities.api.Sequence;

public interface Warehouse extends Recordable<UUID, Warehouse> {
	String name() throws IOException;
	String shortName() throws IOException;
	
	void update(String name, String shortName) throws IOException;
	OperationType addOperationType(String name, Location defaultSourceLocation, Location defaultDestinationLocation, OperationCategory category, Sequence sequence) throws IOException;
	Location addLocation(String name, String shortName) throws IOException;
	Stocks moduleStocks() throws IOException;
	
	OperationTypes operationTypes();
	Locations locations();
	List<ArticleStocks> stocks() throws IOException;
}
