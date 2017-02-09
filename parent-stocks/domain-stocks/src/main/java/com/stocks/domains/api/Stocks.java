package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;

import com.securities.api.Module;
import com.securities.api.Sequences;

public interface Stocks extends Module {	
    ArticleCategories articleCategories();
    AllArticleFamilies families();
    AllArticles articles();
    Locations locations();
    OperationTypes operationTypes();
    Warehouses warehouses();
    List<ArticleStocks> stocks(final Warehouse warehouse) throws IOException;
    StockMovements stockMovements() throws IOException;
    Operations operations() throws IOException;
    Sequences sequences() throws IOException;
}
