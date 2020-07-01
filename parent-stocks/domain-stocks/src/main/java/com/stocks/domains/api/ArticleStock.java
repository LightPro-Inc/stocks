package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Nonable;

public interface ArticleStock extends Nonable {
	UUID id();
	double quantity() throws IOException;
	void updateQuantity(double quantity) throws IOException;
	StockAlert alert() throws IOException;
	Article article() throws IOException;
	Warehouse warehouse() throws IOException;
	Location location() throws IOException;
	ArticlePlanning planning();
	
	public enum StockAlert {
		
		NORMAL (0, "Normal"),
		MAXIMUM(1, "Maximum"),
		SAFETY (2, "Sécurité"),
		MINIMUM(3, "Minimum");
		
		private final String name;
		private final int id;
		
		StockAlert(int id, String name){
			this.name = name;
			this.id = id;
		}
				
		@Override
		public String toString(){
			return name;			
		}
		
		public int id(){
			return id;			
		}
	}
}
