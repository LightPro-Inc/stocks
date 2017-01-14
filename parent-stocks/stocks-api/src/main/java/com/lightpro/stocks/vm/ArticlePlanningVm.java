package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.ArticlePlanning;

public class ArticlePlanningVm {
	
	private final transient ArticlePlanning origin;
	
	public ArticlePlanningVm(){
		throw new UnsupportedOperationException("#ArticlePlanningVm()");
	}
	
	public ArticlePlanningVm(final ArticlePlanning origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public int getMaximumStock() throws IOException {
		return origin.maximumStock();
	}
	
	@JsonGetter
	public int getMinimumStock() throws IOException {
		return origin.minimumStock();
	}
	
	@JsonGetter
	public int getSafetyStock() throws IOException {
		return origin.safetyStock();
	}
	
	@JsonGetter
	public UUID getArticleId() throws IOException {
		return origin.article().id();
	}
	
	@JsonGetter
	public String getArticle() throws IOException {
		return origin.article().name();
	}
	
	@JsonGetter
	public UUID getLocationId() throws IOException {
		return origin.location().id();
	}
	
	@JsonGetter
	public String getLocation() throws IOException {
		return origin.location().name();
	}	
}
