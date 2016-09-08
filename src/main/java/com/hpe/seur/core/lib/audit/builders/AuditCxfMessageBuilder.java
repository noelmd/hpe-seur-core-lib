package com.hpe.seur.core.lib.audit.builders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.hpe.seur.core.lib.audit.AuditDTO;
import com.hpe.seur.core.lib.audit.AuditType;

public class AuditCxfMessageBuilder implements AuditMessageBuilder
{
	private static HttpServletRequest request;
	private static HttpServletResponse response;
	
	private Message message;
	private AuditType auditType;
	private AbstractPhaseInterceptor phase;
	
	public AuditCxfMessageBuilder(Message message, AuditType auditType) 
	{	
		this.message = message;
		this.auditType = auditType;
	}
	
	public AuditCxfMessageBuilder(Message message, AbstractPhaseInterceptor phase) 
	{	
		this.message = message;
		this.phase = phase;
	}
	
	@Override
	public AuditDTO getAuditDTO ()
	{
		AuditDTO auditDTO = null;
		
		if (this.phase != null)
		{
			if (phase.getPhase().equalsIgnoreCase(Phase.RECEIVE))
			{
				auditDTO = this.getAuditRequestDTO(message);
			}
			else //if (interceptor.getPhase().equalsIgnoreCase(Phase.SEND) || interceptor.getPhase().equalsIgnoreCase(Phase.PRE_STREAM))
			{
				auditDTO = this.getAuditResponseDTO(message);
			}
		}
		else 
		{
			if (this.auditType == AuditType.REQUEST)
			{
				auditDTO = this.getAuditRequestDTO(this.message);
			}
			else if (this.auditType == AuditType.RESPONSE)
			{
				auditDTO = this.getAuditResponseDTO(this.message);
			}
		}
		
		return auditDTO;
	}

	//@Bean
	private AuditDTO getAuditRequestDTO(Message message)
	{
		AuditCxfMessageBuilder.request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
		//response = (HttpServletResponse)message.get(AbstractHTTPDestination.HTTP_RESPONSE);

		AuditDTO auditDTO = new AuditDTO();

		auditDTO.setAuditId(AuditCxfMessageBuilder.generateAuditId());
		auditDTO.setTimestamp(AuditCxfMessageBuilder.generateTimestamp());
		auditDTO.setType(AuditCxfMessageBuilder.generateRequestAuditType());

		auditDTO.setContentLenght(AuditCxfMessageBuilder.generateContentLenght());
		auditDTO.setEncoding(AuditCxfMessageBuilder.generateRequestEncoding());
		auditDTO.setMethod(AuditCxfMessageBuilder.generateMethod());
		auditDTO.setRemoteAddress(AuditCxfMessageBuilder.generateRemoteAddress());
		auditDTO.setRemoteUser(AuditCxfMessageBuilder.generateRemoteUser());
		auditDTO.setUri(AuditCxfMessageBuilder.generateUri());
		auditDTO.setUserAgent(AuditCxfMessageBuilder.generateRequestUserAgent());
		auditDTO.setLocale(AuditCxfMessageBuilder.generateRequestLocale());

		auditDTO.setContextualProperties(AuditCxfMessageBuilder.generateContextualProperties(message));
		auditDTO.setHeaders(AuditCxfMessageBuilder.generateHeaders(message));

		//Add AuditId in the Exchange
		AuditCxfMessageBuilder.addAuditMark(AuditCxfMessageBuilder.AUDIT_REQUEST_ID, auditDTO.getAuditId(), message);

		auditDTO.setAttachmentHeaders(AuditCxfMessageBuilder.generateAttachmentHeaders(message));

		auditDTO.setPayload(AuditCxfMessageBuilder.generateRequestPayload(message));

		return auditDTO;
	}

