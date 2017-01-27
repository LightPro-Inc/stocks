package com.stocks.domains.api;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface Operation extends Recordable<UUID, Operation> {
	
	String reference() throws IOException;	
	OperationType type() throws IOException;
	String documentSource() throws IOException;
	Date movementDate() throws IOException;
	boolean delayed() throws IOException;
	Location sourceLocation() throws IOException;
	Location destinationLocation() throws IOException;
	OperationStatut statut() throws IOException;	
	StockMovements movements() throws IOException;
	
	void update(String documentSource, Location source, Location destination, Date movement, boolean delayed) throws IOException;	
	StockMovement addMovement(int quantity, Article article) throws IOException;
	void validate() throws IOException;
	
	public enum OperationStatut{
		NONE(0, "Non défini"),
		BROUILLON(1, "Brouillon"),
		VALIDE   (2, "Validée");
		
		private final String name;
		private final int id;
		
		OperationStatut(int id, String name){
			this.name = name;
			this.id = id;
		}
		
		public int id(){
			return id;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
}
