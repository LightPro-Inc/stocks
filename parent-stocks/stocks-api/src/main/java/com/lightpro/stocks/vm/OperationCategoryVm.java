package com.lightpro.stocks.vm;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.OperationCategory;

public class OperationCategoryVm {

	private final transient OperationCategory origin;
	
	public OperationCategoryVm(){
		throw new UnsupportedOperationException("#OperationCategoryVm()");
	}
	
	public OperationCategoryVm(final OperationCategory origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public int getId(){
		return origin.id();
	}
	
	@JsonGetter
	public String getName() throws IOException {
		return origin.toString();
	}	
}
