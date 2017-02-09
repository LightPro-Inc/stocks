package com.lightpro.stocks.rs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lightpro.stocks.vm.OperationCategoryVm;
import com.securities.api.Secured;
import com.stocks.domains.api.OperationCategory;

@Path("/operation-category")
public class OperationCategoryRs extends StocksBaseRs {
	
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OperationCategoryVm> items = Arrays.asList(OperationCategory.values())
															 .stream()
													 		 .map(m -> new OperationCategoryVm(m))
													 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
}
