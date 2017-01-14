package com.stocks.domains.api;

import java.io.IOException;

public interface OperationCategory {
	String id();
	String name() throws IOException;
}
