package com.hpe.seur.core.lib.marshalling;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MarshallManager 
{	
	private final static Logger LOGGER = LoggerFactory.getLogger(MarshallManager.class);
	  
	public static String getObjectMarshallingXML(Object object)
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(object.getClass());
			StringWriter sw = new StringWriter();
			Marshaller marshaller = jc.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.marshal(object, sw);
	        return sw.toString();
		}
		catch(Exception e)
		{
			LOGGER.error("### Marshalling to XML exception: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error while converting bean to XML string.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getXMLStringUnmarshalling(Class<?> objectClass, String xml) 
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(objectClass);
			Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
			return (T) jaxbUnmarshaller.unmarshal(new StringReader(xml));
		}
		catch (Exception e)
		{
			LOGGER.error("### Unmarshalling from XML exception: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error while converting XML to object.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getXMLFileUnmarshalling(Class<?> objectClass, File xml) 
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(objectClass);
			Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
			return (T) jaxbUnmarshaller.unmarshal(xml);
		}
		catch (Exception e){
			LOGGER.error("### Unmarshalling from XML exception: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error while converting XML to object.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getElementUnmarshalling(Class<?> objectClass, Element element)
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(objectClass);
			Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
			return (T) jaxbUnmarshaller.unmarshal(element);
		}
		catch (Exception e)
		{
			LOGGER.error("### Unmarshalling from Element exception: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error while converting Element to object.", e);
		}	
	}
	
	public static String getObjectMarshallingJSON(Object object)
	{
		return MarshallManager.getObjectMarshallingJSON(object, false);
	}
	
	/* With wrapMainClass = true, the main class of the object that wraps the JsonProperties will be Marshalled, too */
	public static String getObjectMarshallingJSON(Object object, boolean wrapMainClass)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			
			if (wrapMainClass == true)
			{
				mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
			}
			
			return mapper.writeValueAsString(object);
		}
		catch(Exception e)
		{
			LOGGER.error("### Marshalling to JSON exception: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error while converting bean to JSON string.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getJSONStringUnmarshalling(Class<?> objectClass, String json) 
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			return (T) mapper.readValue(json, objectClass);
		}
		catch (Exception e)
		{
//			LOGGER.error("### Unmarshalling from JSON exception: " + e.getMessage());
//			e.printStackTrace();
			throw new RuntimeException("Error while converting JSON to object.", e);
		}
	}
	
	private static JsonNode lookUpJSONProperty(String property, String json) 
	{	
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode node = null;
		
		try 
		{
			JsonNode rootNode = objectMapper.readTree(json);
			
			 //node = rootNode.path(property);
			 node = rootNode.findValue(property);
		}
		catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return node;
	}
	
	public static String lookUpJSONEntry(String property, String json)
	{
		return MarshallManager.lookUpJSONProperty(property, json).toString();
	}
	
	public static String lookUpJSONValue(String property, String json)
	{
		return MarshallManager.lookUpJSONProperty(property, json).asText();
	}
	
	public static String getObjectWithPropertyName (String propertyName, String propertyValue)
	{
		return "{" + "\"" + propertyName + "\": " +  propertyValue + "}";
	}
}
