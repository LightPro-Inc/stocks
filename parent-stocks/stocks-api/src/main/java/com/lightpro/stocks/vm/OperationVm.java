package com.lightpro.stocks.vm;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.stocks.domains.api.Operation;

public final class OperationVm {

	public final UUID id;
	public final String reference;
	public final Date movementDate;
	public final String documentSource;
	public final String sourceLocation;
	public final UUID sourceLocationId;
	public final String destinationLocation;
	public final UUID destinationLocationId;
	public final String type;
	public final UUID typeId;
	public final String partner;
	public final UUID partnerId;
	public final String statut;
	public final int statutId;
	public final List<StockMovementVm> movements;
	public final boolean delayed;
	
	public OperationVm(){
		throw new UnsupportedOperationException("#OperationVm()");
	}
	
	public OperationVm(final Operation origin) {
		try {
			this.id = origin.id();
			this.reference = origin.reference();
			this.movementDate = origin.movementDate();
	        this.documentSource = origin.documentSource();
	        this.sourceLocation = origin.sourceLocation().name();
	        this.sourceLocationId = origin.sourceLocation().id();
	        this.destinationLocation = origin.destinationLocation().name();
			this.destinationLocationId = origin.destinationLocation().id();
	        this.type = origin.type().toString();
	        this.typeId = origin.type().id();
	        this.partner = origin.partner().name();
	        this.partnerId = origin.partner().id();
	        this.statut = origin.statut().toString();
	        this.statutId = origin.statut().id();
	        this.movements = origin.movements().all()
									 .stream()
							 		 .map(m -> new StockMovementVm(m))
							 		 .collect(Collectors.toList());
	        this.delayed = origin.delayed();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
