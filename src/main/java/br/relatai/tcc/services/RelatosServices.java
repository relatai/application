package br.relatai.tcc.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.dominio.Relato;
import br.relatai.tcc.dominio.Validacao;
import br.relatai.tcc.dominio.ReacaoDTO;
import br.relatai.tcc.repository.RelatosRepository;
import br.relatai.tcc.services.exceptions.ObjetoNaoEncontradoException;

@Service
public class RelatosServices {

	@Autowired
	private RelatosRepository relatosRepository;
	
	@Autowired
	private CategoriasServices categoriasServices;

	public Relato buscarPorId(String id) {
		Relato relato = relatosRepository.findOne(id);
		if (relato == null) {
			throw new ObjetoNaoEncontradoException("O relato não pôde ser encontrado.");
		}
		return relato;
	}

	public List<Relato> listar() {
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

	/*
	 * Método que recebe a reação (voto) do usuário: Os detalhes da reação do
	 * votante serão recebidos através de um objeto Json e serializado no objeto
	 * validacao.
	 */
	public ReacaoDTO reagir(String relatoId, Validacao validacao) throws Exception {
		// Através do parâmetro relatoId será instanciado o objeto que possui o
		// respectivo identificador.
		Relato relato = relatosRepository.findOne(relatoId);
		// A classe ReacaoDTO foi criada exclusivamente para dinamizar a exibição dos
		// somatórios
		// das propriedades de contagem de um relato.
		ReacaoDTO reacaoDTO = new ReacaoDTO();
		// Recupera determinadas propriedades de relato e as atribui nas respectivas
		// propriedades de reacaoDTO.
		reacaoDTO.setId(relato.getId());
		reacaoDTO.setConfirmado(relato.getConfirmado());
		reacaoDTO.setDenunciado(relato.getDenunciado());
		reacaoDTO.setMensagem("Reação não computada: relator do problema ou usuário que já tenha votado.");
		// A variável relatorId recupera o identificador do usuário que relatou o
		// problema
		String relatorId = relato.getUsuario().get(0).getId();
		// Verifica se o votante é o próprio relator, entrará caso contrário.
		if (!validacao.getUsuario().getId().equals(relatorId)) {
			// Verifica se o votante já realizou algum voto neste relato.
			for (Validacao v : relato.getValidacoes()) {
				if (!validacao.getUsuario().getId().equals(v.getUsuario().getId())) {
					// Se a reação for verdadeira o voto será computado na propriedade confirmacao
					// das instâncias relato e reacaoDTO.
					if (validacao.isReacao()) {
						relato.setConfirmado(relato.getConfirmado() + 1);
						reacaoDTO.setConfirmado(relato.getConfirmado());
						reacaoDTO.setMensagem("Confirmação válida!");
						// Caso contrário, o voto será computado na propriedade denuncia
						// das instâncias relato e reacaoDTO.
					} else {
						relato.setDenunciado(relato.getDenunciado() + 1);
						reacaoDTO.setDenunciado(relato.getDenunciado());
						reacaoDTO.setMensagem("Denúncia válida!");
						// Etapa que remove o relato caso a quantidade de confirmações seja
						// inferior a 30 e a quantidade de denúncias seja igual a 5.
						if ((relato.getConfirmado() < 30) && (relato.getDenunciado() == 5)) {
							categoriasServices.removerRelato(relatoId);
							relato = null;
						}
					}
					// Por fim, o relato não sendo nulo, é atualizado.
					if (relato != null) {
						atualizar(relato);
					}
					// Interrompe o laço, antes de executar o próximo ciclo, assim que
					// os critérios forem obedecidos
					break;
				}
			}
		}
		// Retornamos a instância devidamente preenchida
		return reacaoDTO;
	}

	public List<Relato> relatosPorUsuario(String usuarioId) {
		return relatosRepository.findByUsuarioIn(usuarioId);
	}

	public void removerSeuProprioRelatoSelecionado(String relatoId) throws Exception {
		categoriasServices.removerRelato(relatoId);
	}

	public void remover(String rid) {
		Relato r = buscarPorId(rid);
		relatosRepository.delete(r);
	}
}
