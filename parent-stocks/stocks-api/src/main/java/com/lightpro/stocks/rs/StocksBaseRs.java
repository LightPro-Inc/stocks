package com.lightpro.stocks.rs;

import java.io.IOException;

import com.securities.api.BaseRs;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.impl.StocksImpl;

public class StocksBaseRs extends BaseRs {
	
	protected Stocks stocks() throws IOException {
		Module module = currentCompany().modules().get(ModuleType.STOCKS);
		return new StocksImpl(base(), module.id());
	}
}
