package com.hpe.seur.core.lib.audit.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.cxf.helpers.CastUtils;

import com.hpe.seur.core.lib.audit.AuditDTO;
import com.hpe.seur.core.lib.audit.AuditType;

public class AuditCamelMessageBuilder implements AuditMessageBuilder 
{
	private  HttpServletRequest request;
	private  HttpServletResponse response;
	
	private  Message message; 
	private  AuditType auditType;
	
	public AuditCamelMessageBuilder(Message message, AuditType auditType) 
	{
		this.message = message;
		this.auditType = auditType;
	}
	
	@Override
	public AuditDTO getAuditDTO ()
	{
		AuditDTO auditDTO = null;
		
		if (auditType == AuditType.REQUEST)
		{
			auditDTO = this.getAuditRequestDTO(message);
		}
		else if (auditType == AuditType.RESPONSE)
		{
			auditDTO = this.getAuditResponseDTO(message);
		}
			
		return auditDTO;
	}
	
	private AuditDTO getAuditRequestDTO(Message message)
	{
		//this.request = (HttpServletRequest)message.getBody(HttpServletRequest.class);
		
		this.request = (HttpServletRequest) message.getHeader(Exchange.HTTP_SERVLET_REQUEST);
		
		if(this.request == null)
		{
			System.out.println("!!!!!!!!!!!!!!!!!!!!! Request Empty !!!!!!!!!!!!");
		}
		
		AuditDTO auditDTO = new AuditDTO();
		
		auditDTO.setAuditId(AuditCamelMessageBuilder.generateAuditId());
		auditDTO.setTimestamp(AuditCamelMessageBuilder.generateTimestamp());
		auditDTO.setType(AuditCamelMessageBuilder.generateRequestAuditType());
		
		auditDTO.setContentLenght(this.generateContentLenght());
		auditDTO.setEncoding(this.generateRequestEncoding());
		auditDTO.setMethod(this.generateMethod());
		auditDTO.setRemoteAddress(this.generateRemoteAddress());
		auditDTO.setRemoteUser(this.generateRemoteUser());
		auditDTO.setUri(this.generateUri());
		auditDTO.setUserAgent(this.generateRequestUserAgent());
		auditDTO.setLocale(this.generateRequestLocale());
		
		auditDTO.setContextualProperties(this.generateContextualProperties(message));
		auditDTO.setHeaders(this.generateHeaders(message));
		
		//Add AuditId in the Exchange
		AuditCamelMessageBuilder.addAuditMark(AuditMessageBuilder.AUDIT_REQUEST_ID, auditDTO.getAuditId(), message);
						
		auditDTO.setAttachmentHeaders(this.generateAttachmentHeaders(message));
		
		auditDTO.setPayload(this.generateRequestPayload(message));
		
		return auditDTO;
	}
	
	private AuditDTO getAuditResponseDTO(Message message)
	{
		//this.response = (HttpServletResponse)message.getBody(HttpServletResponse.class);
		
		this.response = (HttpServletResponse)message.getHeader(Exchange.HTTP_SERVLET_RESPONSE);
		
		if (this.response == null)
		{
			System.out.println("!!!!!!!!!!!!!!!!!!!!! Response Empty !!!!!!!!!!!!");
		}
		
		AuditDTO auditDTO = new AuditDTO();
		
		auditDTO.setAuditId(AuditCamelMessageBuilder.generateAuditId());
		auditDTO.setTimestamp(AuditCamelMessageBuilder.generateTimestamp());
		auditDTO.setType(AuditCamelMessageBuilder.generateResponseAuditType());
		
		auditDTO.setEncoding(this.generateResponseEncoding());
		auditDTO.setUserAgent(this.generateResponseUserAgent());
		auditDTO.setLocale(this.generateResponseLocale());
		
		auditDTO.setStatus(this.generateStatus());
		
		auditDTO.setContextualProperties(this.generateContextualProperties(message));
		auditDTO.setHeaders(this.generateHeaders(message));
		
		//Add AuditId in the Exchange
		AuditCamelMessageBuilder.addAuditMark(AuditMessageBuilder.AUDIT_RESPONSE_ID, auditDTO.getAuditId(), message);
					
		auditDTO.setAttachmentHeaders(this.generateAttachmentHeaders(message));
	
		auditDTO.setPayload(AuditCamelMessageBuilder.generateResponsePayload(message));
	
		return auditDTO;
	}
	
