package br.relatai.tcc.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.domain.Usuario;
import br.relatai.tcc.repository.UsuariosRepository;

/**
 *  Esta classe de serviço possui as regras de negócio para tratamento de operações
 *  realizadas nos usuários. 
 */
@Service
public class UsuariosServices {

	// Injeção de dependências do repositório de usuários.
	@Autowired
	private UsuariosRepository usuariosRepository;
	
	// Método público que lista todos usuários.
	public List<Usuario> listar(){
		// Lista de usuários recuperados do banco de dados.
		List<Usuario> usuarios = usuariosRepository.findAll(); 
		// Lista de usuários criada para receber todos os usuários com os celulares decifrados.
		List<Usuario> usuariosDecifrados = new ArrayList<>(); 
		// Verifica cada usuário da lista de usuários.
		for(Usuario u : usuarios) {
			// Decodifi-se o nº do celular e o reatribui a mesma propriedade de cada usuário da lista.
			u.setCelular(decodificarCelular(u.getCelular())); 
			// Inclui cada usuário com celular decifrado na lista "usuariosDecifrados".
			usuariosDecifrados.add(u); 
		}
		return usuariosDecifrados; // A lista "usuariosDecifrados" é retornada.
	}

	// Método público parametrizado que realiza a busca de um usuário através do número do seu celular. 
	public Usuario buscarPeloCelular(String celular) {	
		// O métedo "cifrarCelular()" é invocado recebendo o nº do celular. Somente a partir de então,  
		// o método "findByCelular()" pode fazer a busca pela cifra registrada no banco de dados. 
		Usuario usuario = usuariosRepository.findByCelular(cifrarCelular(celular));		
		// Verifica-se o usuário não foi encontrado.
		if(usuario == null) {
			// Então, um novo objeto de usuário é criado com o nº do celular passado anteriormente.
			usuario = new Usuario(null, cifrarCelular(celular), LocalDate.now());
			usuariosRepository.save(usuario); // Esse usuário é salvo no banco de dados.
		}		
		return usuario; // O usuário é retornado.
	}
	
	// Método privado que realiza a cifragem do número do celular. 
	private String cifrarCelular(String celular) {
		// A cifragem ocorre através da classe "Base64" e é retornada.
		return Base64.getEncoder().encodeToString(celular.getBytes());
	}
	
	// Método privado que realiza a decodificação do número do celular.
	private String decodificarCelular(String cifra) {
		// A decodificação é realizada na classe Base64 e atribuída na variável de bytes. 
		byte[] bytesDecodificados = Base64.getDecoder().decode(cifra);
		// O objeto "stringDecodificada" converte os bytes em String.
		String stringDecodificada = new String(bytesDecodificados);
		return stringDecodificada; // o objeto é retornado.
	}
}