package com.lightpro.stocks.rs;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lightpro.stocks.cmd.OperationEdited;
import com.lightpro.stocks.cmd.StockMovementEdited;
import com.lightpro.stocks.vm.OperationVm;
import com.stocks.domains.api.Article;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Operation;
import com.stocks.domains.api.StockMovement;

@Path("/operation")
public class OperationRs extends StocksBaseRs {
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingle(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						OperationVm item = new OperationVm(stocks().operations().get(id));

						return Response.ok(item).build();
					}
				});		
	}
	
	@POST
	@Path("/{id}/validate")
	@Produces({MediaType.APPLICATION_JSON})
	public Response add(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Operation item = stocks().operations().get(id);
						item.validate();
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final OperationEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
												
						Operation operation = stocks().operations().get(id);
						Location source = stocks().locations().get(cmd.sourceLocationId());
						Location destination = stocks().locations().get(cmd.destinationLocationId());
						
						operation.update(cmd.documentSource(), source, destination, cmd.movementDate(), cmd.delayed());
						
						for (StockMovementEdited sm : cmd.movements()) {
							Article  article = stocks().articles().get(sm.articleId());
							
							StockMovement smv = operation.movements().build(sm.id());
							
							if(smv.isPresent()){
								if(sm.deleted())
								{
									operation.movements().delete(smv);
								}else{
									smv.update(sm.quantity(), article);	
								}					
							}else{
								if(!sm.deleted())
									operation.addMovement(sm.quantity(), article);
							}
						}
						
						return Response.ok(new OperationVm(operation)).build();
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
						
						Operation item = stocks().operations().get(id);
						stocks().operations().delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
