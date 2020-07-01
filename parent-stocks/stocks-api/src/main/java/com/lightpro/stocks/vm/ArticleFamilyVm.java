package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.stocks.domains.api.ArticleFamily;

public final class ArticleFamilyVm {
	
	public final UUID id;
	public final String name;
	public final UUID categoryId;
	public final String category;
	public final String description;
	public final long numberOfArticles;
	public final String mesureUnitShortName;
	
	public ArticleFamilyVm(){
		throw new UnsupportedOperationException("#ArticleFamilyVm()");
	}
	
	public ArticleFamilyVm(final ArticleFamily origin) {
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.categoryId = origin.category().id();
	        this.category = origin.category().name();
	        this.mesureUnitShortName = origin.category().mesureUnit().shortName();
	        this.description = origin.description();
	        this.numberOfArticles = origin.articles().count();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }
}
