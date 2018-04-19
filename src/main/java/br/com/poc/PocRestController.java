package br.com.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class PocRestController {

	
	/* Estrategia DiscoveryClient
	 * 
	 * Estrategia de mais baixo nivel acessa diretamente a api do
	 * discovery client e escolhe uma instância e não faz load balancing
	 * automatico a mesmo que seja implementado.(Não recomendado)
	 */
	@Autowired
	private DiscoveryClient discoveryClient;

	/* Estrategia enhanced RestTemplate
	 * 
	 * RestTemplate configurado para se comunicar com o Eureka
	 * e o Ribbon(cache local) para fazer o de-para do host de todas as urls
	 * montadas com ele para buscar o local real da aplicação no 
	 * servidor de discovery ou no cache, já faz balanceamento mas
	 * ainda utiliza rest template portanto a camada http não é abstraida
	 * o retorno deve ser tratado como uma resposta HTTP.
	 */
	@Autowired
	private RestTemplate restTemplate;
	
	/* Estrategia Feign
	 * 
	 * Interface anotada como FeignClient e injetada no controller,
	 * nesse caso o cliente apenas usa a interface como se fosse uma
	 * classe comum e toda a comunicação http é abstraida pelo framework,
	 * caso ocorra algum erro uma FeignExecption é jogada, não é necessário
	 * tratar status e resposta HTTP.
	 * 
	 * O Feign também faz uso do cache local de serviços(Ribbon) como o
	 * enhanced RestTemplate.
	 */
	@Autowired
	private PocFeignClient pocFeignClient;
	
	@RequestMapping(method=RequestMethod.GET, value = "/{clientType}")
    public String consumeService(@PathVariable("clientType") String clientType) {
		if(clientType.equals("enhancedRestTemplate")) {
			// http://{spring.application.name}/...}
			return restTemplate.getForEntity("http://poc-spring-cloud-eureka-client-2/api", String.class).getBody();
		}
		if(clientType.equals("discoveryClient")) {
			String urlbase = discoveryClient.getInstances("poc-spring-cloud-eureka-client-2").get(0).getUri().toString();
			//O RestTemplate sendo injetado está configurado para ser enhanced ou seja fazer o de-para de host da url para local real do serviço usando o eureka
			//nesse caso nos buscamos o local real usando o discovery client ou seja o de-para não é necessario por isso é instanciado um novo RestTemplate normal que não faz esse de-para de host.
			return new RestTemplate().getForEntity(urlbase+"/api", String.class).getBody();
		}
		if(clientType.equals("feignClient")) {
			return pocFeignClient.service();
		}
        throw new RuntimeException("clientType invalido "+ clientType);
    }
	
}
