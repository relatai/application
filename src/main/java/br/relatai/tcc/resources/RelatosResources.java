package br.relatai.tcc.resources;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.relatai.tcc.dominio.Relato;
import br.relatai.tcc.dominio.Validacao;
import br.relatai.tcc.services.RelatosServices;

@RestController
@RequestMapping("/relatos")
@CrossOrigin
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
	
	@PostMapping(path="/{rid}/validacoes")
	public ResponseEntity<Void> validar(@PathVariable String rid, @Valid @RequestBody Validacao validacao) {
		relatosServices.validar(rid, validacao);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{rid}").buildAndExpand(validacao.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
}
