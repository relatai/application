package br.relatai.tcc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
        	.allowedOrigins("*")
        	.allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT", "OPTIONS")
        	.allowedHeaders("X-Requested-With,Content-Type,Accept,Origin");
	}
}
