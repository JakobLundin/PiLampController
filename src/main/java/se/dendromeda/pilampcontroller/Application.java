package se.dendromeda.pilampcontroller;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	
	public static void main(String[] args) throws InterruptedException {
		//SpringApplication.run(Application.class, args);
		
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);

	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);

	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Gpio gpio = new GpioStuff();
			//Gpio gpio = new GpioMock();
			RestChecker restChecker = new RestChecker(args[0], restTemplate, gpio, Double.parseDouble(args[2]));
			ButtonChecker buttonChecker = new ButtonChecker(restTemplate, gpio, args[1]);
			Thread buttonThread = new Thread(buttonChecker);
			Thread restThread = new Thread(restChecker);
			restThread.start();
			buttonThread.start();
		};
	}
}
