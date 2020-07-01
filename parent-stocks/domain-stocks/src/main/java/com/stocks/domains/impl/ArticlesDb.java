package com.stocks.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.AdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.ArticleCategoryMetadata;
import com.stocks.domains.api.ArticleFamily;
import com.stocks.domains.api.ArticleFamilyMetadata;
import com.stocks.domains.api.ArticleMetadata;
import com.stocks.domains.api.Articles;
import com.stocks.domains.api.Stocks;

public class ArticlesDb extends AdvancedQueryableDb<Article, UUID, ArticleMetadata> implements Articles {

	private final transient ArticleFamily family;
	private final transient Stocks module;
	private final transient String internalReference;
	
	public ArticlesDb(final Base base, Stocks module, final ArticleFamily family, String internalReference){
		super(base, "Article introuvable !");
		this.family = family;
		this.module = module;
		this.internalReference = internalReference;
	}

	@Override
	public Article add(String name, String internalReference, String barCode, double quantity, double cost, String description, String emballage) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		if(StringUtils.isBlank(emballage))
			throw new IllegalArgumentException("Emballage invalide : il ne peut pas être vide !");
		
		if(family.id() == null)
			throw new IllegalArgumentException("Vous devez renseigner la famille de l'article !");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(!StringUtils.isBlank(internalReference)) {
			internalReference = internalReference.trim();
			
			Articles articles = withInternalReference(internalReference);
			if(!articles.isEmpty())
				throw new IllegalArgumentException("Cette référence interne existe déjà !");
			
			params.put(dm.internalReferenceKey(), internalReference);
		}
			
		params.put(dm.nameKey(), name);		
		params.put(dm.quantityKey(), quantity);		
		params.put(dm.costKey(), cost);		
		params.put(dm.descriptionKey(), description);
		params.put(dm.familyIdKey(), family.id());
		params.put(dm.barCodeKey(), barCode);
		params.put(dm.emballageKey(), emballage);
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);		
	}

	@Override
	public Articles of(ArticleFamily family) {
		return new ArticlesDb(base, module, family, internalReference);
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		ArticleFamilyMetadata faDm = new ArticleFamilyMetadata();
		ArticleCategoryMetadata catDm = new ArticleCategoryMetadata();
		String statement = String.format("%s art "
				+ "JOIN %s fa ON fa.%s=art.%s "
				+ "LEFT JOIN %s cat ON cat.%s=fa.%s "
				+ "WHERE (art.%s ILIKE ? OR art.%s ILIKE ? OR art.%s ILIKE ?) AND cat.%s=?",
				dm.domainName(), 
				faDm.domainName(), faDm.keyName(), dm.familyIdKey(),
				catDm.domainName(), catDm.keyName(), faDm.categoryIdKey(),
				dm.nameKey(), dm.internalReferenceKey(), dm.barCodeKey(), catDm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(!StringUtils.isBlank(internalReference)){
			statement = String.format("%s AND art.%s=?", statement, dm.internalReferenceKey());
			params.add(internalReference);
		}
		
		if(family.id() != null) {
			statement = String.format("%s AND fa.%s=?", statement, faDm.keyName());
			params.add(family.id());
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY art.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("art.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected UUID convertKey(Object id) {
		return UUIDConvert.fromObject(id);
	}

	@Override
	protected Article newOne(UUID id) {
		return new ArticleDb(base, id, module);
	}

	@Override
	public Article none() {
		return new ArticleNone();
	}
	
	@Override
	public void delete(Article item) throws IOException {
		if(contains(item)){
			// 1 - vérifier qu'il n'existe pas de mouvements
			if(item.movements().count() > 0)
				throw new IllegalArgumentException("L'article ne peut pas être supprimé : il existe un historique de mouvements de stock !");
						
			// supprimer la plannification des données
			item.plannings().deleteAll();
			
			// suppression de l'article
			ds.delete(item.id());
		}
	}

	@Override
	public Articles withInternalReference(String internalReference) {
		return new ArticlesDb(base, module, family, internalReference);
	}
}
