package com.hpe.seur.core.lib.audit;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AuditType {

	REQUEST ("REQUEST"), 
	RESPONSE ("RESPONSE"),
	GENERIC ("GENERIC");
	
	private String value;
	
	AuditType(String value)
	{
		this.value = value;
	}
	
	@JsonValue
	public String getValue()
	{
		return this.value;
	}
	
	public static AuditType get(String internalValue)
	{
		for(AuditType enumValue : AuditType.values())
		{
			if(enumValue.getValue().equalsIgnoreCase(internalValue))
			{
				return enumValue;
			}
		}
		return null;
	}
}
