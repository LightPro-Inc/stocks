package com.stocks.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.EntityNone;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.StockMovement;

public final class StockMovementNone extends EntityNone<StockMovement, UUID> implements StockMovement {

	@Override
	public double quantity() throws IOException {
		return 0;
	}

	@Override
	public Article article() throws IOException {
		return new ArticleNone();
	}

	@Override
	public Operation operation() throws IOException {
		return new OperationNone();
	}

	@Override
	public void update(double quantity, Article article) throws IOException {

	}

	@Override
	public void execute() throws IOException {

	}
}
