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
import com.lightpro.stocks.cmd.OperationEdited;
import com.lightpro.stocks.cmd.OperationTypeEdited;
import com.lightpro.stocks.cmd.StockMovementEdited;
import com.lightpro.stocks.vm.OperationTypeVm;
import com.lightpro.stocks.vm.OperationVm;
import com.securities.api.Secured;
import com.securities.api.Sequence;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.OperationType;
import com.stocks.domains.api.OperationTypes;

@Path("/operation-type")
public class OperationTypeRs extends StocksBaseRs {
	
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OperationTypeVm> items = stocks().operationTypes().all()
															 .stream()
													 		 .map(m -> new OperationTypeVm(m))
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
						
						OperationTypes container = stocks().operationTypes();
						
						List<OperationTypeVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new OperationTypeVm(m))
															 .collect(Collectors.toList());
													
						int count = container.totalCount(filter);
						PaginationSet<OperationTypeVm> pagedSet = new PaginationSet<OperationTypeVm>(itemsVm, page, count);
						
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
						
						OperationTypeVm item = new OperationTypeVm(stocks().operationTypes().get(id));

						return Response.ok(item).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final OperationTypeEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						OperationType item = stocks().operationTypes().get(cmd.id());
						Location source = stocks().locations().get(cmd.defaultSourceLocationId());
						Location destination = stocks().locations().get(cmd.defaultDestinationLocationId());
						Sequence sequence = stocks().company().sequences().get(cmd.sequenceId());
						
						item.update(cmd.name(), source, destination, cmd.category(), sequence);
						
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
						
						OperationType item = stocks().operationTypes().get(id);
						stocks().operationTypes().delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
	
	// opération
	@GET
	@Secured
	@Path("{id}/operation/unfinished")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllOperations(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OperationVm> items = stocks().operationTypes().get(id).unfinishedOperations().all()
																		 .stream()
																 		 .map(m -> new OperationVm(m))
																 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/{id}/operation")
	@Produces({MediaType.APPLICATION_JSON})
	public Response add(@PathParam("id") final UUID id, final OperationEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						OperationType type = stocks().operationTypes().get(id);
						Operation operation = type.addOperation(cmd.documentSource(), cmd.sourceLocationId(), cmd.destinationLocationId(), cmd.movementDate(), cmd.delayed());
						for (StockMovementEdited sm : cmd.movements()) {
							Article  article = stocks().articles().get(sm.articleId());
							operation.addMovement(sm.quantity(), article);
						}
						
						return Response.ok(new OperationVm(operation)).build();
					}
				});		
	}
}
