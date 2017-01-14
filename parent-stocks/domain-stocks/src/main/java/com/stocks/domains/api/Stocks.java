package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;

public interface Stocks {	
    ArticleCategories articleCategories();
    ArticleFamilies articleFamilies();
    Articles articles();
    Warehouses warehouses();
    LocationTypes locationTypes();
    Locations locations();
    OperationTypes operationTypes();
    OperationCategories operationCategories();
    List<ArticleStocks> stocks() throws IOException;
    StockMovements stockMovements() throws IOException;
    Operations operations() throws IOException;
}
