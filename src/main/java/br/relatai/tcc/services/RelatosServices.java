package br.relatai.tcc.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.domain.ReacaoDTO;
import br.relatai.tcc.domain.Relato;
import br.relatai.tcc.domain.Validacao;
import br.relatai.tcc.repository.RelatosRepository;
import br.relatai.tcc.repository.ValidacoesRepository;
import br.relatai.tcc.services.exceptions.ObjetoNaoEncontradoException;

/**
 * Esta classe de serviço contém as regras de negócio para tratamento de operações realizadas nos relatos.
 */
@Service
public class RelatosServices {

	// Injeção de dependências do repositório de relato.
	@Autowired
	private RelatosRepository relatosRepository;

	// Injeção de dependências do repositório de validacao.
	@Autowired
	private ValidacoesRepository validacoesRepository;

	// Injeção de dependências da classe de serviços de categoria.
	@Autowired
	private CategoriasServices categoriasServices;

	// Método público que recupera um relato através de um identificador recebido via parâmetro.
	public Relato buscarPorId(String id) {
		// O objeto "relato" recebe o documento buscado pelo identificador na collection "relato".
		Relato relato = relatosRepository.findOne(id);
		// Verificação se o identificador recebido via parâmetro retornará um objeto nulo.
		if (relato == null) {
			// Se a condição for verdadeira será disparada a mensagem abaixo.
			throw new ObjetoNaoEncontradoException("O relato não pôde ser encontrado.");
		}
		return relato; // Retorna o objeto "relato".
	}

	// Método público para listar todos os relatos.
	public List<Relato> listar() {
		// O método default "findAll()" recupera todos os documentos da collection "relato".
		return relatosRepository.findAll();
	}

	// Método público para salvar um novo relato.
	public Relato salvar(Relato relato) {		
		relato.setId(null); // Força o identificador do relato seja nulo para que o banco de dados o atribua.
		// O retorno do relato salvo favorece a recuperação do identificador atribuído pelo banco de dados.
		return relatosRepository.save(relato);
	}

	// Método público para atualização de um relato existente.
	public void atualizar(Relato relato) {
		// Invocação do método que verifica se o objeto "relato" recebido por parâmetro de fato existe.
		verificarExistencia(relato);
		// O método "save()" de qualquer repositório Spring MongoDB exerce a função de
		// salvar e também atualizar.
		relatosRepository.save(relato);
	}

	// Método privado que realiza a verificação da existência.
	private void verificarExistencia(Relato relato) {
		// Utiliza o método "buscarPorId()" para verificar a existência do objeto
		// recebido através de parâmetro.
		// Passamos o identificador do objeto "relato" via parâmetro.
		buscarPorId(relato.getId());
	}
	
	// Método que recebe a reação (voto) do usuário: Os detalhes da reação do
	// votante serão recebidos
	// através de um objeto Json e serializado no objeto validacao.
	public ReacaoDTO reagir(String relatoId, Validacao validacao) throws Exception {
		// Através do parâmetro relatoId será instanciado o objeto que possui o respectivo identificador.
		Relato relato = buscarPorId(relatoId);
		String relatorId = relato.getUsuario().get(0).getId();
		String votanteId = validacao.getUsuario().getId();
		// A classe ReacaoDTO foi criada exclusivamente para dinamizar a exibição dos somatórios
		// dos atributos de contagem de um relato.
		ReacaoDTO reacaoDTO = new ReacaoDTO();
		// Recupera determinados atributos de relato e os atribui nos respectivos atributos de reacaoDTO.
		reacaoDTO.setId(relato.getId());
		reacaoDTO.setConfirmado(relato.getConfirmado());
		reacaoDTO.setDenunciado(relato.getDenunciado());
		// Mensagem padrão caso a reação não seja computada.
		reacaoDTO.setMensagem("Reação não computada: relator do problema ou usuário que já tenha votado.");		
		Set<String> usuariosSet = new HashSet<String>(); // Array que não recebe valores duplicados.
		// Condição que verifica se a lista de validações em relato existe. 
		if(relato.getValidacoes() != null) {
			// Recupera cada validação existente na lista de validações.
			for(Validacao v : relato.getValidacoes()) {
				// Adiciona cada identificador de usuário encontrado no array "usuariosSet".
				usuariosSet.add(v.getUsuario().getId());			
			}			
		}	
		// Verifica se o relator é diferente do votante.
		if(!relatorId.equals(votanteId)) {
			// Verifica se na lista de identificadores do array "usuariosSet" já contém o votante.
			if(usuariosSet.contains(votanteId)) {				
			}else { // Caso valor retornado seja false dará continuidade ao algoritmo.
				salvarValidacao(validacao); // Persiste os dados de validação. 
				// Se não existir lista de validações no objeto "relato".
				if(relato.getValidacoes() == null) {					
					ArrayList<Validacao> validacoes = new ArrayList<>(); // Cria-se um array de validações.
					validacoes.add(validacao); // Adiciona-se no array o objeto de validação.
					// adiciona-se o array "validacoes" na lista de validações de relato.
					relato.setValidacoes(validacoes); 
				}else { // Se a lista de validações já existir.
					// Adiciona-se o objeto "validacao" na lista de validações do relato.
					relato.getValidacoes().add(validacao); 
				}
				// Invocação do método que faz a contagem de reações.
				operacionalizarReacao(reacaoDTO, relato, validacao); 
				if (relato != null) { // Se o relato não for nulo.
					atualizar(relato); // Atualiza-se o relato na collection.
				}
			}			
		}		
		return reacaoDTO; // Retorna o objeto "reacaoDTO".
	}

