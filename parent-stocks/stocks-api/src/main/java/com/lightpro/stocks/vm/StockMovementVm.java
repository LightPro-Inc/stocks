package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.StockMovement;

public class StockMovementVm {
	private final transient StockMovement origin;
	
	public StockMovementVm(){
		throw new UnsupportedOperationException("#StockMovementVm()");
	}
	
	public StockMovementVm(final StockMovement origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public String getReference() throws IOException {
		return origin.operation().reference();
	}
	
	@JsonGetter
	public String getDocumentSource() throws IOException {
		return origin.operation().documentSource();
	}	
	
	@JsonGetter
	public String getSourceLocation() throws IOException {
		return origin.operation().sourceLocation().name();
	}	
	
	@JsonGetter
	public UUID getSourceLocationId() throws IOException {
		return origin.operation().sourceLocation().id();
	}	
	
	@JsonGetter
	public String getDestinationLocation() throws IOException {
		return origin.operation().destinationLocation().name();
	}	
	
	@JsonGetter
	public UUID getDestinationLocationId() throws IOException {
		return origin.operation().destinationLocation().id();
	}
	
	@JsonGetter
	public String getArticle() throws IOException {
		return origin.article().name();
	}
	
	@JsonGetter
	public UUID getArticleId() throws IOException {
		return origin.article().id();
	}
	
	@JsonGetter
	public int getQuantity() throws IOException {
		return origin.quantity();
	}
	
	@JsonGetter
	public Date getMovementDate() throws IOException {
		return origin.operation().movementDate();
	}
}
