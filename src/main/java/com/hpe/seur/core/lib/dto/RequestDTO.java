package com.hpe.seur.core.lib.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonPropertyOrder({"auditIn","auditOut","trace","payload"})
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType (propOrder={"auditIn","auditOut","trace","payload"})
public class RequestDTO <T> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3240790333551537420L;
	
	@JsonProperty("payload")
    @JsonInclude(JsonInclude.Include.ALWAYS)
	@XmlElement (name ="payload")
	private T payload;
	
	@JsonProperty("auditIn")
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@XmlElement (name ="auditIn")
	private Boolean auditIn = Boolean.FALSE;
	
	@JsonProperty("auditOut")
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@XmlElement (name ="auditOut")
	private Boolean auditOut = Boolean.FALSE;
	
	@JsonProperty("trace")
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@XmlElement (name ="trace")
	private Boolean trace = Boolean.FALSE;
	
	public RequestDTO() 
	{
		super();
	}
	
	public RequestDTO(T payload) 
	{
		super();
		this.payload = payload;
	}
	
	public RequestDTO(T payload, RequestType type) 
	{
		this(payload);
		this.resolveRequestType(type);
	}
	
	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	public Boolean getAuditIn() {
		return auditIn;
	}

	public void setAuditIn(Boolean auditIn) {
		this.auditIn = auditIn;
	}

	public Boolean getAuditOut() {
		return auditOut;
	}

	public void setAuditOut(Boolean auditOut) {
		this.auditOut = auditOut;
	}

	public Boolean getTrace() {
		return trace;
	}

	/*public void setTrace(Boolean trace) {
		this.trace = trace;
	}*/

	private void resolveRequestType(RequestType type)
	{
		if (type == RequestType.AUDIT_IN )
		{
			this.auditIn = Boolean.TRUE;
			this.auditOut = Boolean.FALSE;
			this.trace = Boolean.FALSE;
		}
		else if (type == RequestType.AUDIT_AND_TRACE)
		{
			this.auditIn = Boolean.TRUE;
			this.auditOut = Boolean.TRUE;
			this.trace = Boolean.TRUE;
		}
	}
}
