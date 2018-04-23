package br.relatai.tcc.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import br.relatai.tcc.domain.ReacaoDTO;
import br.relatai.tcc.domain.Relato;
import br.relatai.tcc.domain.Validacao;
import br.relatai.tcc.services.RelatosServices;

@RestController
@RequestMapping("/relatos")
public class RelatosResources {	
	// Injeção de dependências da classe de serviço de relatos.
	@Autowired
	private RelatosServices relatosServices;
	
	// Endpoint de listagem de relatos.
	@GetMapping
	public List<Relato> listar(){
		return relatosServices.listar(); // Retorna a listagem de relatos.
	}

	// Endpoint que realiza busca através de identificador recebido via parâmetro.
	@GetMapping(path="/{relatoId}") // Parâmetro recebido via URI.
	// A anotação @PathVariable atribui o parâmetro recebido a variável "relatoId".
	public ResponseEntity<?> buscarPorId(@PathVariable String relatoId){
		// O cache armazena a informação por 5 segundos.
		CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.SECONDS);
		// Instancia o objeto "relato" através da busca pelo identificador.
		Relato relato = relatosServices.buscarPorId(relatoId);	
		// Se o status da resposta for OK, o objeto "relato" será devolvido. 
		return ResponseEntity
				.status(HttpStatus.OK)
				.cacheControl(cacheControl)
				.body(relato);
	}
	
	// Endpoint que lista todos os relatos de um determinado usuário.
	@GetMapping(path="/{usuarioId}/usuarios")
	public ResponseEntity<?> meusRelatos(@PathVariable String usuarioId){
		// A lista será construída após recuperação dos relatos através do 
		// identificador de usuário recebido via URI. 
		List<Relato> relatos = relatosServices.relatosPorUsuario(usuarioId);
		// Se o status da resposta for OK, a lista de "relatos" será devolvida.
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(relatos);
	}
	
	// Endpoint que realiza a validação (reação) exercida pelo usuário
	// num determinado relato.
	// São recebidos dois parâmetros: o identificador do relato e o objeto "validacao".
	@PostMapping(path="/{relatoId}/validacoes")
	public ResponseEntity<?> validar(@PathVariable String relatoId, @Valid @RequestBody 
			Validacao validacao) throws Exception {
		// É invocado o método "reagir()" e atribui o objeto reacaoDTO gerado 
		// ao objeto "reacao".
		ReacaoDTO reacao = relatosServices.reagir(relatoId, validacao);
		// Se o status do HTTP for Ok, o objeto "reação" inserido no corpo
		// da requisição.
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(reacao);
	}
	
	// Endpoint que remove o determinado relato escolhido pelo próprio relator.
	// Que recebe um identificador de relato. 
	@DeleteMapping(path="/{relatoId}/selecionados")
	public ResponseEntity<Void> removerSeuProprioRelatoSelecionado
		(@PathVariable String relatoId) throws Exception{
		try {
			// O método de remoção do relato é invocado e para tal ação
			// precisa do identificador do relato que será removido.
			relatosServices.removerSeuProprioRelatoSelecionado(relatoId);
		// Se a exceção "EmptyResultDataAccessException" for lançada.
		}catch (EmptyResultDataAccessException e) {
			// A resposta da requisição será um 404.
			return ResponseEntity
					.notFound()
					.build();
		}
		// A resposta da requisição será vazia.
		return ResponseEntity
				.noContent()
				.build();		
	}	
}