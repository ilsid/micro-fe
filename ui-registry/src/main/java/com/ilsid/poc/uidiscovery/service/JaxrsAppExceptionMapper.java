package com.ilsid.poc.uidiscovery.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * 
 * @author illia.sydorovych
 *
 */
@Provider
public class JaxrsAppExceptionMapper implements ExceptionMapper<AppException> {

	public Response toResponse(AppException exception) {
		return Response.status(exception.getStatus()).entity(exception.getEntity()).build();
	}

}
