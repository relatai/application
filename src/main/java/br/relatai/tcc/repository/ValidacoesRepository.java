package br.relatai.tcc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.relatai.tcc.domain.Validacao;

/**
 * Esta é a camada que acessa a collection validacao na base de dados.  
 */
public interface ValidacoesRepository extends MongoRepository<Validacao, String> {
	
}