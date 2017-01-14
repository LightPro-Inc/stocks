package com.lightpro.stocks.rs;

import com.infrastructure.core.BaseRs;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.impl.StocksImpl;

public class StocksBaseRs extends BaseRs {
	protected Stocks stocks(){
		return new StocksImpl(base);
	}
}
