package br.relatai.tcc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.relatai.tcc.domain.Usuario;

/*
 * Esta é a camada que acessa a collection usuário na base de dados.  
 */
public interface UsuariosRepository extends MongoRepository<Usuario, String> {
	// Método que realiza a busca de um usuário através do número do celular.
	Usuario findByCelular(String celular);
}