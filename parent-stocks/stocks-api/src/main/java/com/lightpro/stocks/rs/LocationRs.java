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

import com.lightpro.stocks.cmd.ActivateLocationCmd;
import com.lightpro.stocks.cmd.LocationEdited;
import com.lightpro.stocks.vm.LocationVm;
import com.securities.api.Secured;
import com.stocks.domains.api.Location;
import com.stocks.domains.api.Locations;

@Path("/location")
public class LocationRs extends StocksBaseRs {

	@GET
	@Secured
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
	@Secured
	@Path("/{id}/activate")
	@Produces({MediaType.APPLICATION_JSON})
	public Response activateLocation(@PathParam("id") final UUID id, final ActivateLocationCmd cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Location location = stocks().locations().get(id);
						
						location.activate(cmd.activate());
						
						log.info(String.format("Activation de l'emplacement %s", location.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateLocation(@PathParam("id") final UUID id, final LocationEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Location item = stocks().locations().get(id);						
						item.update(cmd.name(), cmd.shortName());
						
						log.info(String.format("Mise à jour des données de l'emplacement %s", cmd.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteLocation(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>() {
					@Override
					public Response call() throws IOException {
						
						Locations locations = stocks().locations();
						Location item = locations.get(id);			
						String name = item.name();
						locations.delete(item);
						
						log.info(String.format("Suppression de l'emplacement %s", name));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
