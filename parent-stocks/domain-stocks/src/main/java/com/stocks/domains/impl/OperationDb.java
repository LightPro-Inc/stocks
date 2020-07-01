package com.stocks.domains.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.securities.api.Contact;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.StockMovement;
import com.stocks.domains.api.StockMovementMetadata;
import com.stocks.domains.api.StockMovements;
import com.stocks.domains.api.Stocks;

public final class OperationDb extends GuidKeyEntityDb<Operation, OperationMetadata> implements Operation {

	private final Stocks module;
	
	public OperationDb(final Base base, final UUID id, final Stocks module){
		super(base, id, "Opération introuvable !");
		this.module = module;
	}

	@Override
	public String reference() throws IOException {
		return ds.get(dm.referenceKey());
	}

	@Override
	public OperationType type() throws IOException {
		UUID typeId = ds.get(dm.operationTypeIdKey());
		return new OperationTypeDb(this.base, typeId, module);		
	}

	@Override
	public String documentSource() throws IOException {
		return ds.get(dm.documentSourceKey());
	}

	@Override
	public Location sourceLocation() throws IOException {
		UUID sourceLocationId = ds.get(dm.sourceLocationIdKey());
		return new LocationDb(this.base, sourceLocationId, module);
	}

	@Override
	public Location destinationLocation() throws IOException {
		UUID destinationLocationId = ds.get(dm.destinationLocationIdKey());
		return new LocationDb(this.base, destinationLocationId, module);
	}

	@Override
	public OperationStatut statut() throws IOException {
		int statutId = ds.get(dm.statutIdKey());
		
		for (OperationStatut statut : OperationStatut.values()) {
			if(statut.id() == statutId)
				return statut;
		}
		
		throw new NotFoundException("Statut de l'opération indéfini !");
	}

	@Override
	public void update(String documentSource, Location source, Location destination, Date movementDate, boolean delayed, Contact partner) throws IOException {
		
		if(statut() != OperationStatut.BROUILLON)
			throw new IllegalArgumentException("Le document doit être en mode brouillon pour être modifiée !");
		
		if (source.isNone()) {
            throw new IllegalArgumentException("Vous devez renseigner l'emplacement source !");
        }
		
		if (destination.isNone()) {
            throw new IllegalArgumentException("Vous devez renseigner l'emplacement de destination !");
        }
		
		if(source.equals(destination))
			throw new IllegalArgumentException("L'emplacement source doit être différent de l'emplacement de destination !");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.documentSourceKey(), documentSource);
		params.put(dm.sourceLocationIdKey(), source.id());
		params.put(dm.destinationLocationIdKey(), destination.id());
		params.put(dm.destinationLocationIdKey(), destination.id());
		
		UUID partnerId = partner.id() != null ? partner.id() : type().warehouse().moduleStocks().contacts().defaultPerson().id();
		params.put(dm.partnerIdKey(), partnerId);
		params.put(dm.delayedKey(), delayed);				
		params.put(dm.movementDateKey(), new java.sql.Timestamp(movementDate.getTime()));
		
		ds.set(params);
	}

	@Override
	public void validate() throws IOException {		
		
		if(statut() != OperationStatut.BROUILLON)
			throw new IllegalArgumentException("L'opération doit être en mode brouillon pour être validée !");		
		
		if(!(sourceLocation().isInternal() || destinationLocation().isInternal()))
			throw new IllegalArgumentException("Vous devez sélectionner au moins un emplacement interne !");
			
		// 1 - exécuter l'opération de préparation
		OperationType preparationOpType = type().preparationOpType();
		if(!preparationOpType.isNone()){
			Operation op = preparationOpType.addOperation(documentSource(), null, false, partner());
			for (StockMovement mvt : this.movements().all()) {
				op.addMovement(mvt.quantity(), mvt.article());
			}
			
			op.validate();
			op.execute();
		}
				
		ds.set(dm.statutIdKey(), OperationStatut.VALIDE.id()); 
	}
	
	@Override
	public void execute() throws IOException {		
		
		if(statut() != OperationStatut.VALIDE)
			throw new IllegalArgumentException("L'opération doit être validée pour être exécutée !");		
		
		// 1 - mettre à jour le stock total pour chaque mouvement
		for (StockMovement mvt : this.movements().all()) {			
			mvt.execute();
		}
		
		// 2 - changer le statut de l'opération à exécuter		
		ds.set(dm.statutIdKey(), OperationStatut.EXECUTED.id()); 
	}

	@Override
	public StockMovement addMovement(double quantity, Article article) throws IOException {
		
		if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure 0 !");
        }
		
		if (article.isNone()) {
            throw new IllegalArgumentException("Vous devez renseigner l'article !");
        }
		
		StockMovementMetadata dm = new StockMovementMetadata();
		DomainsStore ds = base.domainsStore(dm);
		 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.articleIdKey(), article.id());
		params.put(dm.quantityKey(), quantity);
		params.put(dm.operationIdKey(), this.id);
				
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new StockMovementDb(this.base, id, module);
	}

	@Override
	public StockMovements movements() throws IOException {
		return type().warehouse().moduleStocks().stockMovements().of(this);
	}

	@Override
	public Date movementDate() throws IOException {
		return ds.get(dm.movementDateKey());
	}

	@Override
	public boolean delayed() throws IOException {
		return ds.get(dm.delayedKey());
	}

	@Override
	public Contact partner() throws IOException {
		UUID partnerId = ds.get(dm.partnerIdKey());
		return type().warehouse().moduleStocks().contacts().build(partnerId);
	}
}
