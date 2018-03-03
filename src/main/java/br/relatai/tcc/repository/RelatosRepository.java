package br.relatai.tcc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.relatai.tcc.dominio.Relato;

public interface RelatosRepository extends MongoRepository<Relato, String>{

}