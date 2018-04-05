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
	
	@GetMapping(path="/{usuarioId}/usuarios")
	public ResponseEntity<?> meusRelatos(@PathVariable String usuarioId){		
		List<Relato> relatos = relatosServices.relatosPorUsuario(usuarioId);	
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(relatos);
	}
	
	@PostMapping(path="/{relatoId}/validacoes")
	public ResponseEntity<?> validar(@PathVariable String relatoId, @Valid @RequestBody Validacao validacao) throws Exception {
		ReacaoDTO reacao = relatosServices.reagir(relatoId, validacao);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(reacao);
	}
	
	@DeleteMapping(path="/{relatoId}/selecionados")
	public ResponseEntity<Void> removerSeuProprioRelatoSelecionado(@PathVariable String relatoId) throws Exception{
		try {
			relatosServices.removerSeuProprioRelatoSelecionado(relatoId);
		}catch (EmptyResultDataAccessException e) {
			return ResponseEntity
					.notFound()
					.build();
		}
		return ResponseEntity
				.noContent()
				.build();		
	}	
}
