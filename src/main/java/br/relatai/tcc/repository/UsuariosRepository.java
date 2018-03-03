package br.relatai.tcc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.relatai.tcc.dominio.Usuario;

public interface UsuariosRepository extends MongoRepository<Usuario, String> {

	Usuario findByCelular(String celular);
}
