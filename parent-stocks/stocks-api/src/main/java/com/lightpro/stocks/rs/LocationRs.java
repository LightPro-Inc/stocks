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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lightpro.stocks.cmd.ActivateLocationCmd;
import com.lightpro.stocks.cmd.LocationEdited;
import com.lightpro.stocks.vm.LocationVm;
import com.stocks.domains.api.Location;

@Path("/location")
public class LocationRs extends StocksBaseRs {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<LocationVm> items = stocks().locations().all()
													 .stream()
											 		 .map(m -> new LocationVm(m))
											 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingleLocation(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Location location = stocks().locations().get(id);

						return Response.ok(new LocationVm(location)).build();
					}
				});		
	}
	
	@POST
	@Path("/{id}/activate")
	@Produces({MediaType.APPLICATION_JSON})
	public Response activateLocation(@PathParam("id") final UUID id, final ActivateLocationCmd cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Location location = stocks().locations().get(id);
						
						location.activate(cmd.activate());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateLocation(@PathParam("id") final UUID id, final LocationEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Location item = stocks().locations().get(id);						
						item.update(cmd.name(), cmd.shortName());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteLocation(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>() {
					@Override
					public Response call() throws IOException {
						
						Location item = stocks().locations().get(id);						
						stocks().locations().delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
