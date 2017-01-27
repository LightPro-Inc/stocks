package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface Warehouse extends Recordable<UUID, Warehouse> {
	String name() throws IOException;
	String shortName() throws IOException;
	
	void update(String name, String shortName) throws IOException;
	OperationType addOperationType(String name, UUID defaultSourceLocationId, UUID defaultDestinationLocationId, String categoryId, UUID sequenceId) throws IOException;
	Location addLocation(String name, String shortName) throws IOException;
	
	OperationTypes operationTypes();
	Locations locations();
	List<ArticleStocks> stocks() throws IOException;
}
