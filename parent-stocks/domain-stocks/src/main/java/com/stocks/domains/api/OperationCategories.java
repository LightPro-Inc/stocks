package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;

public interface OperationCategories {
	OperationCategory get(String id) throws IOException;
	List<OperationCategory> all() throws IOException;
}
