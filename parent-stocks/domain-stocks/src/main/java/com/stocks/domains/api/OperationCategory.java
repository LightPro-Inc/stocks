package com.stocks.domains.api;

public enum OperationCategory {
	
	NONE             (0, "Non défini"),
	FOURNISSEUR      (1, "Fournisseur"),
	INTERNAL         (2, "Interne"),
	CLIENT           (3, "Client");	
	
	private final int id;
	private final String name;
	
	OperationCategory(final int id, final String name) {
		this.id = id;
		this.name = name;
	}
	
	public static OperationCategory get(int id){
		
		OperationCategory value = OperationCategory.NONE;
		for (OperationCategory item : OperationCategory.values()) {
			if(item.id() == id)
				value = item;
		}
		
		return value;
	}

	public int id(){
		return id;
	}
	
	public String toString(){
		return name;
	}	
}
