package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.ArticleCategory;

public class ArticleCategoryVm {
	
	private final transient ArticleCategory origin;
	
	public ArticleCategoryVm(){
		throw new UnsupportedOperationException("#ArticleCategory()");
	}
	
	public ArticleCategoryVm(final ArticleCategory origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public String getName() throws IOException {
		return origin.name();
	}
	
	@JsonGetter
	public UUID getMesureUnitId() throws IOException {
		return origin.mesureUnit().id();
	}
	
	@JsonGetter
	public String getMesureUnitShortName() throws IOException {
		return origin.mesureUnit().shortName();
	}
	
	@JsonGetter
	public String getMesureUnitFullName() throws IOException {
		return origin.mesureUnit().fullName();
	}
	
	@JsonGetter
	public int getNumberOfFamilies() throws IOException {
		return origin.families().size();
	}	
}
