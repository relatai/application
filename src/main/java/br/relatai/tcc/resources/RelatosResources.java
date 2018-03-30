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
	
	@Autowired
	private RelatosServices relatosServices;
	
	@GetMapping
	public List<Relato> listar(){
		return relatosServices.listar();
	}

	@GetMapping(path="/{relatoId}")
	public ResponseEntity<?> buscarPorId(@PathVariable String relatoId){
		CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.SECONDS);
		Relato relato = relatosServices.buscarPorId(relatoId);	
		return ResponseEntity.status(HttpStatus.OK).cacheControl(cacheControl).body(relato);
	}
	
	@GetMapping(path="/{usuarioId}/usuarios")
	public ResponseEntity<?> meusRelatos(@PathVariable String usuarioId){		
		List<Relato> relatos = relatosServices.relatosPorUsuario(usuarioId);	
		return ResponseEntity.status(HttpStatus.OK).body(relatos);
	}
	
	@PostMapping(path="/{relatoId}/validacoes")
	public ResponseEntity<?> validar(@PathVariable String relatoId, @Valid @RequestBody Validacao validacao) throws Exception {
		ReacaoDTO reacao = relatosServices.reagir(relatoId, validacao);
		return ResponseEntity.status(HttpStatus.OK).body(reacao);
	}
	
	@DeleteMapping(path="/{relatoId}/selecionados")
	public ResponseEntity<Void> removerSeuProprioRelatoSelecionado(@PathVariable String relatoId) throws Exception{
		try {
			relatosServices.removerSeuProprioRelatoSelecionado(relatoId);
		}catch (EmptyResultDataAccessException e) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();		
	}	
}