	//private Methods
	
	private static String generateAuditId ()
	{
		Date currentDate = new Date();
		String uuid = UUID.randomUUID().toString() + "-" + currentDate.toString();
		
		return uuid;
	}
	
	private static Date generateTimestamp ()
	{
		Date currentDate = new Date();
		
		return currentDate;
	}
	
	private static AuditType generateRequestAuditType ()
	{
		return AuditType.REQUEST;
	}
	
	private static AuditType generateResponseAuditType ()
	{
		return AuditType.RESPONSE;
	}
	
	private  String generateUri()
	{
		String requestUri = "";
		
		if (this.request != null)
		{
			requestUri = this.request.getRequestURI();
		}
		else
		{
			requestUri = (String) this.message.getHeader(Exchange.HTTP_URL);
		}
		return requestUri;
	}
	
	private String generateRequestEncoding()
	{
		String encondig = "";
		
		if (this.request != null)
		{
			this.request.getCharacterEncoding();
		}
		else
		{
			encondig = (String) this.message.getHeader(Exchange.HTTP_CHARACTER_ENCODING);
		}
			
		return encondig;
	}
	
	private String generateResponseEncoding()
	{
		String encondig = (String) this.message.getHeader(Exchange.HTTP_CHARACTER_ENCODING);
		
		return encondig;
	}
	
	private String generateMethod()
	{
		String method = "";
		
		if (this.request != null)
		{
			method = this.request.getMethod();
		}
		else
		{
			method = (String) this.message.getHeader(Exchange.HTTP_METHOD);
		}
				 
		return method;
	}
	
	private String generateRemoteUser()
	{
		String remoteUser = AuditMessageBuilder.UNKNOWN_USER;
		
		if (this.request != null)
		{
			remoteUser = this.request.getRemoteUser();
		}
		else
		{
			remoteUser = (String) this.message.getHeader("X-Forwarded-For");
		}
		
		return remoteUser;
	}
	
	private String generateRemoteAddress()
	{
		String remoteAddr = "";
		
		if (this.request != null)
		{
			remoteAddr = this.request.getRemoteAddr();
		}
		else
		{
			remoteAddr = (String) this.message.getHeader("HTTP_X_FORWARDED_FOR");
		}
		
		return remoteAddr;
	}
	
	private String generateRequestUserAgent()
	{
		String userAgent;
		
		if (request != null)
		{
			userAgent = this.request.getHeader(AuditMessageBuilder.HEADER_USER_AGENT);
		}
		else
		{
			userAgent = (String) this.message.getHeader(AuditMessageBuilder.HEADER_USER_AGENT);
		}
		
		return userAgent;
	}
	
	private String generateResponseUserAgent()
	{
		String userAgent;
		
		if (response != null)
		{
			userAgent = this.response.getHeader(AuditMessageBuilder.HEADER_USER_AGENT);
		}
		else
		{
			userAgent = (String) this.message.getHeader(AuditMessageBuilder.HEADER_USER_AGENT);
		}
		
		return userAgent;
	}
	
	private Long generateContentLenght()
	{
		Long contentLenght = 0L;
		
		if (this.request != null)
		{
			contentLenght = this.request.getContentLengthLong();
		}
		else
		{
			contentLenght = (Long) this.message.getHeader("content-length");
		}
		
		return contentLenght;
	}
	
	private String generateRequestLocale()
	{
		String localeString = ""; 
		
		if (this.request != null)
		{
			Locale locale = this.request.getLocale();
			localeString = locale.toString();
		}
		else
		{
			localeString = (String) this.message.getHeader("Accept-Language");
		}
		
		return localeString;
	}
	
