package com.java.microservices.currencyconversionservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name="exchangeservice")
public interface CurrencyCoversionClient {
	
	@GetMapping("/service/currency-exchange/from/{from}/to/{to}")
	public ResponseEntity<ExchangeValue> retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);
	
}
