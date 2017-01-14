package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.ArticleFamily;

public class ArticleFamilyVm {
	
	private final transient ArticleFamily origin;
	
	public ArticleFamilyVm(){
		throw new UnsupportedOperationException("#ArticleFamilyVm()");
	}
	
	public ArticleFamilyVm(final ArticleFamily origin) {
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
	public UUID getCategoryId() throws IOException {
		return origin.category().id();
	}
	
	@JsonGetter
	public String getCategory() throws IOException {
		return origin.category().name();
	}
	
	@JsonGetter
	public String getDescription() throws IOException {
		return origin.description();
	}	
	
	@JsonGetter
	public int getNumberOfArticles() throws IOException {
		return origin.articles().size();
	}	
}
