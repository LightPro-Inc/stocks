package com.lightpro.stocks.rs;

import java.io.IOException;
import java.util.ArrayList;
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
import com.lightpro.stocks.cmd.LocationEdited;
import com.lightpro.stocks.cmd.OperationTypeEdited;
import com.lightpro.stocks.cmd.WarehouseEdited;
import com.lightpro.stocks.vm.ArticleStocksVm;
import com.lightpro.stocks.vm.LocationVm;
import com.lightpro.stocks.vm.OperationTypeVm;
import com.lightpro.stocks.vm.WarehouseVm;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.OperationTypes;
import com.stocks.domains.api.Warehouse;
import com.stocks.domains.api.Warehouses;

@Path("/warehouse")
public class WarehouseRs extends StocksBaseRs {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<WarehouseVm> items = stocks().warehouses().all()
								 .stream()
						 		 .map(m -> new WarehouseVm(m))
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
						
						Warehouses containers = stocks().warehouses();
						
						List<WarehouseVm> itemsVm = containers.find(page, pageSize, filter).stream()
																   .map(m -> new WarehouseVm(m))
																   .collect(Collectors.toList());
													
						int count = containers.totalCount(filter);
						PaginationSet<WarehouseVm> pagedSet = new PaginationSet<WarehouseVm>(itemsVm, page, count);
						
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
						
						WarehouseVm item = new WarehouseVm(stocks().warehouses().get(id));

						return Response.ok(item).build();
					}
				});		
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response add(final WarehouseEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						stocks().warehouses().add(cmd.name(), cmd.shortName());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final WarehouseEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Warehouse item = stocks().warehouses().get(id);
						item.update(cmd.name(), cmd.shortName());
						
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
						
						Warehouse item = stocks().warehouses().get(id);
						stocks().warehouses().delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
	
	// locations	
	@GET
	@Path("/{id}/location/internal")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getInternalLocations(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<LocationVm> items = stocks().warehouses().get(id).locations().all()
													 .stream()
											 		 .map(m -> new LocationVm(m))
											 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Path("/{id}/location/all")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllLocations(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<Location> locations = new ArrayList<Location>();
						for (Location location : stocks().locations().all()) {
							if(location.warehouse().id() == null || location.warehouse().id().equals(id)){
								locations.add(location);
							}
						}
						
						List<LocationVm> items = locations.stream()
											 		 .map(m -> new LocationVm(m))
											 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@POST
	@Path("/{id}/location")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addLocation(@PathParam("id") final UUID id, final LocationEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Warehouse item = stocks().warehouses().get(id);						
						item.addLocation(cmd.name(), cmd.shortName());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}	
	
	// type d'opérations
	@GET
	@Path("/{id}/operation-type")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllOperationType(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OperationTypeVm> items = stocks().warehouses().get(id).operationTypes().all()
															 .stream()
													 		 .map(m -> new OperationTypeVm(m))
													 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Path("/{id}/operation-type/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response search( @PathParam("id") final UUID id,
						    @QueryParam("page") int page, 
							@QueryParam("pageSize") int pageSize, 
							@QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						OperationTypes container = stocks().warehouses().get(id).operationTypes();
						
						List<OperationTypeVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new OperationTypeVm(m))
															 .collect(Collectors.toList());
													
						int count = container.totalCount(filter);
						PaginationSet<OperationTypeVm> pagedSet = new PaginationSet<OperationTypeVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
	
	@POST
	@Path("/{id}/operation-type")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addOperationType(@PathParam("id") final UUID id, final OperationTypeEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						stocks().warehouses().get(id).addOperationType(cmd.name(), cmd.defaultSourceLocationId(), cmd.defaultDestinationLocationId(), cmd.categoryId(), cmd.sequenceId());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	// stocks
	@GET
	@Path("/{id}/stock")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllStocks(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ArticleStocksVm> items = stocks().warehouses().get(id).stocks()
															 .stream()
													 		 .map(m -> new ArticleStocksVm(m))
													 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
}
