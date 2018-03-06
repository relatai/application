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
import br.relatai.tcc.dominio.Categoria;
import br.relatai.tcc.dominio.Relato;
import br.relatai.tcc.services.CategoriasServices;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "http://localhost:8013")
public class CategoriasResources {

	@Autowired
	private CategoriasServices categoriasServices;	
			
	@PostMapping
	public ResponseEntity<Void> salvar(@Valid @RequestBody Categoria categoria) {		
		categoria = categoriasServices.salvar(categoria);		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}	
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<?> buscarPorIdentificadores(@PathVariable List<String> id) {
		CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.SECONDS);		
		List<Categoria> categorias = categoriasServices.buscarDiversasCategorias(id);
		return ResponseEntity.status(HttpStatus.OK).cacheControl(cacheControl).body(categorias);
	}
	
	@GetMapping
	public ResponseEntity<List<Categoria>> listar(){
		CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.SECONDS);
		return ResponseEntity.status(HttpStatus.OK).cacheControl(cacheControl).body(categoriasServices.listar());
	}	
	
	@PostMapping(path = "/{cid}/relatos")
	public ResponseEntity<Void> relatar(@PathVariable String cid, @Valid @RequestBody Relato relato) {				
		Categoria categoria = categoriasServices.relatar(cid, relato);		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
}
