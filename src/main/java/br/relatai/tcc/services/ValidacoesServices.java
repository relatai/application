package br.relatai.tcc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.domain.Validacao;
import br.relatai.tcc.repository.ValidacoesRepository;

/**
 * Esta classe de serviço possui a regra de negócio para inclusão de validações na base de dados. 
 */
@Service
public class ValidacoesServices {
	
	// Injeção de dependências do repositório de validações.
	@Autowired
	private ValidacoesRepository validacoesRepository;
	
	// Método público que recebe um objeto "validacao" por parâmetro e o salva.
	public void salvar(Validacao validacao) {
		validacao.setId(null); // Força o identificador do objeto ser nulo.
		validacoesRepository.save(validacao); // Salva o objeto no banco de dados.
	}
}
