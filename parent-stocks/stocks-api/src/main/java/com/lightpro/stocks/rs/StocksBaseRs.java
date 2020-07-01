package com.lightpro.stocks.rs;

import java.io.IOException;

import com.securities.api.BaseRs;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.impl.StocksDb;

public class StocksBaseRs extends BaseRs {
	
	public StocksBaseRs() {
		super(ModuleType.STOCKS);
	}

	protected Stocks stocks() throws IOException {
		return stocks(currentModule);
	}
	
	protected Stocks stocks(Module module) throws IOException {
		return new StocksDb(base, module);
	}
}
