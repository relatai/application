package br.relatai.tcc.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.relatai.tcc.dominio.Categoria;

public interface CategoriasRepository extends MongoRepository<Categoria, String> {
	List<Categoria> findBySituacaoTrueOrderByNomeAsc();
	Categoria findByRelatosIn(String relatoId);
}