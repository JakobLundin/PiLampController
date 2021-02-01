package se.dendromeda.pilampcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class ButtonChecker implements Runnable{

	final long MS = 1000000;
	final long BUTTONPRESS_THRESHOLD = 1 * MS; 
	final long BUTTONPRESS_COOLDOWN = 500 * MS; 
	long LONGPRESS_THRESHOLD = 700 * MS;
	
	private static String BASE_URL = "http://dendromeda.se:8080/lamp/";
	private static final Logger log = LoggerFactory.getLogger(ButtonChecker.class);
	
	String lampId;
	
	Gpio gpio;
	
	RestTemplate rest;
	
	public ButtonChecker(RestTemplate rest, Gpio gpio, String lampId) {
		this.rest = rest;
		this.gpio = gpio;
		this.lampId = lampId;
	}
	
	public long buttonDuration() {
		if (!gpio.checkButton()) return 0;
		long buttonDown = System.nanoTime();
		while (gpio.checkButton());
		return System.nanoTime() - buttonDown;
	}

	@Override
	public void run() {
		String addUrl = BASE_URL +lampId+"/add";
		while(true) {
			long lastPress = System.nanoTime();
			long duration;
			for (duration = 0; duration < BUTTONPRESS_THRESHOLD; duration = buttonDuration());
			log.info("Button pressed for" + duration + "ns");
			long sinceLast = System.nanoTime() - lastPress;
			
			if (sinceLast < BUTTONPRESS_COOLDOWN) {
				gpio.toggle();
			} else if (duration > LONGPRESS_THRESHOLD) {
				rest.getForObject(addUrl, String.class);
				gpio.blink(50000);
				gpio.blink(50000);
			}
		}
		
	}
}
