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
import br.relatai.tcc.domain.Categoria;
import br.relatai.tcc.domain.Relato;
import br.relatai.tcc.services.CategoriasServices;
import br.relatai.tcc.services.ConvertBase64AndUploadToCloudinaryImageService;

@RestController
@RequestMapping("/categorias")
public class CategoriasResources {

	@Autowired // Injeção de dependência da classe de serviços "CategoriasServices".
	private CategoriasServices categoriasServices;		
	
	@Autowired // Injeção necessária para interação com os métodos de acesso ao Cloudinary.
	private ConvertBase64AndUploadToCloudinaryImageService uploadToCloudinary;
		
	// Endpoint do tipo POST que recebe um objeto "categoria" por parêmetro.
	@PostMapping
	public ResponseEntity<Void> salvar(@Valid @RequestBody Categoria categoria) {		
		// O objeto "categoria" é salvo e reatribuído ao mesmo objeto,
		// a fim de que seu identificador seja resgatado.
		categoria = categoriasServices.salvar(categoria);
		// O objeto "uri" indica que o identificador da categoria será informado
		// através do atributo Location na resposta do HTTP.
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(categoria.getId())
				.toUri();
		// A resposta é criada e retornada.
		return ResponseEntity.created(uri).build();
	}	
	
	
	// Endpoint do tipo GET que recebe uma lista de identificadores de categorias separados
	// por vírgulas.
	@GetMapping(path = "/{categoriaId}")
	public ResponseEntity<?> buscarPorIdentificadores(@PathVariable List<String> listaIds) {
		// O cache é construído com tempo de 5 segundos.
		CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.SECONDS);
		// A lista de identificadores das categorias é passada por parâmetro ao método
		// "buscarDiversasCategorias()" que gera uma lista destes objetos e a atribui a
		// lista de categorias.
		List<Categoria> categorias = categoriasServices.buscarDiversasCategorias(listaIds);
		// Se o status do HTTP for Ok, o cache e a lista de categorias serão incluídos
		// na resposta e esta será retornada.
		return ResponseEntity.status(HttpStatus.OK)
				.cacheControl(cacheControl)
				.body(categorias);
	}
	
	// Endpoint do tipo GET que lista todas as categorias cadastradas.
	@GetMapping
	public ResponseEntity<List<Categoria>> listar(){
		// O cache é construído com tempo de 5 segundos.
		CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.SECONDS);
		// Se o status do HTTP for Ok, a lista de categorias será enviada como resposta
		// ao HTTP que também inicia a contagem do cache.
		return ResponseEntity.status(HttpStatus.OK)
				.cacheControl(cacheControl).
				body(categoriasServices.listar());
	}	
	
	// Endpoint do tipo POST que recebe um identificador de categoria e um objeto JSON
	// de relato.
	@PostMapping(path = "/{categoriaId}/relatos")
	public ResponseEntity<Void> relatar(@PathVariable String categoriaId, 
			@Valid @RequestBody Relato relato) throws IOException {				
		// O valor do atributo "foto" do objeto "relato" é redefinido através do método
		// "mePassaStringBase64()".
		relato.setFoto(uploadToCloudinary.mePassaStringBase64(relato.getFoto())
				  .ireiConverter() // Método que cria um "productId".
				  .realizarUpload() // Método que realiza o upload da imagem.
				  .eRetornarUrlGeradaAposUpload()); // Método que recupera a URI gerada.
		// O método "relatar()" salva o relato e o associa a categoria informada via
		// parâmetro e logo cria uma instância desta categoria.
		Categoria categoria = categoriasServices.relatar(categoriaId, relato);
		// O objeto "uri" indica que o identificador da categoria será informado
		// através do atributo Location na resposta do HTTP.
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{categoriaId}")
				.buildAndExpand(categoria.getId())
				.toUri();
		// A resposta é criada e retornada.
		return ResponseEntity.created(uri).build();
	}
}
