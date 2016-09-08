package com.hpe.seur.core.lib.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestType {

	AUDIT_IN ("AUDIT_IN"), 
	AUDIT_AND_TRACE ("AUDIT_AND_TRACE");
	
	private String value;
	
	RequestType(String value)
	{
		this.value = value;
	}
	
	@JsonValue
	public String getValue()
	{
		return this.value;
	}
	
	public static RequestType get(String internalValue)
	{
		for(RequestType enumValue : RequestType.values())
		{
			if(enumValue.getValue().equalsIgnoreCase(internalValue))
			{
				return enumValue;
			}
		}
		return null;
	}
}