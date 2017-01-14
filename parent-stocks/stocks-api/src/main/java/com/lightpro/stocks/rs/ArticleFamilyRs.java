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
import com.lightpro.stocks.cmd.ArticleFamilyEdited;
import com.lightpro.stocks.vm.ArticleFamilyVm;
import com.lightpro.stocks.vm.ArticleVm;
import com.stocks.domains.api.ArticleFamilies;
import com.stocks.domains.api.ArticleFamily;

@Path("/article-family")
public class ArticleFamilyRs extends StocksBaseRs {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ArticleFamilyVm> items = stocks().articleFamilies().all()
								 .stream()
						 		 .map(m -> new ArticleFamilyVm(m))
						 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response search( @QueryParam("page") int page, 
							@QueryParam("pageSize") int pageSize, 
							@QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ArticleFamilies container = stocks().articleFamilies();
						
						List<ArticleFamilyVm> itemsVm = container.find(page, pageSize, filter).stream()
																 .map(m -> new ArticleFamilyVm(m))
																 .collect(Collectors.toList());
													
						int count = container.totalCount(filter);
						PaginationSet<ArticleFamilyVm> pagedSet = new PaginationSet<ArticleFamilyVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingle(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ArticleFamilyVm item = new ArticleFamilyVm(stocks().articleFamilies().get(id));

						return Response.ok(item).build();
					}
				});		
	}
	
	@GET
	@Path("/{id}/article")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllArticles(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ArticleVm> items = stocks().articleFamilies().get(id)
															  .articles()
														      .stream()
													 	      .map(m -> new ArticleVm(m))
													          .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response add(final ArticleFamilyEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						stocks().articleFamilies().add(cmd.name(), cmd.description(), cmd.categoryId());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final ArticleFamilyEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ArticleFamily item = stocks().articleFamilies().get(cmd.id());
						item.update(cmd.name(), cmd.description(), cmd.categoryId());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response delete(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						ArticleFamily item = stocks().articleFamilies().get(id);
						stocks().articleFamilies().delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
