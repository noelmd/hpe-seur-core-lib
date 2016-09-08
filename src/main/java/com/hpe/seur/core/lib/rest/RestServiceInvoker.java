package com.hpe.seur.core.lib.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestServiceInvoker 
{
	private String serviceUrl;
	
	//@Autowired       
	protected RestTemplate restTemplate; 
	
	//@Autowired
	/*public RestServiceInvoker(String serviceUrl) 
	{
		super();
	}*/
	
	public RestServiceInvoker() 
	{
		super();
		
		this.restTemplate = (this.restTemplate == null) ? new RestTemplate() : restTemplate;
		
		this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		
		HttpMessageConverter<?> formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter<?> stringHttpMessageConverternew = new StringHttpMessageConverter();
        HttpMessageConverter<?> jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter ();
        
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        
        converters.add(formHttpMessageConverter);
        converters.add(stringHttpMessageConverternew);
        converters.add(jsonHttpMessageConverter);
        
        restTemplate.setMessageConverters(converters);
	}
	
	public < T > T  postInvocation( Object postParam, Class <?> response)
	{
		return (T) this.restTemplate.postForObject(this.serviceUrl, postParam, response);
	}
	
	public String getServiceUrl() 
	{
		return this.serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) 
	{
		this.serviceUrl = serviceUrl;
	}
}
