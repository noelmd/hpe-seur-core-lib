package com.hpe.seur.core.lib.audit.trace;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;

@XmlRootElement(name = "traceEvent")
public class TraceDTO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 289782460467124911L;
	
	@Id
	private String traceId;
	private Date timeStamp;
	private List <String> traceLog;
	
	public TraceDTO() 
	{
		super();
	}
	
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public List <String> getTraceLog() 
	{
		if (this.traceLog == null)
		{
			this.traceLog = new LinkedList<String> ();
		}
		
		return traceLog;
	}

	public void setTraceLog(List<String> traceLog) {
		this.traceLog = traceLog;
	}	
}
