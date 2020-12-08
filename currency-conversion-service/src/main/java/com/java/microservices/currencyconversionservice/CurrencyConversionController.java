package com.java.microservices.currencyconversionservice;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
//@RibbonClient(name = "exchangeservice")
@RequestMapping(path="/client")
public class CurrencyConversionController {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	
	@Autowired
	LoadBalancerClient lbClient;
		
	//@Autowired
	//private DiscoveryClient discoveryClient;
	
	@Bean
	@LoadBalanced
	RestTemplate createRestTemplate() {
		RestTemplateBuilder b = new RestTemplateBuilder();
		return b.build();
	}
	
	@Autowired
	@Lazy
	RestTemplate lbrestTemplate;
	
	//Using Rest Template for load bancer
	@RequestMapping(path="/currency-converter-load/from/{from}/to/{to}/quantity/{quantity}", method = RequestMethod.GET) //where {from} and {to} represents the column 
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)
	{
		LOG.info("CurrencyConversionController:convertCurrency()======>Start the method");
		
		//calling the currency exchange service
		//ResponseEntity<CurrencyConversionBean> responseEntity=new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);
		try {
			LOG.info(">>>>>>>>>>>.Using loadbancer<<<<<<<<<<<<<<<<<<<");
			//ServiceInstance serviceInstance=lbClient.choose("exchangeservice");
			//System.out.println("baseUrl----------------->"+serviceInstance.getUri());
			//LOG.info("loadbancerbaseUrl----------------->"+serviceInstance.getUri());
			//String baseUrl=serviceInstance.getUri().toString();
			
			//String baseUrl = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort();
			//baseUrl=baseUrl+"/currency-exchange/from/{from}/to/{to}";
			String baseUrl ="http://192.168.99.104:8000/service/currency-exchange/from/{from}/to/{to}";
			LOG.info("loadbancerbaseUrl----------------->"+baseUrl);
			
			Map<String, String> uriVariables=new HashMap<>();
			uriVariables.put("from", from);
			uriVariables.put("to", to);
			
			ResponseEntity<CurrencyConversionBean> responseEntity=new RestTemplate().getForEntity(baseUrl, CurrencyConversionBean.class, uriVariables);
			CurrencyConversionBean response=responseEntity.getBody();
			
			LOG.info("CurrencyConversionController:convertCurrency()======>End the method response load===>"+response);
			return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()), response.getPort());
			
			
		} catch (Exception e) {
			LOG.info("CurrencyConversionController:convertCurrency()======>Exception occurred the method=====>"+e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	
	//Mapping for currency-converter-feign service //@HystrixCommand(fallbackMethod="fallbackRetrieveConfigurations")  
	@RequestMapping(path ="/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}", method = RequestMethod.GET) //where {from} and {to} represents the column 
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)
	{
		LOG.info("CurrencyConversionController:convertCurrency()======>Start the method");
		LOG.info(">>>>>>>>>>>fetching data from server side by using feign client<<<<<<<<<<<<<<<<<<<");
		try {
			
			ResponseEntity<ExchangeValue> response =proxy.retrieveExchangeValue(from, to);
			LOG.info("CurrencyConversionController:retrieveExchangeValue()======>"+response);
			if(HttpStatus.NOT_FOUND.equals(response.getStatusCode() )){
				System.out.println("ExchangeValue - not valid !!");
				LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>ExchangeValue - not valid !!<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				ExchangeValue evalue = response.getBody();
				return new CurrencyConversionBean(evalue.getFrom());
			}
			ExchangeValue exvalue = response.getBody();
			System.out.println("Get ExchangeValue --->"+exvalue.toString());
			
			return new CurrencyConversionBean(exvalue.getId(), from, to, exvalue.getConversionMultiple(), quantity, quantity.multiply(exvalue.getConversionMultiple()), exvalue.getPort());
		
		} catch (Exception e) {
			
			System.out.println("CurrencyConversionController:retrieveExchangeValue( feign)======>Exception occurred the method=====>"+e.getMessage());
			LOG.info("CurrencyConversionController:retrieveExchangeValue( feign)======>Exception occurred the method=====>"+e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	/*
	public CurrencyConversionBean fallbackRetrieveConfigurations(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		LOG.info("CurrencyConversionController:fallbackRetrieveConfigurations======>Start the method for CIRCUIT BREAKER ENABLED.");
		System.out.println("CIRCUIT BREAKER ENABLED!!! No Response From Currency Exchange Service at this moment.  Service will be back shortly - " + new Date());
		
		String msg = "CIRCUIT BREAKER ENABLED!!! No Response From Currency Exchange Service at this moment.  Service will be back shortly - " + new Date();
		
		LOG.info("CurrencyConversionController:fallbackRetrieveConfigurations======>End the method for CIRCUIT BREAKER ENABLED.");
		return new CurrencyConversionBean(msg);
	}
	*/
	
	//Using Rest Template for load balancer
	@RequestMapping(path="/currency-converter-rest/from/{from}/to/{to}/quantity/{quantity}", method = RequestMethod.GET) //where {from} and {to} represents the column 
	public CurrencyConversionBean convertCurrencyRest(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)
	{
		LOG.info("CurrencyConversionController:convertCurrencyRest()======>Start the method");
		
		try {
			
			Map<String, String> uriVariables=new HashMap<>();
			uriVariables.put("from", from);
			uriVariables.put("to", to);
			String baseUrl = "http://exchangeservice/service/currency-exchange/from/{from}/to/{to}";
			
			LOG.info("convertCurrencyRest baseUrl============>"+baseUrl);
			//calling the currency exchange service
			ResponseEntity<CurrencyConversionBean> responseEntity=lbrestTemplate.getForEntity(baseUrl, CurrencyConversionBean.class, uriVariables);
			
			LOG.info("CurrencyConversionController:convertCurrencyRest() responseEntity======>"+responseEntity.getBody());
			
			CurrencyConversionBean response=responseEntity.getBody();
			
			LOG.info("CurrencyConversionController:convertCurrencyRest()======>End the method");
			
			return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()), response.getPort());
		
		} catch (Exception e) {
			LOG.info("CurrencyConversionController:retrieveExchangeValue( rest)======>Exception occurred the method=====>"+e.getMessage());
			e.printStackTrace();
			return null;
		}

	}
	
	
	@RequestMapping(path="/currency-converter-rest1/from/{from}/to/{to}/quantity/{quantity}", method = RequestMethod.GET) //where {from} and {to} represents the column 
	public CurrencyConversionBean convertCurrencyRest1(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)
	{
		LOG.info("CurrencyConversionController:convertCurrencyRest()======>Start the method");
		
		try {
			
			Map<String, String> uriVariables=new HashMap<>();
			uriVariables.put("from", from);
			uriVariables.put("to", to);
			String baseUrl = "http://exchangeservice:8000/service/currency-exchange/from/{from}/to/{to}";
			
			LOG.info("convertCurrencyRest baseUrl============>"+baseUrl);
			//calling the currency exchange service
			ResponseEntity<CurrencyConversionBean> responseEntity=lbrestTemplate.getForEntity(baseUrl, CurrencyConversionBean.class, uriVariables);
			
			LOG.info("CurrencyConversionController:convertCurrencyRest() responseEntity======>"+responseEntity.getBody());
			
			CurrencyConversionBean response=responseEntity.getBody();
			
			LOG.info("CurrencyConversionController:convertCurrencyRest()======>End the method");
			
			return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()), response.getPort());
		
		} catch (Exception e) {
			LOG.info("CurrencyConversionController:retrieveExchangeValue( rest)======>Exception occurred the method=====>"+e.getMessage());
			e.printStackTrace();
			return null;
		}

	}
	
}