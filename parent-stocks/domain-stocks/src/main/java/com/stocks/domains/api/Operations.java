package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface Operations extends AdvancedQueryable<Operation, UUID>, Updatable<Operation> {
	void deleteAll() throws IOException;
}
