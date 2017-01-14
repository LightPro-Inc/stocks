package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.stocks.domains.api.Article;

public class ArticleVm {
	
	private final transient Article origin;
	
	public ArticleVm(){
		throw new UnsupportedOperationException("#ArticleVm()");
	}
	
	public ArticleVm(final Article origin) {
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
	public String getInternalReference() throws IOException {
		return origin.internalReference();
	}
	
	@JsonGetter
	public double getCost() throws IOException {
		return origin.cost();
	}
	
	@JsonGetter
	public UUID getFamilyId() throws IOException {
		return origin.family().id();
	}
	
	@JsonGetter
	public String getFamily() throws IOException {
		return origin.family().name();
	}
	
	@JsonGetter
	public UUID getCategoryId() throws IOException {
		return origin.family().category().id();
	}
	
	@JsonGetter
	public String getCategory() throws IOException {
		return origin.family().category().name();
	}
	
	@JsonGetter
	public String getDescription() throws IOException {
		return origin.description();
	}
	
	@JsonGetter
	public UUID getMesureUnitId() throws IOException {
		return origin.family().category().mesureUnit().id();
	}
	
	@JsonGetter
	public String getMesureUnitShortName() throws IOException {
		return origin.family().category().mesureUnit().shortName();
	}
	
	@JsonGetter
	public String getMesureUnitFullName() throws IOException {
		return origin.family().category().mesureUnit().fullName();
	}
	
	@JsonGetter
	public int getQuantity() throws IOException {
		return origin.quantity();
	}
	
	@JsonGetter
	public String getBarCode() throws IOException {
		return origin.barCode();
	}
}
