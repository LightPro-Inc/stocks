package com.stocks.domains.impl;

import com.stocks.domains.api.OperationCategory;

public class OperationCategoryImpl implements OperationCategory {

	public final static String FOURNISSEUR = "FOURNISSEUR";
	public final static String INTERNAL = "INTERNAL";
	public final static String CLIENT = "CLIENT";
	
	private final transient String id;
	private final transient String name;
	
	public OperationCategoryImpl(final String id, final String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}
}
