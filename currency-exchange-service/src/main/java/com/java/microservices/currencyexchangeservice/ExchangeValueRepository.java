package com.java.microservices.currencyexchangeservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long>
{
	//creating query method
	ExchangeValue findByFromAndTo(String from, String to);
}

