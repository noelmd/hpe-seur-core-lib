package com.hpe.seur.core.lib.audit.builders;

import com.hpe.seur.core.lib.audit.AuditDTO;

public interface AuditMessageBuilder 
{
	public final static String UNKNOWN_USER ="UNKNOWN_USER";
	public final static String HEADER_USER_AGENT = "User-Agent";
	public final static String AUDIT_REQUEST_ID = "auditRequestId";
	public final static String AUDIT_RESPONSE_ID = "auditResponseId";
	
	public AuditDTO getAuditDTO ();
}
