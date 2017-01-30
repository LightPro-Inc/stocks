package com.stocks.domains.api;

import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface OperationTypes extends AdvancedQueryable<OperationType, UUID>, Updatable<OperationType> {

}
