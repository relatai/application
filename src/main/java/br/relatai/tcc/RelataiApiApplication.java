package br.relatai.tcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe do Spring Boot para inicialização da aplicação.
 */
@SpringBootApplication
public class RelataiApiApplication {

	// Método principal para execução da aplicação.
	public static void main(String[] args) {
		SpringApplication.run(RelataiApiApplication.class, args);
	}
}