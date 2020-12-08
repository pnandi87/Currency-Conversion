package com.java.microservices.currencyconversionservice;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

//Enabling feign
//@FeignClient(name="currency-exchange-service", url="localhost:8000")
//enabling ribbon
//@RibbonClient(name="currency-exchange-service", configuration = RibbonConfiguration.class)
//@RibbonClient(name = "exchangeservice")
//@FeignClient(name="exchangeservice")

@Service
public class CurrencyExchangeServiceProxy {
	
	final CurrencyCoversionClient currencyCovClient;
	public CurrencyExchangeServiceProxy(CurrencyCoversionClient currencyCovClient) {
		
		this.currencyCovClient=currencyCovClient;
	}
		
	
	@HystrixCommand(fallbackMethod="fallbackRetrieveConfigurations") 
	public ResponseEntity<ExchangeValue> retrieveExchangeValue(String from, String to) {
		
		ResponseEntity<ExchangeValue> exvalue = currencyCovClient.retrieveExchangeValue(from,to);
		
		return exvalue;
	
	}
	
	public ResponseEntity<ExchangeValue> fallbackRetrieveConfigurations( String from, String to) {
		
		
		//LOG.info("CurrencyConversionController:fallbackRetrieveConfigurations======>Start the method for CIRCUIT BREAKER ENABLED.");
		System.out.println("CIRCUIT BREAKER ENABLED!!! No Response From Currency Exchange Service at this moment.  Service will be back shortly - " + new Date());
		
		String msg = "CIRCUIT BREAKER ENABLED!!! No Response From Currency Exchange Service at this moment.  Service will be back shortly - " + new Date();
		
		//LOG.info("CurrencyConversionController:fallbackRetrieveConfigurations======>End the method for CIRCUIT BREAKER ENABLED.");
		//return 0.0;
		ExchangeValue evalue = new ExchangeValue(msg);
		return new ResponseEntity<ExchangeValue>(evalue,HttpStatus.NOT_FOUND);
	}
	
	
	
	//@RequestMapping(method = RequestMethod.GET, path="/currency-exchange/from/{from}/to/{to}")
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")		//where {from} and {to} are path variable
	//public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to); //from map to USD and to map to INR
	 


	 
}