	private AuditDTO getAuditResponseDTO(Message message)
	{
		//request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
		AuditCxfMessageBuilder.response = (HttpServletResponse)message.get(AbstractHTTPDestination.HTTP_RESPONSE);

		AuditDTO auditDTO = new AuditDTO();

		auditDTO.setAuditId(AuditCxfMessageBuilder.generateAuditId());
		auditDTO.setTimestamp(AuditCxfMessageBuilder.generateTimestamp());
		auditDTO.setType(AuditCxfMessageBuilder.generateResponseAuditType());

		auditDTO.setEncoding(AuditCxfMessageBuilder.generateResponseEncoding());
		auditDTO.setUserAgent(AuditCxfMessageBuilder.generateResponseUserAgent());
		auditDTO.setLocale(AuditCxfMessageBuilder.generateResponseLocale());

		auditDTO.setStatus(AuditCxfMessageBuilder.generateStatus());

		auditDTO.setContextualProperties(AuditCxfMessageBuilder.generateContextualProperties(message));
		auditDTO.setHeaders(AuditCxfMessageBuilder.generateHeaders(message));

		//Add AuditId in the Exchange
		AuditCxfMessageBuilder.addAuditMark(AuditCxfMessageBuilder.AUDIT_RESPONSE_ID, auditDTO.getAuditId(), message);

		auditDTO.setAttachmentHeaders(AuditCxfMessageBuilder.generateAttachmentHeaders(message));

		auditDTO.setPayload(AuditCxfMessageBuilder.generateResponsePayload(message));

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

	/*private static AuditType generateDefaultAuditType ()
		{
			return AuditType.GENERIC;
		}*/

	private static AuditType generateRequestAuditType ()
	{
		return AuditType.REQUEST;
	}

	private static AuditType generateResponseAuditType ()
	{
		return AuditType.RESPONSE;
	}

	private static String generateUri()
	{
		return AuditCxfMessageBuilder.request.getRequestURI();
	}

	private static String generateRequestEncoding()
	{
		return AuditCxfMessageBuilder.request.getCharacterEncoding();
	}

	private static String generateResponseEncoding()
	{
		return AuditCxfMessageBuilder.response.getCharacterEncoding();
	}

	private static String generateMethod()
	{
		return AuditCxfMessageBuilder.request.getMethod();
	}

	private static String generateRemoteUser()
	{
		String remoteUser = AuditCxfMessageBuilder.request.getRemoteUser();

		if (remoteUser == null || remoteUser.equals(""))
		{
			remoteUser = AuditMessageBuilder.UNKNOWN_USER;
		}

		return remoteUser;
	}

	private static String generateRemoteAddress()
	{
		return AuditCxfMessageBuilder.request.getRemoteAddr();
	}

	private static String generateRequestUserAgent()
	{
		return AuditCxfMessageBuilder.request.getHeader(AuditCxfMessageBuilder.HEADER_USER_AGENT);
	}

	private static String generateResponseUserAgent()
	{
		return AuditCxfMessageBuilder.response.getHeader(AuditCxfMessageBuilder.HEADER_USER_AGENT);
	}

	private static Long generateContentLenght()
	{
		return AuditCxfMessageBuilder.request.getContentLengthLong();
	}

	private static String generateRequestLocale()
	{
		Locale locale = AuditCxfMessageBuilder.request.getLocale();

		return locale.toString();
	}

	private static String generateResponseLocale()
	{
		Locale locale = AuditCxfMessageBuilder.response.getLocale();

		return locale.toString();
	}

	private static Integer generateStatus()
	{
		return AuditCxfMessageBuilder.response.getStatus();
	}

	private static Map <String , String > generateContextualProperties(Message message)
	{
		Map  <String , String > contextualPropertiesMap = new HashMap <String , String > ();

		//Map<String, String> contextualPropertiesMap = CastUtils.cast((Map<?, ?>)message.get(Message.PRP);

		/*
			System.out.println("Contextual Properties........");*/
		if ( message != null && !message.isEmpty())
		{    		
			try
			{
				Set <String> contextualPropertiesKeys = message.getContextualPropertyKeys();

				if (contextualPropertiesKeys != null && !contextualPropertiesKeys.isEmpty())
				{
					for (String contextualPropertyKey : contextualPropertiesKeys)
					{
						Object contextualPropertyValue = message.getContextualProperty(contextualPropertyKey);

						if (contextualPropertyValue != null)
						{
							//System.out.println("Contextual Property: " + contextualPropertyKey + " value: " + contextualPropertyValue.toString());

							contextualPropertiesMap.put(contextualPropertyKey, contextualPropertyValue.toString());
						}
					}
				}
				else
				{
					System.out.println("The ContextualPropertiesKeys Set is empty!!!");

					contextualPropertiesMap = null;
				}
			}
			catch (NullPointerException npexception)
			{
				System.out.println("No exist for this kind of message a ContextualPropertiesKeys Set!!!");

				contextualPropertiesMap = null;
			}
		}

		return contextualPropertiesMap;
	}

	private static Map <String, List <String>> generateHeaders(Message message)
	{
		Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>)message.get(Message.PROTOCOL_HEADERS));

		return headers;
	}

	private static Map <String, Map <String, String>> generateAttachmentHeaders(Message message)
	{
		Collection <Attachment> attachments = message.getAttachments();

		Map <String, Map <String, String>> attachmentMap = null;

		if (attachments != null && !attachments.isEmpty())
		{
			attachmentMap = new HashMap <String, Map <String, String>> ();

			for (Attachment attachment : attachments)
			{
				String attachementId = attachment.getDataHandler().getName();

				Iterator <String> it = attachment.getHeaderNames();

				Map <String, String> headerMap = new HashMap <String, String> ();

				while (it.hasNext())
				{
					String headerName = it.next();

					String headerValue = attachment.getHeader(headerName);

					headerMap.put(headerName, headerValue);
				}

				attachmentMap.put(attachementId, headerMap);
			}
		}

		return attachmentMap;
	}

	private static String generateRequestPayload (Message message)
	{
		System.out.println("Generating Request Payload");

		InputStream is = (InputStream) message.getContent(InputStream.class);

		//System.out.println("InputStream: " + is );

		CachedOutputStream cos = new CachedOutputStream();

		String payload = "";

		try 
		{
			IOUtils.copy (is,cos);
			cos.flush ();

			message.setContent (InputStream.class, cos.getInputStream());

			is.close ();

			System.out.println("CachedOutputStream: " + cos );

			payload = IOUtils.toString(cos.getInputStream());

			cos.close ();
		} 
		catch (IOException ioException) 
		{
			System.out.println("Error generating payload");
			ioException.printStackTrace();
		}

		return payload;
	}

	private static String generateResponsePayload (Message message)
	{
		System.out.println("Generating Response Payload");

		boolean isOutbound = message == message.getExchange().getOutMessage();

		StringBuilder responsePayload = new StringBuilder();

		if (isOutbound)
		{
			OutputStream os = message.getContent(OutputStream.class);

			final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);

			message.setContent(OutputStream.class, newOut);

			message.getInterceptorChain().doIntercept(message);

			CachedOutputStream cos = (CachedOutputStream) newOut;

			System.out.println("CachedOutputStream: " + cos);

			try 
			{
				cos.writeCacheTo(responsePayload);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}

		return responsePayload.toString();
	}

	//Type Request or Response, value es el AuditDTOId
	private static void addAuditMark(String type, String value, Message message)
	{
		message.getExchange().put(type, value);

		System.out.println("Putting into exhange: " + type + " - " + value);
	}
}
