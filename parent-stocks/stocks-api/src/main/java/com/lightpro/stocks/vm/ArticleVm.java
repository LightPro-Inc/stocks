package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.stocks.domains.api.Article;

public final class ArticleVm {
	
	public final UUID id;
	public final String name;
	public final String internalReference;
	public final double cost;
	public final UUID familyId;
	public final String family;
	public final UUID categoryId;
	public final String category;
	public final String description;
	public final UUID mesureUnitId;
	public final String mesureUnitShortName;
	public final String mesureUnitFullName;
	public final double quantity;
	public final String barCode;
	public final String emballage;
	
	public ArticleVm(){
		throw new UnsupportedOperationException("#ArticleVm()");
	}
	
	public ArticleVm(final Article origin) {
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.internalReference = origin.internalReference();
	        this.cost = origin.cost();
	        this.familyId = origin.family().id();
	        this.family = origin.family().name();
	        this.categoryId = origin.family().category().id();
			this.category = origin.family().category().name();
	        this.description = origin.description();
	        this.mesureUnitId = origin.family().category().mesureUnit().id();
	        this.mesureUnitShortName = origin.family().category().mesureUnit().shortName();
	        this.mesureUnitFullName = origin.family().category().mesureUnit().fullName();
	        this.quantity = origin.quantity();
	        this.barCode = origin.barCode();
	        this.emballage = origin.emballage();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
