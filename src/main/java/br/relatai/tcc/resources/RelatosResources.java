package br.relatai.tcc.resources;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.relatai.tcc.dominio.Relato;
import br.relatai.tcc.dominio.Validacao;
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

	@GetMapping(path="/{rid}")
	public ResponseEntity<?> buscarPorId(@PathVariable String rid){
		CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.SECONDS);
		Relato relato = relatosServices.buscarPorId(rid);	
		return ResponseEntity.status(HttpStatus.OK).cacheControl(cacheControl).body(relato);
	}
	
	@GetMapping(path="/{uid}/usuarios")
	public ResponseEntity<?> meusRelatos(@PathVariable String uid){		
		List<Relato> relatos = relatosServices.relatosPorUsuario(uid);	
		return ResponseEntity.status(HttpStatus.OK).body(relatos);
	}
	
	@PostMapping(path="/{rid}/validacoes")
	public ResponseEntity<Void> validar(@PathVariable String rid, @Valid @RequestBody Validacao validacao) throws Exception {
		relatosServices.reagir(rid, validacao);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{rid}").buildAndExpand(validacao.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@DeleteMapping(path="/{rid}/selecionados")
	public ResponseEntity<Void> removerSeuProprioRelatoSelecionado(@PathVariable String rid) throws Exception{
		try {
			relatosServices.removerSeuProprioRelatoSelecionado(rid);
		}catch (EmptyResultDataAccessException e) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();		
	}	
}
