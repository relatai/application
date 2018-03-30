package br.relatai.tcc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
 * "WebConfig" é a principal classe de configuração global da API.
 *  Nela configuramos a liberação de consumo da API por clientes localizados em domínios diferentes
 *  do domínio da API, ou seja, configuramos o CORS (Cross-Origin Resource Sharing).
 */

@Configuration
// Para termos acesso ao método "addCorsMappings" precisamos extender a interface "WebMvcConfigurerAdapter"
public class WebConfig extends WebMvcConfigurerAdapter{
	// Sobrescremos o método "addCorsMappings".
	@Override
	public void addCorsMappings(CorsRegistry registry) {		
		registry.addMapping("/**") // Definimos que a configuração é para qualquer recurso.
			.allowedOrigins("*") // Liberamos o consumo para qualquer domínio.
        	.allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT", "OPTIONS") // Todos os verbos estão disponíveis.
        	.allowedHeaders("X-Requested-With,Content-Type,Accept,Origin"); // Os principais cabeçalhos estão disponíveis.
	}
}
