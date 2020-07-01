package com.lightpro.stocks.rs;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infrastructure.core.PaginationSet;
import com.lightpro.stocks.cmd.ArticleCategoryEdited;
import com.lightpro.stocks.vm.ArticleFamilyVm;
import com.securities.api.MesureUnit;
import com.securities.api.Secured;
import com.lightpro.stocks.vm.ArticleCategoryVm;
import com.stocks.domains.api.ArticleCategories;
import com.stocks.domains.api.ArticleCategory;

@Path("/article-category")
public class ArticleCategoryRs extends StocksBaseRs {
	
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ArticleCategoryVm> items = stocks().articleCategories().all()
															  .stream()
													 		  .map(m -> new ArticleCategoryVm(m))
													 		  .collect(Collectors.toList());
 
						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/{id}/family")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllFamilies(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ArticleFamilyVm> items = stocks().articleCategories().get(id)
															  .families().all()
														      .stream()
													 	      .map(m -> new ArticleFamilyVm(m))
													          .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response search( @QueryParam("page") int page, 
							@QueryParam("pageSize") int pageSize, 
							@QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ArticleCategories container = stocks().articleCategories();
						
						List<ArticleCategoryVm> itemsVm = container.find(page, pageSize, filter)
																  .stream()
																  .map(m -> new ArticleCategoryVm(m))
																  .collect(Collectors.toList());
							
						long count = container.count(filter);
						PaginationSet<ArticleCategoryVm> pagedSet = new PaginationSet<ArticleCategoryVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
	
	@GET
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingle(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ArticleCategoryVm item = new ArticleCategoryVm(stocks().articleCategories().get(id));

						return Response.ok(item).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response add(final ArticleCategoryEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						MesureUnit mesureUnit = stocks().mesureUnits().build(cmd.mesureUnitId());
						stocks().articleCategories().add(cmd.name(), mesureUnit);
						
						log.info(String.format("Création de la catégorie d'article %s", cmd.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final ArticleCategoryEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ArticleCategory item = stocks().articleCategories().get(cmd.id());
						MesureUnit mesureUnit = stocks().mesureUnits().get(cmd.mesureUnitId());
						item.update(cmd.name(), mesureUnit);
						
						log.info(String.format("Mise à jour des données de la catégorie d'article %s", cmd.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response delete(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ArticleCategory item = stocks().articleCategories().get(id);
						String name = item.name();
						stocks().articleCategories().delete(item);
						
						log.info(String.format("Suppression de la catégorie d'article %s", name));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
