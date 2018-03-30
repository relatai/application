package br.relatai.tcc.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.relatai.tcc.domain.Relato;

/*
 * Esta é a camada que acessa a collection relato na base de dados.  
 */
public interface RelatosRepository extends MongoRepository<Relato, String>{
	// Método que realiza busca de um determinado usuário através do identificador na lista de relatos.
	List<Relato> findByUsuarioIn(String usuarioId);
}