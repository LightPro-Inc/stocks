/**
 * données de planification d'un article
 * @author oob
 */

package com.stocks.domains.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ArticlePlannings {
	ArticlePlanning get(UUID id) throws IOException;
	ArticlePlanning get(Location location) throws IOException;
	List<ArticlePlanning> all() throws IOException;
}
