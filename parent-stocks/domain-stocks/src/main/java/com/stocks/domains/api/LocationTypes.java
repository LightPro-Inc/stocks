package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;

public interface LocationTypes {
	LocationType get(String id) throws IOException;
	List<LocationType> all() throws IOException;
}
