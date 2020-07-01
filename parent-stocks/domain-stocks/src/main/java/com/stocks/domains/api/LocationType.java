package com.stocks.domains.api;

public enum LocationType {
	NONE             (0, "Non défini", "N/O"),
	INTERNAL         (1, "Emplacement interne", "V/INTERNE"),
	FOURNISSEUR      (2, "Emplacement fournisseur", "V/FOURNISSEUR"),
	CLIENT           (3, "Emplacement client", "V/CLIENT"),
	PERTE_INVENTAIRE (4, "Perte d'inventaire", "V/PERTEINV"),
	APPRO            (5, "Emplacement d'approvisionnement", "V/APPRO"),
	PROD             (6, "Emplacement de production", "V/PROD"),
	TRANSIT          (7, "Emplacement de transit", "V/TRANSIT"),
	RETOUR           (8, "Emplacement de retour", "V/RETOUR"),
	REBUT            (9, "Emplacement de rebut", "V/REBUT");
	
	private final int id;
	private final String name;
	private final String shortName;
	
	LocationType(final int id, final String name, final String shortName){
		this.id = id;
		this.name = name;
		this.shortName = shortName;
	}
	
	public static LocationType get(int id){
		
		LocationType value = LocationType.NONE;
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
	
	public String shortName(){
		return shortName;
	}
}
