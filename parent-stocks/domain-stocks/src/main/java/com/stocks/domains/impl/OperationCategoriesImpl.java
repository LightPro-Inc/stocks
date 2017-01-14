package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.stocks.domains.api.OperationCategories;
import com.stocks.domains.api.OperationCategory;

public class OperationCategoriesImpl implements OperationCategories {

	public OperationCategoriesImpl(){
		
	}
	
	@Override
	public OperationCategory get(String id) throws IOException {
		List<OperationCategory> items = all();
		
		for (OperationCategory lt : items) {
			
			if(lt.id().equals(id))
				return lt;
		}
		
		return null;
	}

	@Override
	public List<OperationCategory> all() throws IOException {
		
		List<OperationCategory> items = new ArrayList<OperationCategory>();
		
		items.add(new OperationCategoryImpl(LocationTypeImpl.FOURNISSEUR		, "Fournisseurs"));
		items.add(new OperationCategoryImpl(LocationTypeImpl.INTERNAL   		, "Interne"));		
		items.add(new OperationCategoryImpl(LocationTypeImpl.CLIENT     		, "Clients"));		
		
		return items;
	}
}
