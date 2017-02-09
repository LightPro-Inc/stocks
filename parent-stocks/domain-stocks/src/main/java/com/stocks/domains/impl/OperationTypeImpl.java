package com.stocks.domains.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.common.utilities.convert.TimeConvert;
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
	private final transient UUID id;
	private final transient OperationTypeMetadata dm;
	private final transient DomainStore ds;
	
	public OperationTypeImpl(final Base base, final UUID id){
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
	public void update(String name, Location defaultSourceLocation, Location defaultDestinationLocation, OperationCategory category, Sequence sequence) throws IOException {
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if (!defaultSourceLocation.isPresent()) {
            throw new IllegalArgumentException("Invalid default source location : it can't be empty!");
        }
		
		if (!defaultDestinationLocation.isPresent()) {
            throw new IllegalArgumentException("Invalid default destination location : it can't be empty!");
        }
		
		if (category == null) {
            throw new IllegalArgumentException("Invalid category : it can't be empty!");
        }
		
		if (!sequence.isPresent()) {
            throw new IllegalArgumentException("Invalid sequence : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);
		params.put(dm.defaultSourceLocationKey(), defaultSourceLocation.id());
		params.put(dm.defaultDestinationLocationKey(), defaultDestinationLocation.id());
		params.put(dm.operationCategoryIdKey(), category.id());
		params.put(dm.sequenceIdKey(), sequence.id());
		
		ds.set(params);	
	}

	@Override
	public Warehouse warehouse() throws IOException {
		UUID warehouseId = ds.get(dm.warehouseIdKey());
		return new WarehouseImpl(base, warehouseId);
	}

	@Override
	public OperationCategory category() throws IOException {
		int categoryId = ds.get(dm.operationCategoryIdKey());
		return OperationCategory.get(categoryId);
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
		
		movementDate = delayed ? movementDate : TimeConvert.toDate(LocalDateTime.now(), ZoneId.systemDefault());
		params.put(dm.movementDateKey(), new java.sql.Timestamp(movementDate.getTime()));
		params.put(dm.operationTypeIdKey(), this.id);
				
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return new OperationImpl(this.base, id);
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public Operations unfinishedOperations() throws IOException {
		return new OperationsByType(base, OperationStatut.BROUILLON, this);
	}

	@Override
	public Operations operationsDone() throws IOException {
		return new OperationsByType(base, OperationStatut.VALIDE, this);
	}
	
	@Override
	public boolean isEqual(OperationType item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(OperationType item) {
		return !isEqual(item);
	}
}
