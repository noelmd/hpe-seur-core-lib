package com.hpe.seur.core.lib.audit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;

@XmlRootElement(name = "auditEvent")
public class AuditDTO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8756487219136489407L;
	
	@Id
	private String auditId;
	
	private Date timestamp;
	
	private String uri;
	
	private String encoding;
	
	private String method;
	
	private String remoteUser;
	private String remoteAddress;
	private String userAgent;
	
	private Long contentLenght;
	
	private String locale;
	
	private String payloadType;
	private String payload;
	
	private Integer status;
	
	private AuditType type;
	
	private Map <String, List <String>> headers;
	
	private Map <String, String> contextualProperties;
	
	//private Map <String, String> attachmentHeaders;
	
	private Map <String, Map <String, String>> attachmentHeaders;
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getRemoteUser() {
		return remoteUser;
	}

	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Long getContentLenght() {
		return contentLenght;
	}

	public void setContentLenght(Long contentLenght) {
		this.contentLenght = contentLenght;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public AuditType getType() {
		return type;
	}

	public void setType(AuditType type) {
		this.type = type;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public Map<String, String> getContextualProperties() {
		return contextualProperties;
	}

	public void setContextualProperties(Map<String, String> contextualProperties) {
		this.contextualProperties = contextualProperties;
	}

	public Map<String, Map<String, String>> getAttachmentHeaders() {
		return attachmentHeaders;
	}

	public void setAttachmentHeaders(Map<String, Map<String, String>> attachmentHeaders) {
		this.attachmentHeaders = attachmentHeaders;
	}
}
