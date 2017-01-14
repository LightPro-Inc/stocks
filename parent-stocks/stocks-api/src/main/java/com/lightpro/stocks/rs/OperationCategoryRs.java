package com.lightpro.stocks.rs;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lightpro.stocks.vm.OperationCategoryVm;

@Path("/operation-category")
public class OperationCategoryRs extends StocksBaseRs {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OperationCategoryVm> items = stocks().operationCategories().all()
															 .stream()
													 		 .map(m -> new OperationCategoryVm(m))
													 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
}
