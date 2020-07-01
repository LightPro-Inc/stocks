package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.stocks.domains.api.ArticleStock;

public final class ArticleStockVm {
	
	public final UUID id;
	public final double maximumStock;
	public final double minimumStock;
	public final double safetyStock;
	public final UUID articleId;
	public final String article;
	public final UUID locationId;
	public final String location;
	public final double quantity;
	public final String alert;
	public final int alertId;
	
	public ArticleStockVm(){
		throw new UnsupportedOperationException("#ArticleStockVm()");
	}
	
	public ArticleStockVm(final ArticleStock origin) {
		try {
			this.id = origin.id();
			this.maximumStock = origin.planning().maximumStock();
			this.minimumStock = origin.planning().minimumStock();
	        this.safetyStock = origin.planning().safetyStock();
	        this.articleId = origin.article().id();
	        this.article = origin.article().name();
	        this.locationId = origin.location().id();
			this.location = origin.location().name();
	        this.quantity = origin.quantity();
	        this.alert = origin.alert().toString();
	        this.alertId = origin.alert().id();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
