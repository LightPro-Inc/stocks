package com.stocks.domains.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.DomainMetadata;
import com.infrastructure.core.EntityBase;
import com.infrastructure.datasource.Base;
import com.securities.api.Company;
import com.securities.api.Contacts;
import com.securities.api.Feature;
import com.securities.api.FeatureSubscribed;
import com.securities.api.Features;
import com.securities.api.Indicators;
import com.securities.api.Log;
import com.securities.api.MesureUnitType;
import com.securities.api.MesureUnits;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.Sequences;
import com.stocks.domains.api.ArticleCategories;
import com.stocks.domains.api.ArticleCategoryMetadata;
import com.stocks.domains.api.ArticleFamilies;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.ArticleMetadata;
import com.stocks.domains.api.ArticlePlanningMetadata;
import com.stocks.domains.api.ArticleStockMetadata;
import com.stocks.domains.api.Articles;
import com.stocks.domains.api.LocationMetadata;
import com.stocks.domains.api.LocationType;
import com.stocks.domains.api.Locations;
import com.stocks.domains.api.Operation.OperationStatut;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.StockMovementMetadata;
import com.stocks.domains.api.StockMovements;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.WarehouseMetadata;
import com.stocks.domains.api.Warehouses;

public final class StocksDb extends EntityBase<Stocks, UUID> implements Stocks {

	private final transient Base base;
	private final transient Module origin;
	
	public StocksDb(final Base base, final Module module){
		super(module.id());
		this.base = base;
		this.origin = module;
	}
	
	@Override
	public ArticleCategories articleCategories() {
		return new ArticleCategoriesDb(this.base, this);
	}

	@Override
	public ArticleFamilies articleFamilies() {
		return new ArticleFamiliesDb(this.base, this, new ArticleCategoryNone());
	}

	@Override
	public Articles articles() {
		return new ArticlesDb(this.base, this, new ArticleFamilyNone(), StringUtils.EMPTY);
	}

	@Override
	public Warehouses warehouses() {
		return new WarehousesDb(this.base, this);
	}

	@Override
	public StockMovements stockMovements() throws IOException {
		return new StockMovementsDb(this.base, this, new OperationNone(), new OperationTypeNone(), new WarehouseNone(), new ArticleNone(), OperationStatut.NONE);
	}

	@Override
	public Operations operations() throws IOException {
		return new OperationsDb(this.base, OperationStatut.NONE, this, new OperationTypeNone());
	}

	@Override
	public Company company() throws IOException {
		return origin.company();
	}

	@Override
	public String description() throws IOException {
		return origin.description();
	}

	@Override
	public Module install() throws IOException {
			
		Module module = origin.install();
		
		// 1 - créer les unités de mesures par défaut
		MesureUnits units = company().moduleAdmin().mesureUnits();
		
		if(units.find("Article").isEmpty())
			units.add("Art", "Article", MesureUnitType.QUANTITY);
		
		if(units.find("Kilogramme").isEmpty())
			units.add("Kg", "Kilogramme", MesureUnitType.QUANTITY);
		
		if(units.find("Litre").isEmpty())
			units.add("L", "Litre", MesureUnitType.QUANTITY);
		
		if(units.find("Mètre cube").isEmpty())
			units.add("m3", "Mètre cube", MesureUnitType.QUANTITY);	
		
		if(units.find("Heure").isEmpty())
			units.add("Hr", "Heure", MesureUnitType.TIME);
		
		if(units.find("Mois").isEmpty())
			units.add("Mois", "Mois", MesureUnitType.TIME);
		
		// 2 - créer les emplacements virtuels
		Locations locations = locations();
		locations.addVirtual(LocationType.FOURNISSEUR.toString(), LocationType.FOURNISSEUR.toString(), LocationType.FOURNISSEUR);
		locations.addVirtual(LocationType.CLIENT.toString(), LocationType.CLIENT.toString(), LocationType.CLIENT);
		locations.addVirtual(LocationType.PERTE_INVENTAIRE.toString(), LocationType.PERTE_INVENTAIRE.toString(), LocationType.PERTE_INVENTAIRE);
		
		// 3 - créer l'entrepôt principal
	    warehouses().add("Entrepôt principal", "ENTP");
	    
	    return new StocksDb(base, module);
	}

	@Override
	public boolean isInstalled() {
		return origin.isInstalled();
	}

	@Override
	public boolean isSubscribed() {
		return origin.isSubscribed();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public int order() throws IOException {
		return origin.order();
	}

	@Override
	public ModuleType type() throws IOException {
		return origin.type();
	}

	@Override
	public Module uninstall() throws IOException {
				
		// supprimer les données d'interfaçage
		base.executeUpdate("DELETE FROM sales.team_stock_interfaces WHERE owner_companyid=?", Arrays.asList(company().id()));
		
		// supprimer les données du modules
		List<DomainMetadata> domains = 
				Arrays.asList(
					StockMovementMetadata.create(),
					OperationMetadata.create()			
				);
		
		for (DomainMetadata domainMetadata : domains) {
			base.deleteAll(domainMetadata);
		}
		
		operationTypes().deleteAll();
		
		domains = 
				Arrays.asList(
					ArticleStockMetadata.create(),
					ArticlePlanningMetadata.create(),
					ArticleMetadata.create(),
					LocationMetadata.create(),
					WarehouseMetadata.create(),
					ArticleFamilyMetadata.create(),
					ArticleCategoryMetadata.create()					
				);
		
		for (DomainMetadata domainMetadata : domains) {
			base.deleteAll(domainMetadata);
		}
		
		// supprimer le module
		Module module = origin.uninstall();
				
		return new StocksDb(base, module);
	}

	@Override
	public UUID id() {
		return origin.id();
	}

	@Override
	public String shortName() throws IOException {
		return origin.shortName();
	}

	@Override
	public Locations locations() {
		return new LocationsDb(base, this, new WarehouseNone(), LocationType.NONE);
	}

	@Override
	public OperationTypes operationTypes() {
		return new OperationTypesDb(base, this, new WarehouseNone());
	}

	@Override
	public Sequences sequences() throws IOException {
		return company().moduleAdmin().sequences();
	}

	@Override
	public void activate(boolean active) throws IOException {
		origin.activate(active);
	}

	@Override
	public Features featuresAvailable() throws IOException {
		return origin.featuresAvailable();
	}

	@Override
	public Features featuresSubscribed() throws IOException {
		return origin.featuresSubscribed();
	}

	@Override
	public Indicators indicators() throws IOException {
		return origin.indicators();
	}

	@Override
	public Module subscribe() throws IOException {
		return origin.subscribe();
	}

	@Override
	public FeatureSubscribed subscribeTo(Feature feature) throws IOException {
		return origin.subscribeTo(feature);
	}

	@Override
	public Module unsubscribe() throws IOException {
		return origin.unsubscribe();
	}

	@Override
	public void unsubscribeTo(Feature feature) throws IOException {
		origin.unsubscribeTo(feature);
	}

	@Override
	public Features featuresProposed() throws IOException {
		return origin.featuresProposed();
	}

	@Override
	public boolean isActive() {
		return origin.isActive();
	}
	
	@Override
	public Contacts contacts() throws IOException {
		return company().moduleAdmin().contacts();
	}

	@Override
	public MesureUnits mesureUnits() throws IOException {
		return company().moduleAdmin().mesureUnits();
	}

	@Override
	public Log log() throws IOException {
		return origin.log();
	}
}
