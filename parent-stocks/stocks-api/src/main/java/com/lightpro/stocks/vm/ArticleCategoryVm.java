package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.stocks.domains.api.ArticleCategory;

public final class ArticleCategoryVm {
	
	public final UUID id;
	public final String name;
	public final UUID mesureUnitId;
	public final String mesureUnitShortName;
	public final String mesureUnitFullName;
	public final long numberOfFamilies;
	
	public ArticleCategoryVm(){
		throw new UnsupportedOperationException("#ArticleCategory()");
	}
	
	public ArticleCategoryVm(final ArticleCategory origin) {
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.mesureUnitId = origin.mesureUnit().id();
	        this.mesureUnitShortName = origin.mesureUnit().shortName();
	        this.mesureUnitFullName = origin.mesureUnit().fullName();
	        this.numberOfFamilies = origin.families().count();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }	
}
