package br.relatai.tcc.resources;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.relatai.tcc.dominio.Categoria;
import br.relatai.tcc.dominio.Relato;
import br.relatai.tcc.services.CategoriasServices;
import br.relatai.tcc.services.ConvertBase64AndUploadToCloudinaryImageService;

@RestController
@RequestMapping("/categorias")
public class CategoriasResources {

	@Autowired
	private CategoriasServices categoriasServices;		
	
	@Autowired 
	private ConvertBase64AndUploadToCloudinaryImageService uploadToCloudinary;
			
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
	public ResponseEntity<Void> relatar(@PathVariable String cid, @Valid @RequestBody Relato relato) throws IOException {				
		
		relato.setFoto(uploadToCloudinary.mePassaStringBase64(relato.getFoto())
				  .ireiConverter()
				  .realizarUpload()
				  .eRetornarUrlGeradaAposUpload());
	
		Categoria categoria = categoriasServices.relatar(cid, relato);		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(categoria.getId())
				.toUri();
		
		return ResponseEntity.created(uri).build();
	}	
}
