package br.relatai.tcc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.relatai.tcc.dominio.Validacao;

public interface ValidacoesRepository extends MongoRepository<Validacao, String> {

}
