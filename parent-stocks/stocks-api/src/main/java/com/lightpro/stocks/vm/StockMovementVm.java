package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.stocks.domains.api.StockMovement;

public final class StockMovementVm {
	
	public final UUID id;
	public final String reference;
	public final String documentSource;
	public final String sourceLocation;
	public final UUID sourceLocationId;
	public final String destinationLocation;
	public final UUID destinationLocationId;
	public final String article;
	public final UUID articleId;
	public final double quantity;
	public final Date movementDate;
	
	public StockMovementVm(){
		throw new UnsupportedOperationException("#StockMovementVm()");
	}
	
	public StockMovementVm(final StockMovement origin) {
		try {
			this.id = origin.id();
			this.reference = origin.operation().reference();
			this.documentSource = origin.operation().documentSource();        
	        this.sourceLocation = origin.operation().sourceLocation().name();
	        this.sourceLocationId = origin.operation().sourceLocation().id();
	        this.destinationLocation = origin.operation().destinationLocation().name();
			this.destinationLocationId = origin.operation().destinationLocation().id();
	        this.article = origin.article().name();
	        this.articleId = origin.article().id();
	        this.quantity = origin.quantity();
	        this.movementDate = origin.operation().movementDate();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
