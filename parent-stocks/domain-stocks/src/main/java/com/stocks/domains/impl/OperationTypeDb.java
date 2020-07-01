package com.stocks.domains.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.TimeConvert;
import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.securities.api.Contact;
import com.securities.api.Contacts;
import com.securities.api.Sequence;
import com.securities.impl.SequenceDb;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.OperationCategory;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.Stocks;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.Operation.OperationStatut;

public final class OperationTypeDb extends GuidKeyEntityDb<OperationType, OperationTypeMetadata> implements OperationType {
	
	private final Stocks module;
	
	public OperationTypeDb(final Base base, final UUID id, final Stocks module){
		super(base, id, "Type d'opération introuvable !");
		this.module = module;
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public Location defaultSourceLocation() throws IOException {
		UUID locationId = ds.get(dm.defaultSourceLocationKey());
		return new LocationDb(base, locationId, module);
	}

	@Override
	public Location defaultDestinationLocation() throws IOException {
		UUID locationId = ds.get(dm.defaultDestinationLocationKey());
		return new LocationDb(base, locationId, module);
	}

	@Override
	public void update(String name, Location defaultSourceLocation, Location defaultDestinationLocation, OperationCategory category, Sequence sequence, OperationType preparationOpType, OperationType returnOpType) throws IOException {
				
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (defaultSourceLocation.id() == null) {
            throw new IllegalArgumentException("Invalid default source location : it can't be empty!");
        }
		
		if (defaultDestinationLocation.id() == null) {
            throw new IllegalArgumentException("Invalid default destination location : it can't be empty!");
        }
		
		if (category == OperationCategory.NONE) {
            throw new IllegalArgumentException("Invalid category : it can't be empty!");
        }
		
		if (sequence.id() == null) {
            throw new IllegalArgumentException("Invalid sequence : it can't be empty!");
        }
		
		if(preparationOpType.equals(this))
			throw new IllegalArgumentException("Type de préparation invalide : il ne peut pas être égal au type courant!");
		
		if(returnOpType.equals(this))
			throw new IllegalArgumentException("Type de retour invalide : il ne peut pas être égal au type courant!");
		
		if((!preparationOpType.equals(preparationOpType()) || !returnOpType.equals(returnOpType())) && !todoOperations().all().isEmpty()){
			throw new IllegalArgumentException("Vous devez terminer les opérations à faire avant de modifier le  type de préparation ou de retour !");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.defaultSourceLocationKey(), defaultSourceLocation.id());
		params.put(dm.defaultDestinationLocationKey(), defaultDestinationLocation.id());
		params.put(dm.operationCategoryIdKey(), category.id());
		params.put(dm.sequenceIdKey(), sequence.id());
		params.put(dm.preparationOpTypeIdKey(), preparationOpType.id());
		params.put(dm.returnOpTypeIdKey(), returnOpType.id());
		
		ds.set(params);	
	}

	@Override
	public Warehouse warehouse() throws IOException {
		UUID warehouseId = ds.get(dm.warehouseIdKey());
		return new WarehouseDb(base, warehouseId, module);
	}

	@Override
	public OperationCategory category() throws IOException {
		int categoryId = ds.get(dm.operationCategoryIdKey());
		return OperationCategory.get(categoryId);
	}

	@Override
	public Sequence sequence() throws IOException {
		UUID sequenceId = ds.get(dm.sequenceIdKey());
		return new SequenceDb(base, sequenceId);
	}

	@Override
	public Operation addOperation(String documentSource, UUID sourceLocationId, UUID destinationLocationId, Date movementDate, boolean delayed, Contact partner) throws IOException {
		return addOperation(UUID.randomUUID(), documentSource, sourceLocationId, destinationLocationId, movementDate, delayed, partner);	
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public Operations unfinishedOperations() throws IOException {
		return warehouse().moduleStocks().operations().of(this).with(OperationStatut.BROUILLON);
	}
	
	@Override
	public Operations todoOperations() throws IOException {
		return warehouse().moduleStocks().operations().of(this).with(OperationStatut.VALIDE);		
	}

	@Override
	public Operations operationsDone() throws IOException {
		return warehouse().moduleStocks().operations().of(this).with(OperationStatut.EXECUTED);		
	}

	@Override
	public OperationType preparationOpType() throws IOException {
		UUID opId = ds.get(dm.preparationOpTypeIdKey());
		if(opId == null)
			return new OperationTypeNone();
		
		return new OperationTypeDb(base, opId, module);
	}

	@Override
	public OperationType returnOpType() throws IOException {
		UUID opId = ds.get(dm.returnOpTypeIdKey());
		if(opId == null)
			return new OperationTypeNone();
		
		return new OperationTypeDb(base, opId, module);
	}

	@Override
	public Operation addOperation(String documentSource, Date movementDate, boolean delayed, Contact partner) throws IOException {
		return addOperation(id, documentSource, movementDate, delayed, partner);
	}

	@Override
	public Operation addOperation(UUID id, String documentSource, UUID sourceLocationId, UUID destinationLocationId, Date movementDate, boolean delayed, Contact partner) throws IOException {
		
		if (sourceLocationId == null) {
            throw new IllegalArgumentException("Vous devez renseigner l'emplacement source !");
        }
		
		if (destinationLocationId == null) {
            throw new IllegalArgumentException("Vous devez renseigner l'emplacement de destination !");
        }		
		
		if(sourceLocationId.equals(destinationLocationId))
			throw new IllegalArgumentException("L'emplacement source doit être différent de l'emplacement de destination !");
		
		OperationMetadata dm = new OperationMetadata();
		DomainsStore ds = base.domainsStore(dm);
		 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.referenceKey(), sequence().generate());
		params.put(dm.documentSourceKey(), documentSource);
		params.put(dm.sourceLocationIdKey(), sourceLocationId);
		params.put(dm.destinationLocationIdKey(), destinationLocationId);
		params.put(dm.statutIdKey(), OperationStatut.BROUILLON.id());
		params.put(dm.delayedKey(), delayed);
		
		Contacts contacts = warehouse().moduleStocks().contacts();
		
		UUID partnerId;
		if(category() == OperationCategory.INTERNAL)
			partnerId = contacts.myCompany().id();
		else
			partnerId = partner.isNone() ? contacts.defaultPerson().id() : partner.id();
			
		params.put(dm.partnerIdKey(), partnerId);
		
		movementDate = delayed ? movementDate : TimeConvert.toDate(LocalDateTime.now(), ZoneId.systemDefault());
		params.put(dm.movementDateKey(), new java.sql.Timestamp(movementDate.getTime()));
		params.put(dm.operationTypeIdKey(), this.id);
				
		ds.set(id, params);
		
		return new OperationDb(this.base, id, module);
	}

	@Override
	public Operation addOperation(UUID id, String documentSource, Date movementDate, boolean delayed, Contact partner) throws IOException {
		return addOperation(id, documentSource, defaultSourceLocation().id(), defaultDestinationLocation().id(), movementDate, delayed, partner);
	}
}
