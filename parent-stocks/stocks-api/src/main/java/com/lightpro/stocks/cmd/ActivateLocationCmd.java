package com.lightpro.stocks.cmd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActivateLocationCmd {
	private final boolean activate;
	
	public ActivateLocationCmd(){
		throw new UnsupportedOperationException("#ActivateLocationCmd()");
	}
	
	@JsonCreator
	public ActivateLocationCmd(@JsonProperty("activate") final boolean activate){
		
		this.activate = activate;
	}
	
	public boolean activate(){
		return activate;
	}
}
