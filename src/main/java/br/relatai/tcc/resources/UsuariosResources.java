package br.relatai.tcc.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import br.relatai.tcc.domain.Usuario;
import br.relatai.tcc.services.UsuariosServices;

@RestController
@RequestMapping("/usuarios")
public class UsuariosResources {

	@Autowired
	private UsuariosServices usuariosServices;
		
	@GetMapping(path = "/{celular}")
	public ResponseEntity<?> buscarPeloCelular(@PathVariable String celular) {		
		Usuario usuario = usuariosServices.buscarPeloCelular(celular);
		return ResponseEntity.status(HttpStatus.OK).body(usuario);
	}
	
	@GetMapping
	public ResponseEntity<List<Usuario>> listar(){
		CacheControl cacheControl = CacheControl.maxAge(20, TimeUnit.SECONDS);
		return ResponseEntity.status(HttpStatus.OK).cacheControl(cacheControl).body(usuariosServices.listar());
	}	
}


