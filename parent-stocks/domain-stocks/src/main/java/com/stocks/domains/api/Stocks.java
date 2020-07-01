package com.stocks.domains.api;

import java.io.IOException;

import com.securities.api.Contacts;
import com.securities.api.MesureUnits;
import com.securities.api.Module;
import com.securities.api.Sequences;

public interface Stocks extends Module {	
    ArticleCategories articleCategories();
    ArticleFamilies articleFamilies();
    Articles articles();
    Locations locations();
    OperationTypes operationTypes();
    Warehouses warehouses();
    StockMovements stockMovements() throws IOException;
    Operations operations() throws IOException;
    Sequences sequences() throws IOException;
    MesureUnits mesureUnits() throws IOException;
    Contacts contacts() throws IOException;
}
