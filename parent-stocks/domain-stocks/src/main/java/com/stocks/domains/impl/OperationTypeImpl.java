package com.stocks.domains.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.securities.api.Sequence;
import com.securities.impl.SequenceImpl;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.OperationCategory;
import com.stocks.domains.api.OperationMetadata;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypeMetadata;
import com.stocks.domains.api.Operations;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.Operation.OperationStatut;

public class OperationTypeImpl implements OperationType {

	private final transient Base base;
	private final transient Object id;
	private final transient OperationTypeMetadata dm;
	private final transient DomainStore ds;
	
	public OperationTypeImpl(final Base base, final Object id){
		this.base = base;
		this.id = id;
		this.dm = dm();
		this.ds = this.base.domainsStore(this.dm).createDs(id);	
	}
	
	@Override
	public UUID id() {
		return UUIDConvert.fromObject(this.id);
	}

	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}	

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public Location defaultSourceLocation() throws IOException {
		UUID locationId = ds.get(dm.defaultSourceLocationKey());
		return new LocationImpl(base, locationId);
	}

	@Override
	public Location defaultDestinationLocation() throws IOException {
		UUID locationId = ds.get(dm.defaultDestinationLocationKey());
		return new LocationImpl(base, locationId);
	}

	public static OperationTypeMetadata dm(){
		return new OperationTypeMetadata();
	}

	@Override
	public void update(String name, UUID defaultSourceLocationId, UUID defaultDestinationLocationId, String categoryId, UUID sequenceId) throws IOException {
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (defaultSourceLocationId == null) {
            throw new IllegalArgumentException("Invalid default source location : it can't be empty!");
        }
		
		if (defaultDestinationLocationId == null) {
            throw new IllegalArgumentException("Invalid default destination location : it can't be empty!");
        }
		
		if (categoryId == null || categoryId.isEmpty()) {
            throw new IllegalArgumentException("Invalid category : it can't be empty!");
        }
		
		if (sequenceId == null) {
            throw new IllegalArgumentException("Invalid sequence : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.defaultSourceLocationKey(), defaultSourceLocationId);
		params.put(dm.defaultDestinationLocationKey(), defaultDestinationLocationId);
		params.put(dm.operationCategoryIdKey(), categoryId);
		params.put(dm.sequenceIdKey(), sequenceId);
		
		ds.set(params);	
	}

	@Override
	public Warehouse warehouse() throws IOException {
		UUID warehouseId = ds.get(dm.warehouseIdKey());
		return new WarehouseImpl(base, warehouseId);
	}

	@Override
	public OperationCategory category() throws IOException {
		String categoryId = ds.get(dm.operationCategoryIdKey());
		return new OperationCategoriesImpl().get(categoryId);
	}

	@Override
	public Sequence sequence() throws IOException {
		UUID sequenceId = ds.get(dm.sequenceIdKey());
		return new SequenceImpl(base, sequenceId);
	}

	@Override
	public Operation addOperation(String documentSource, UUID sourceLocationId, UUID destinationLocationId, Date movementDate, boolean delayed) throws IOException {
		
		if (sourceLocationId == null) {
            throw new IllegalArgumentException("Invalid source location : it can't be empty!");
        }
		
		if (destinationLocationId == null) {
            throw new IllegalArgumentException("Invalid destination location : it can't be empty!");
        }
		
		OperationMetadata dm = OperationImpl.dm();
		DomainsStore ds = base.domainsStore(dm);
		 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.referenceKey(), sequence().generate());
		params.put(dm.documentSourceKey(), documentSource);
		params.put(dm.sourceLocationIdKey(), sourceLocationId);
		params.put(dm.destinationLocationIdKey(), destinationLocationId);
		params.put(dm.statutIdKey(), OperationStatut.BROUILLON.id());
		params.put(dm.delayedKey(), delayed);
		
		movementDate = delayed ? movementDate : Date.from(Instant.from(java.time.LocalDate.now().atStartOfDay()));
		params.put(dm.movementDateKey(), new java.sql.Timestamp(movementDate.getTime()));
		params.put(dm.operationTypeIdKey(), this.id);
				
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new OperationImpl(this.base, id);
	}

	@Override
	public boolean isPresent() throws IOException {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public Operations unfinishedOperations() {
		return new OperationsByType(base, OperationStatut.BROUILLON, this.id);
	}

	@Override
	public Operations operationsDone() {
		return new OperationsByType(base, OperationStatut.VALIDE, this.id);
	}
}
