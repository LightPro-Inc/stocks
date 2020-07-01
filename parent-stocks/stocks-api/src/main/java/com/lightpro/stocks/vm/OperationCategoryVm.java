package com.lightpro.stocks.vm;

import com.stocks.domains.api.OperationCategory;

public final class OperationCategoryVm {

	public final int id;
	public final String name;	
	
	public OperationCategoryVm(){
		throw new UnsupportedOperationException("#OperationCategoryVm()");
	}
	
	public OperationCategoryVm(final OperationCategory origin) {
        this.id = origin.id();
        this.name = origin.toString();
    }
}
