/**
 * données de planification d'un article
 * @author oob
 */

package com.stocks.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Queryable;

public interface ArticlePlannings extends Queryable<ArticlePlanning, UUID> {
	ArticlePlanning get(Location location) throws IOException;
	void deleteAll() throws IOException;
}
