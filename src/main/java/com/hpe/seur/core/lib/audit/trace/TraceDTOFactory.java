package com.hpe.seur.core.lib.audit.trace;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TraceDTOFactory {

	public TraceDTOFactory() 
	{
		super();
	}
	
	//private Methods
	
	private static String generateTraceId()
	{
		Date currentDate = new Date();
		String uuid = UUID.randomUUID().toString() + "-" + currentDate.toString();

		return uuid;
	}

	private static Date generateTimestamp()
	{
		Date currentDate = new Date();

		return currentDate;
	}
	
	public TraceDTO createTraceDTO()
	{
		TraceDTO traceDTO = new TraceDTO();
		
		traceDTO.setTraceId(TraceDTOFactory.generateTraceId());
		traceDTO.setTimeStamp(TraceDTOFactory.generateTimestamp());
		
		return traceDTO;
				
	}
}
