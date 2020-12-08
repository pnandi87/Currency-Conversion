package com.java.microservices.currencyexchangeservice;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController 
@RequestMapping(path="/service")
public class CurrencyExchangeController 
{
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private ExchangeValueRepository repository;
	
	/*
	@RequestMapping(path="/currency-exchange/from/{from}/to/{to}", method = RequestMethod.GET)		//where {from} and {to} are path variable
	public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to)   //from map to USD and to map to INR
	{	
		LOG.info("CurrencyExchangeController:retrieveExchangeValue()======>Start the method");
		ExchangeValue exchangeValue = repository.findByFromAndTo(from, to);
		//setting the port
		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		LOG.info("CurrencyExchangeController:retrieveExchangeValue()=======>DATA fetched from H2 DataBase " + exchangeValue);
		return exchangeValue;
	}
	*/
	
	@RequestMapping(path="/currency-exchange/from/{from}/to/{to}", method = RequestMethod.GET, produces = "application/json")		//where {from} and {to} are path variable
	public ResponseEntity<ExchangeValue> retrieveExchangeValue(@PathVariable String from, @PathVariable String to)   //from map to USD and to map to INR
	{	
		LOG.info("CurrencyExchangeController:retrieveExchangeValue()======>Start the method");
		ExchangeValue exchangeValue = repository.findByFromAndTo(from, to);
		//setting the port
		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		LOG.info("CurrencyExchangeController:retrieveExchangeValue()=======>DATA fetched from H2 DataBase " + exchangeValue.toString());
		//return exchangeValue;
		return new ResponseEntity<ExchangeValue>(exchangeValue, exchangeValue == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
	

	
	@RequestMapping(path="/add/id/{id}/from/{from}/to/{to}/conversionMultiple/{conversionMultiple}", method = RequestMethod.POST)
	public ExchangeValue addConversionFactor(@PathVariable Long id,@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal conversionMultiple) {
		System.out.println(">>>>>>>>>>>>addConversionFactor <<<<<<<<<<<");	
		LOG.info("CurrencyExchangeController:addConversionFactor()======>Start the method");
		ExchangeValue ev = new ExchangeValue();
		ev.setId(id);
		ev.setFrom(from);
		ev.setTo(to);
		ev.setConversionMultiple(conversionMultiple);
		ev.setPort(Integer.parseInt(environment.getProperty("server.port")));
		LOG.info("CurrencyExchangeController:addConversionFactor()======>End the method");
		return repository.save(ev);
    }
	
	@RequestMapping(path="/list", method = RequestMethod.GET)
    public List < ExchangeValue > getAllConversionFactor() {
		System.out.println(">>>>>>>>>>>>getAllConversionFactor <<<<<<<<<<<");	
		LOG.info("CurrencyExchangeController:getAllConversionFactor()======>Fetching all the data from DB");
        return repository.findAll();
    }
	
}