	private String generateResponseLocale()
	{
		String locale;
		
		if (this.response != null)
		{
			Locale localeObject = this.response.getLocale();
			
			locale = localeObject.toString();
		}
		else
		{
			locale = "UNKNOWN_LOCALE";
		}
		
		return locale;
	}
	
	private Integer generateStatus()
	{
		Integer status;
		
		if (this.response != null)
		{
			status =  this.response.getStatus();
		}
		else
		{
			status = (Integer)this.message.getHeader(Exchange.HTTP_RESPONSE_CODE);
		}
		
		return status;
	}
	

	private Map <String,String> generateContextualProperties(Message message)
	{
		Map  <String,String> contextualPropertiesMap = new HashMap <String , String > ();
		
    	if ( message != null && message.getBody() != null)
    	{    		
    		Map <String, Object> mapProperties = message.getExchange().getProperties();
    		
    		if (mapProperties != null && !mapProperties.isEmpty())
    		{		
	    		try
	    		{
		    		Set <String> propertyKeys = mapProperties.keySet();
			    	
			    	if (propertyKeys != null && !propertyKeys.isEmpty())
			    	{
				    	for (String propertyKey : propertyKeys)
				    	{
				    		Object propertyValue = mapProperties.get(propertyKey);
				    		
				    		if (propertyValue != null)
				    		{
				    			//System.out.println("Contextual Property: " + contextualPropertyKey + " value: " + contextualPropertyValue.toString());
				    			
				    			contextualPropertiesMap.put(propertyKey, propertyValue.toString());
				    		}
				    	}
			    	}
			    	else
			    	{
			    		System.out.println("The ContextualPropertiesKeys Set is empty!!!");
			    		
			    		contextualPropertiesMap = null;
			    	}
	    		}
	    		catch (NullPointerException npException)
	    		{
	    			System.out.println("No exist for this kind of message a ContextualPropertiesKeys Set!!!");
		    		
		    		contextualPropertiesMap = null;
	    		}
    		}
    	}
    	
    	return contextualPropertiesMap;
	}
		
	private Map <String, List <String>> generateHeaders(Message message)
	{
		//Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>) message.getHeaders());
        
		Map <String, List <String>> headerMap = null;
		
		if (message != null && !message.getHeaders().isEmpty())
		{
			headerMap = new HashMap <String, List <String>>();
			
			Set <String> keySet = message.getHeaders().keySet();
			
			if (keySet != null && !keySet.isEmpty())
			{
				for (String key: keySet)
				{
					//System.out.println("Header Name: " + key);
					
					Object headerObject = message.getHeaders().get(key);
					
					if (headerObject != null)
					{
						List <String> listValue = new ArrayList <String>();
						
						listValue.add(headerObject.toString());
						
						headerMap.put(key,listValue);
					}
				}
			}
		}
		
		return headerMap;
	}
	
	private Map <String, Map <String, String>> generateAttachmentHeaders(org.apache.camel.Message message)
	{
		Map <String, Map <String, String>> attachmentMap = null;
		
		return attachmentMap;
	}
	
	private String generateRequestPayload (org.apache.camel.Message message)
    {
		System.out.println("Generating Request Payload");
		
		String response = "";
		
		if (message != null && message.getBody() != null)
		{
			response = message.getBody().toString();
		}
		
		return response; 
    }
		
	private static String generateResponsePayload (Message message)
    {
		System.out.println("Generating Response Payload");
				
		String response = "";
		
		if (message != null && message.getBody() != null)
		{
			response = message.getBody().toString();
		}
		
		return response; 
	}
	
	private static void addAuditMark(String type, String value, Message message)
	{
		message.getExchange().getProperties().put(type, value);
		
		System.out.println("Putting into exhange: " + type + " - " + value);
	}
	
	public AuditDTO getMockAuditDTO (Message message, AuditType auditType )
	{
		AuditDTO auditDTO = null;
		
		if (auditType == AuditType.REQUEST)
		{
			auditDTO = this.getAuditRequestDTO(message);
		}
		else if (auditType == AuditType.RESPONSE)
		{
			auditDTO = this.getAuditResponseDTO(message);
		}
			
		return auditDTO;
	}
}
