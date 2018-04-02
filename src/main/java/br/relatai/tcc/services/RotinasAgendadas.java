package br.relatai.tcc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * A classe RotinasAgendadas tem por objetivo criar um agendamento
 * de rotinas a serem verificadas automaticamente pela API.
 */
@Component
@EnableScheduling
public class RotinasAgendadas {

	// Definição da constante "TIME_ZONE" e seu valor.
	private final String TIME_ZONE = "America/Sao_Paulo";
	
	// Injeção de dependências de CategoriasServices. 
	@Autowired
	private CategoriasServices categoriasServices;
	
	// O agendamento define que todos os dias, às 03h, o método "verificarRelatosAbandonados()" é executado.
	@Scheduled(cron = "0 * 21 * * *", zone = TIME_ZONE)
	public void verificarRelatosAbandonados() throws Exception {
		// Execução do método RemoverRelatosAbandonados().
		categoriasServices.RemoverRelatosAbandonados();
	}
}
