package com.hpe.seur.core.lib.audit;

import org.apache.cxf.phase.AbstractPhaseInterceptor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hpe.seur.core.lib.audit.builders.AuditCamelMessageBuilder;
import com.hpe.seur.core.lib.audit.builders.AuditCxfMessageBuilder;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AuditDTOFactory 
{
	//private final static String UNKNOWN_COMPONENT ="UNKNOWN_COMPONENT";
	
	public AuditDTO getAuditDTO (org.apache.camel.Message message, AuditType auditType)
	{
		//AuditDTO auditDTO = null;
		
		AuditCamelMessageBuilder camelMessageBuilder = new AuditCamelMessageBuilder( message, auditType);
			
		return camelMessageBuilder.getAuditDTO();
	}
	
	public AuditDTO getAuditDTO (org.apache.cxf.message.Message message, AuditType auditType )
	{
		//AuditDTO auditDTO = null;
		
		AuditCxfMessageBuilder camelMessageBuilder = new AuditCxfMessageBuilder(message, auditType);
			
		return camelMessageBuilder.getAuditDTO();
	}
	
	public AuditDTO getAuditDTO (org.apache.cxf.message.Message message, AbstractPhaseInterceptor interceptor)
	{
		//AuditDTO auditDTO = null;
		
		AuditCxfMessageBuilder camelMessageBuilder = new AuditCxfMessageBuilder(message, interceptor);
		
		return camelMessageBuilder.getAuditDTO();
	}
}
