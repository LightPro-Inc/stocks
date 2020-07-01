package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.securities.api.Sequence;

public interface Warehouse extends Nonable {
	UUID id();
	String name() throws IOException;
	String shortName() throws IOException;
	
	void update(String name, String shortName) throws IOException;
	OperationType addOperationType(String name, Location defaultSourceLocation, Location defaultDestinationLocation, OperationCategory category, Sequence sequence, OperationType preparationOpType, OperationType returnOpType) throws IOException;
	Location addLocation(String name, String shortName) throws IOException;
	Stocks moduleStocks() throws IOException;
	
	OperationTypes operationTypes() throws IOException;
	Locations locations()  throws IOException;
	List<ArticleStocks> stocks() throws IOException;
}
