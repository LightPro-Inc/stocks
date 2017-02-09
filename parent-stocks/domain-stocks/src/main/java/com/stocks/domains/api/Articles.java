package com.stocks.domains.api;

import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface Articles extends AdvancedQueryable<Article, UUID>, Updatable<Article> {
	
}