	// Método privado que cria novo documento de validação na collection "validacao".
	private Validacao salvarValidacao(Validacao validacao) {
		// É necessário recuperarmos o objeto "validacao" com identificador defino no banco de dados,
		// para que este objeto seja adicionado na lista do objeto relato.
		return validacoesRepository.save(validacao);
	}

	// Método privado que recebe parâmetros para completar a parte de contabilização das reações. 
	private void operacionalizarReacao(ReacaoDTO reacaoDTO, Relato relato, Validacao validacao) throws Exception {
		// Se a reação for verdadeira, o voto será computado no atributo "confirmacao"
		// das instâncias relato e reacaoDTO.
		if (validacao.isReacao()) {
			// Incrementa 1 ao valor já existente no atributo "confirmado" do relato.
			relato.setConfirmado(relato.getConfirmado() + 1); 
			// O atributo "confirmado" do objeto reacaoDTO recebe o novo valor do atributo "confirmado" do relato. 
			reacaoDTO.setConfirmado(relato.getConfirmado());			
			reacaoDTO.setMensagem("Confirmação válida!"); // Mensagem que será enviada a aplicação cliente.
			// Caso a reação seja negativa.
		} else {
			// Incrementa 1 ao valor já existente no atributo "denunciado" do relato.
			relato.setDenunciado(relato.getDenunciado() + 1);
			// O atributo "denunciado" do objeto reacaoDTO recebe o novo valor do atributo "denunciado" do relato. 
			reacaoDTO.setDenunciado(relato.getDenunciado());
			reacaoDTO.setMensagem("Denúncia válida!"); // Mensagem que será enviada a aplicação cliente.
			// Etapa que remove o relato caso a quantidade de confirmações seja inferior a 30 
			// e a quantidade de denúncias seja igual a 5.
			if ((relato.getConfirmado() < 30) && (relato.getDenunciado() == 5)) {
				categoriasServices.removerRelato(relato.getId()); // O método para remover o relato é invocado.
				relato = null; // O objeto instanciado "relato" é anulado. 
			}
		}
	}

	// Método público que realiza busca de relatos criados por um determinado usuário.
	public List<Relato> relatosPorUsuario(String usuarioId) {
		// O método "findByUsuarioIn()" recupera todos os relatos que possuam o mesmo identificador
		// de usuário recebido via parâmetro.
		return relatosRepository.findByUsuarioIn(usuarioId);
	}

	// Método público utilizado para remover um relato selecionado pelo próprio relator.
	public void removerSeuProprioRelatoSelecionado(String relatoId) throws Exception {
		// O método "removerRelato()" da regra de negócios de categorias é o método mais eficiente
		// para remoção de um relato, pois apaga, simultaneamente, o relato na lista de relatos da
		// collection "categoria" e da collection "relato".
		categoriasServices.removerRelato(relatoId);
	}
}
