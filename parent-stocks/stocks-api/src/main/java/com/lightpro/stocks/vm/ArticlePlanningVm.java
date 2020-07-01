package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.stocks.domains.api.ArticlePlanning;

public final class ArticlePlanningVm {
	
	public final UUID id;
	public final double maximumStock;
	public final double minimumStock;
	public final double safetyStock;
	public final UUID articleId;
	public final String article;
	public final UUID locationId;
	public final String location;
	
	public ArticlePlanningVm(){
		throw new UnsupportedOperationException("#ArticlePlanningVm()");
	}
	
	public ArticlePlanningVm(final ArticlePlanning origin) {
		try {
			this.id = origin.id();
			this.maximumStock = origin.maximumStock();
			this.minimumStock = origin.minimumStock();
	        this.safetyStock = origin.safetyStock();
	        this.articleId = origin.article().id();
	        this.article = origin.article().name();
	        this.locationId = origin.location().id();
	        this.location = origin.location().name();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }
}
