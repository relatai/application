package br.relatai.tcc.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import br.relatai.tcc.domain.Usuario;
import br.relatai.tcc.services.UsuariosServices;

/**
 * Esta classe de recursos disponibiliza endpoints voltados ao
 * consumo de usuários.  
 */
@RestController
@RequestMapping("/usuarios")
public class UsuariosResources {
	
	// Injeção de dependência da classe de serviços de usuários.
	@Autowired
	private UsuariosServices usuariosServices;
	
	// Endpoint que localiza um usuário através do nº do celular.
	// Verbo GET /usuarios/{celular}
	@GetMapping(path = "/{celular}")
	public ResponseEntity<?> buscarPeloCelular(@PathVariable String celular) {
		// Usuário instanciado com resultado da busca pelo método "buscarPeloCelular()".
		Usuario usuario = usuariosServices.buscarPeloCelular(celular);
		// Se resultado for 200Ok, objeto JSON com propriedades de usuário será retornado.
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(usuario);
	}
	// Endpoint que recupera a lista de todos usuários cadastrados.
	@GetMapping // Verbo GET /usuarios
	public ResponseEntity<List<Usuario>> listar(){
		// Como essa lista não será retornada constantemente, seu cache será de 20S.
		CacheControl cacheControl = CacheControl.maxAge(20, TimeUnit.SECONDS);
		// Se resultado for 200Ok, lista JSON com propriedades de usuário será retornada.
		return ResponseEntity
				.status(HttpStatus.OK)
				.cacheControl(cacheControl)
				.body(usuariosServices.listar());
	}	
}