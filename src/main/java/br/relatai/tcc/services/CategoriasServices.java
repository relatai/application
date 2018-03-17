package br.relatai.tcc.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.dominio.Categoria;
import br.relatai.tcc.dominio.Relato;
import br.relatai.tcc.repository.CategoriasRepository;
import br.relatai.tcc.services.exceptions.ObjetoNaoEncontradoException;

@Service
public class CategoriasServices {

	@Autowired
	private CategoriasRepository categoriasRepository;
	
	@Autowired
	private RelatosServices relatosServices;
	
	public List<Categoria> listar(){		
		List<Categoria> categorias = categoriasRepository.findBySituacaoTrueOrderByNomeAsc();	
		return categorias;
	}
			
	private Categoria buscarPorId(String id) {
		Categoria categoria = categoriasRepository.findOne(id);		
		if(categoria == null) {
			throw new ObjetoNaoEncontradoException("A categoria não pôde ser encontrada.");
		}		
		return categoria;
	}

	public List<Categoria> buscarDiversasCategorias(List<String> ids) {
		Categoria categoria = null;
		List<Categoria> categorias = new ArrayList<>();
		for(String i : ids) {
			categoria = categoriasRepository.findOne(i);			
			if(categoria == null) {
				throw new ObjetoNaoEncontradoException("A categoria não pôde ser encontrada.");
			}
			categorias.add(categoria);
		}
		return categorias;
	}
		
	public Categoria salvar(Categoria categoria) {		
		categoria.setId(null);		
		return categoriasRepository.save(categoria);
	}
	
	public void atualizar(Categoria categoria) {
		verificarExistencia(categoria);
		categoriasRepository.save(categoria);
	}
	
	private void verificarExistencia(Categoria categoria) {
		buscarPorId(categoria.getId());
	}
	
	public Categoria relatar(String cid, Relato relato) {
		Categoria categoria = buscarPorId(cid);		
		relatosServices.salvar(relato);		
		if(categoria.getRelatos() == null) {
			List<Relato> relatos = new ArrayList<>();		
			relatos.add(relato);
			categoria.setRelatos(relatos);
		}else {
			categoria.getRelatos().add(relato);
		}		
		atualizar(categoria);		
		return categoria;
	}
	
	public void removerRelato(String relatoId) {
		Categoria categoria = categoriasRepository.findByRelatosIn(relatoId);
		for(Relato r : categoria.getRelatos()) {			
			if(r.getId().equals(relatoId)) {
				categoria.getRelatos().remove(r);
				categoriasRepository.save(categoria);
				relatosServices.remover(relatoId);
				break;
			}			
		}		
	}
}

