package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.ArticleStock;

public class ArticleStockVm {
	
	private final transient ArticleStock origin;
	
	public ArticleStockVm(){
		throw new UnsupportedOperationException("#ArticleStockVm()");
	}
	
	public ArticleStockVm(final ArticleStock origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public int getMaximumStock() throws IOException {
		return origin.planning().maximumStock();
	}
	
	@JsonGetter
	public int getMinimumStock() throws IOException {
		return origin.planning().minimumStock();
	}
	
	@JsonGetter
	public int getSafetyStock() throws IOException {
		return origin.planning().safetyStock();
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
	
	@JsonGetter
	public double getQuantity() throws IOException {
		return origin.quantity();
	}
	
	@JsonGetter
	public String getAlert() throws IOException {
		return origin.alert().toString();
	}
	
	@JsonGetter
	public int getAlertId() throws IOException {
		return origin.alert().id();
	}
}
