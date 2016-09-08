package com.hpe.seur.core.lib.audit;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PayLoadType {

	XML ("XML"), 
	JSON ("JSON"),
	BINARY("BINARY"),
	PLAIN_TEXT ("PLAIN_TEXT");
	
	private String value;
	
	PayLoadType(String value)
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
