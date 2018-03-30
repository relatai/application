package br.relatai.tcc.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.domain.Usuario;
import br.relatai.tcc.repository.UsuariosRepository;

/*
 *  Esta classe de serviço possui as regras de negócio para tratamento de operações realizadas nos usuários. 
 */
@Service
public class UsuariosServices {

	@Autowired
	private UsuariosRepository usuariosRepository;
	
	public List<Usuario> listar(){
		List<Usuario> usuarios = usuariosRepository.findAll();
		List<Usuario> usuariosDecifrados = new ArrayList<>();
		for(Usuario u : usuarios) {
			u.setCelular(decodificarCelular(u.getCelular()));
			usuariosDecifrados.add(u);
		}
		return usuariosDecifrados;
	}

	public Usuario buscarPeloCelular(String celular) {	
		Usuario usuario = usuariosRepository.findByCelular(cifrarCelular(celular));		
		if(usuario == null) {
			usuario = new Usuario(null, cifrarCelular(celular), LocalDate.now());
			usuariosRepository.save(usuario);
		}		
		return usuario;
	}
	
	private String cifrarCelular(String celular) {
		return Base64.getEncoder().encodeToString(celular.getBytes());
	}
	
	private String decodificarCelular(String cifra) {
		byte[] bytesDecodificados = Base64.getDecoder().decode(cifra);
		String stringDecodificada = new String(bytesDecodificados);
		return stringDecodificada;
	}
}