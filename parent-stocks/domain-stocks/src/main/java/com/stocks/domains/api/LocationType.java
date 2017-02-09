package com.stocks.domains.api;

public enum LocationType {
	
	INTERNAL         (1, "Emplacement interne"),
	FOURNISSEUR      (2, "Emplacement fournisseur"),
	CLIENT           (3, "Emplacement client"),
	PERTE_INVENTAIRE (4, "Perte d'inventaire"),
	APPRO            (5, "Approvisionnement"),
	PROD             (6, "Production"),
	TRANSIT          (7, "Emplacement de transit"),
	RETOUR           (8, "Retour"),
	REBUT            (9, "Rebut");
	
	private final int id;
	private final String name;
	
	LocationType(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static LocationType get(int id){
		
		LocationType value = LocationType.INTERNAL;
		for (LocationType item : LocationType.values()) {
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
