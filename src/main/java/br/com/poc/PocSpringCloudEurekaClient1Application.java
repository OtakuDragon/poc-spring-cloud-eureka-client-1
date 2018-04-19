package br.com.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//Ver class PocRestController para entender cada estrategia
@EnableDiscoveryClient //Apenas para estrategia que utiliza diretamente o bean DiscoveryClient
@EnableFeignClients  //Apenas para estretegia  utilizando interfaces anotadas do feign
@SpringBootApplication
public class PocSpringCloudEurekaClient1Application {

	public static void main(String[] args) {
		SpringApplication.run(PocSpringCloudEurekaClient1Application.class, args);
	}
	
	//Apenas para a estrategia do enhanced RestTemplate
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
