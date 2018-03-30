package br.relatai.tcc.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.relatai.tcc.domain.Categoria;

/*
 * Esta é a camada que acessa a collection categoria na base de dados.  
 */
public interface CategoriasRepository extends MongoRepository<Categoria, String> {
	// Método que lista todas categorias ordenadas por nome de forma ascendente.
	List<Categoria> findAllByOrderByNomeAsc();
	// Método que procura um determinado relato através do seu identificador em todas categorias.
	Categoria findByRelatosIn(String relatoId);
}