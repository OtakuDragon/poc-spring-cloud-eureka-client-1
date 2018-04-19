package br.com.poc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("poc-spring-cloud-eureka-client-2")
public interface PocFeignClient {
	
	@RequestMapping(method=RequestMethod.GET, value = "/api")
    public String service();
}
