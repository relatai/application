package br.relatai.tcc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.dominio.Validacao;
import br.relatai.tcc.repository.ValidacoesRepository;

@Service
public class ValidacoesServices {

	@Autowired
	private ValidacoesRepository validacoesRepository;
	
	public void salvar(Validacao validacao) {
		validacao.setId(null);
		validacoesRepository.save(validacao);
	}
}
