package com.stocks.domains.api;

import java.io.IOException;

public interface ArticlesByFamily extends Articles {
	Article add(String name, String internalReference, String barCode, int quantity, double cost, String description) throws IOException;
}
