package com.stocks.domains.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.StockMovement;
import com.stocks.domains.api.StockMovementMetadata;
import com.stocks.domains.api.StockMovements;

public class OperationImpl implements Operation {

	private final transient Base base;
	private final transient UUID id;
	private final transient OperationMetadata dm;
	private final transient DomainStore ds;
	
	public OperationImpl(final Base base, final UUID id){
		this.base = base;
		this.id = id;
		this.dm = dm();
		this.ds = this.base.domainsStore(this.dm).createDs(id);	
	}
	
	@Override
	public UUID id() {
		return this.id;
	}

	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}

	@Override
	public String reference() throws IOException {
		return ds.get(dm.referenceKey());
	}

	@Override
	public OperationType type() throws IOException {
		UUID typeId = ds.get(dm.operationTypeIdKey());
		return new OperationTypeImpl(this.base, typeId);
		
	}

	@Override
	public String documentSource() throws IOException {
		return ds.get(dm.documentSourceKey());
	}

	@Override
	public Location sourceLocation() throws IOException {
		UUID sourceLocationId = ds.get(dm.sourceLocationIdKey());
		return new LocationImpl(this.base, sourceLocationId);
	}

	@Override
	public Location destinationLocation() throws IOException {
		UUID destinationLocationId = ds.get(dm.destinationLocationIdKey());
		return new LocationImpl(this.base, destinationLocationId);
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
	public void update(String documentSource, Location source, Location destination, Date movementDate, boolean delayed) throws IOException {
		
		if (!source.isPresent()) {
            throw new IllegalArgumentException("Invalid source location : it can't be empty!");
        }
		
		if (!destination.isPresent()) {
            throw new IllegalArgumentException("Invalid destination location : it can't be empty!");
        }
		
		OperationMetadata dm = OperationImpl.dm();
		 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.documentSourceKey(), documentSource);
		params.put(dm.sourceLocationIdKey(), source.id());
		params.put(dm.destinationLocationIdKey(), destination.id());
		params.put(dm.destinationLocationIdKey(), destination.id());
		params.put(dm.delayedKey(), delayed);				
		params.put(dm.movementDateKey(), new java.sql.Timestamp(movementDate.getTime()));
		
		ds.set(params);
	}

	@Override
	public void validate() throws IOException {		
		
		if(statut() == OperationStatut.VALIDE)
			return;
		
		if(!(sourceLocation().isInternal() || destinationLocation().isInternal()))
			throw new IllegalArgumentException("Vous devez sélectionner au moins un emplacement interne !");
		
		// 1 - mettre à jour le stock total pour chaque mouvement
		for (StockMovement mvt : this.movements().all()) {			
			mvt.execute();
		}
		
		// 2 - changer le statut de l'opération à validé		
		ds.set(dm.statutIdKey(), OperationStatut.VALIDE.id()); 
	}
	
	public static OperationMetadata dm(){
		return new OperationMetadata();
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public StockMovement addMovement(int quantity, Article article) throws IOException {
		if (quantity == 0) {
            throw new IllegalArgumentException("Invalid quantity : it can't be equal to zero!");
        }
		
		if (!article.isPresent()) {
            throw new IllegalArgumentException("Invalid source location : it can't be empty!");
        }
		
		StockMovementMetadata dm = StockMovementImpl.dm();
		DomainsStore ds = base.domainsStore(dm);
		 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.articleIdKey(), article.id());
		params.put(dm.quantityKey(), quantity);
		params.put(dm.operationIdKey(), this.id);
				
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new StockMovementImpl(this.base, id);
	}

	@Override
	public StockMovements movements() throws IOException {
		return new OperationMovements(this.base, id);
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
	public boolean isEqual(Operation item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(Operation item) {
		return !isEqual(item);
	}
}
