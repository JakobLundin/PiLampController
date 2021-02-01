package se.dendromeda.pilampcontroller;

import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


public class RestChecker implements Runnable{
	
	private static String BASE_URL = "http://dendromeda.se:8080/lamp/";
	private static final Logger log = LoggerFactory.getLogger(RestChecker.class);
	
	Gpio gpio;
	
	String lampId;
	private RestTemplate rest;
	double blinkDuration;

	public RestChecker(String lampId, RestTemplate rest, Gpio gpio, double blinkDuration) {
		this.lampId = lampId;
		this.rest = rest;
		this.gpio = gpio;
		this.blinkDuration = blinkDuration;
	}
	
	
	@Override
	public void run(){
		String getUrl = BASE_URL +lampId+"/get";
		
		
		gpio.blink(10000);
		gpio.blink(10000);
		gpio.blink(10000);
		
		while (true) {
			log.info("Checking....");
			try {
				if (rest.getForObject(
						getUrl, String.class).equals("true")) {
					gpio.blink((int)(blinkDuration * 1000000));
					log.info("Light on!");
				}				
			} catch(ResourceAccessException e) {
				log.error("No connection to web service!");
			}
	
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				//Ignored by design
			}
		}
	};
}
