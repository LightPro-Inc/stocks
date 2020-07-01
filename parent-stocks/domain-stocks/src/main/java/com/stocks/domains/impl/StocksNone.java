package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.securities.api.Company;
import com.securities.api.Contacts;
import com.securities.api.Feature;
import com.securities.api.FeatureSubscribed;
import com.securities.api.Features;
import com.securities.api.Indicators;
import com.securities.api.Log;
import com.securities.api.MesureUnits;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.Sequences;
import com.securities.impl.CompanyNone;
import com.stocks.domains.api.ArticleCategories;
import com.stocks.domains.api.ArticleFamilies;
import com.stocks.domains.api.Articles;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.StockMovements;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouses;

public final class StocksNone extends EntityNone<Stocks, UUID> implements Stocks {

	@Override
	public void activate(boolean arg0) throws IOException {

	}

	@Override
	public Company company() throws IOException {
		return new CompanyNone();
	}

	@Override
	public String description() throws IOException {
		return null;
	}

	@Override
	public Features featuresAvailable() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Features featuresProposed() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Features featuresSubscribed() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Indicators indicators() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Module install() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public boolean isInstalled() {
		return false;
	}

	@Override
	public boolean isSubscribed() {
		return false;
	}

	@Override
	public String name() throws IOException {
		return "Aucun module";
	}

	@Override
	public int order() throws IOException {
		return 0;
	}

	@Override
	public String shortName() throws IOException {
		return null;
	}

	@Override
	public Module subscribe() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public FeatureSubscribed subscribeTo(Feature arg0) throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public ModuleType type() throws IOException {
		return ModuleType.NONE;
	}

	@Override
	public Module uninstall() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Module unsubscribe() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void unsubscribeTo(Feature arg0) throws IOException {

	}

	@Override
	public boolean isNone() {
		return false;
	}

	@Override
	public ArticleCategories articleCategories() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public ArticleFamilies articleFamilies() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Articles articles() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Locations locations() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public OperationTypes operationTypes() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Warehouses warehouses() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public StockMovements stockMovements() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Operations operations() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Sequences sequences() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public MesureUnits mesureUnits() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Contacts contacts() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Log log() throws IOException {
		return null;
	}
}
