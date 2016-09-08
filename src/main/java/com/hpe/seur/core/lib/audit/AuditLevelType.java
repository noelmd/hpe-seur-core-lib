package com.hpe.seur.core.lib.audit;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AuditLevelType {

	AUDIT_IN ("AUDIT_IN"), 
	AUDIT_IN_OUT ("AUDIT_IN_OUT"),
	TRACE ("TRACE");
	
	private String value;
	
	AuditLevelType(String value)
	{
		this.value = value;
	}
	
	@JsonValue
	public String getValue()
	{
		return this.value;
	}
	
	public static AuditLevelType get(String internalValue)
	{
		for(AuditLevelType enumValue : AuditLevelType.values())
		{
			if(enumValue.getValue().equalsIgnoreCase(internalValue))
			{
				return enumValue;
			}
		}
		return null;
	}
}
