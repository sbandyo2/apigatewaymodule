package com.ibm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class GatewayApplication {

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
		springApplication.addListeners(new ApplicationPidFileWriter("gatewayservice.pid"));
		springApplication.run(args);
	}
}
