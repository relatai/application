package br.relatai.tcc.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.dominio.Relato;
import br.relatai.tcc.dominio.Validacao;
import br.relatai.tcc.repository.RelatosRepository;
import br.relatai.tcc.services.exceptions.ObjetoNaoEncontradoException;

@Service
public class RelatosServices {

	@Autowired
	private RelatosRepository relatosRepository;
	
	@Autowired
	private ValidacoesServices validacoesServices;
		
	public Relato buscarPorId(String id) {
		Relato relato = relatosRepository.findOne(id);		
		if(relato == null) {
			throw new ObjetoNaoEncontradoException("O relato não pôde ser encontrado.");
		}		
		return relato;
	}
	
	public List<Relato> listar(){		
		return relatosRepository.findAll();
	}
	
	public Relato salvar(Relato relato) {		
		relato.setId(null);		
		return relatosRepository.save(relato);
	}
	
	public void atualizar(Relato relato) {
		verificarExistencia(relato);
		relatosRepository.save(relato);
	}
	
	private void verificarExistencia(Relato relato) {
		buscarPorId(relato.getId());
	}
	
	public void validar(String rid, Validacao validacao) {
		Relato relato = relatosRepository.findOne(rid);	
		validacoesServices.salvar(validacao);
		if(validacao.isReacao() == true) {			
			relato.setConfirmado(relato.getConfirmado() + 1);
		}else {
			relato.setDenunciado(relato.getDenunciado() + 1);
			if((relato.getConfirmado() < 30) && (relato.getDenunciado() == 5)) {
				relatosRepository.delete(relato);
			}
		}
		atualizar(relato);
	}
}
