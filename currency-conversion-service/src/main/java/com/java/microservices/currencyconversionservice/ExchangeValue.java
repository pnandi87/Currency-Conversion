package com.java.microservices.currencyconversionservice;
import java.math.BigDecimal;


public class ExchangeValue 
{
	
	private Long id;
	private String from;
	private String to;
	private BigDecimal conversionMultiple;
	private int port;
	
	public ExchangeValue(){}
	
	public ExchangeValue(String from){
		this.from=from;
		System.out.println(from);
	}
	
	//generating constructor using fields
	public ExchangeValue(Long id, String from, String to, BigDecimal conversionMultiple) 
	{
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.conversionMultiple = conversionMultiple;
	}
	
	
	@Override
	public String toString() {
		return "ExchangeValue [id=" + id + ", from=" + from + ", to="+ to + "conversionMultiple="+conversionMultiple+"]";
	}
	
	//generating getters
	public int getPort() 
	{
	return port;
	}
	public void setPort(int port) 
	{
	this.port = port;
	}
	public Long getId() 
	{
	return id;
	}
	public String getFrom() 
	{
	return from;
	}
	public String getTo() 
	{
	return to;
	}
	public BigDecimal getConversionMultiple() 
	{
	return conversionMultiple;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public void setConversionMultiple(BigDecimal conversionMultiple) {
		this.conversionMultiple = conversionMultiple;
	}


}
