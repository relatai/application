package br.relatai.tcc.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.domain.ReacaoDTO;
import br.relatai.tcc.domain.Relato;
import br.relatai.tcc.domain.Validacao;
import br.relatai.tcc.repository.RelatosRepository;
import br.relatai.tcc.repository.ValidacoesRepository;
import br.relatai.tcc.services.exceptions.ObjetoNaoEncontradoException;

/*
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
		// O retorno do relato salvo favorece a recuperação do identificador atribuído pelo 
		// banco de dados.
		return relatosRepository.save(relato);
	}

	// Método público para atualização de um relato existente.
	public void atualizar(Relato relato) {
		// Invocação do método que verifica se o objeto "relato" recebido por parâmetro de fato existe.
		verificarExistencia(relato);
		// O método "save()" de qualquer repositório Spring MongoDB exerce a função de salvar e também atualizar.
		relatosRepository.save(relato);
	}
	
	// Método privado que realiza a verificação da existência.
	private void verificarExistencia(Relato relato) {
		// Utiliza o método "buscarPorId()" para verificar a existência do objeto recebido através de parâmetro.
		// Passamos o identificador do objeto "relato" via parâmetro. 
		buscarPorId(relato.getId());
	}

	// Método que recebe a reação (voto) do usuário: Os detalhes da reação do votante serão recebidos  
	// através de um objeto Json e serializado no objeto validacao.	
	public ReacaoDTO reagir(String relatoId, Validacao validacao) throws Exception {		
		// Através do parâmetro relatoId será instanciado o objeto que possui o respectivo identificador.
		Relato relato = buscarPorId(relatoId);		
		String relatorId = relato.getUsuario().get(0).getId(); // Recupera o identificador do relator.
		String votanteId = validacao.getUsuario().getId(); // Recupera o identificador do votante.
		// A classe ReacaoDTO foi criada exclusivamente para dinamizar a exibição dos somatórios
		// dos atributos de contagem de um relato.
		ReacaoDTO reacaoDTO = new ReacaoDTO();
		// Recupera determinados atributos de relato e os atribui nos respectivos atributos de reacaoDTO.
		reacaoDTO.setId(relato.getId());
		reacaoDTO.setConfirmado(relato.getConfirmado());
		reacaoDTO.setDenunciado(relato.getDenunciado());
		reacaoDTO.setMensagem("Reação não computada: relator do problema ou usuário que já tenha votado.");
		// Verifica se os identificadores de ambos usuários são diferentes.
		if(votanteId != relatorId) { 
			// Verifica se a lista de validações não existe no objeto "relato".
			if(relato.getValidacoes() == null) {
				// É necessário salvar o registro de validação antes de associá-lo na lista de validações do relato,
				// para não ocorrer exception.
				salvarValidacao(validacao);  
				List<Validacao> validacoes = new ArrayList<>(); // Criação e instanciamento da lista "validacoes".
				validacoes.add(validacao); // Inclusão do
				relato.setValidacoes(validacoes);	
			// Caso a lista de validações exista.
			}else { 
				// Percorre cada elemento de validação da lista de validações do objeto relato.
				for(Validacao v : relato.getValidacoes()) {			
					// Como regra de negócio, um usuário que já tenha votado não poderá realizar outro voto no mesmo relato.
					// Então, comparamos o identificador do usuário que está tentando votar com os identificadores já existentes
					// dos usuários que já votaram.
					if(!v.getUsuario().getId().equals(votanteId)) {  
						// Se o identificador de usuário não for encontrado, o objeto de verificação será salvo como novo 
						// documento na collection "validacao".
						salvarValidacao(validacao);  
						// Inclusão do objeto de validação na lista de validações do objeto de relato.
						relato.getValidacoes().add(validacao); 
						break; // Interrupção da verificação após realização da linha anterior.
					}					
				}
			}
			// Se a reação for verdadeira o voto será computado no atributo "confirmacao"
			// das instâncias relato e reacaoDTO.
			if(validacao.isReacao()) {
				relato.setConfirmado(relato.getConfirmado() + 1);
				reacaoDTO.setConfirmado(relato.getConfirmado());
				reacaoDTO.setMensagem("Confirmação válida!");
			}else { 					
				relato.setDenunciado(relato.getDenunciado() + 1);
				reacaoDTO.setDenunciado(relato.getDenunciado());
				reacaoDTO.setMensagem("Denúncia válida!");
				// Etapa que remove o relato caso a quantidade de confirmações seja inferior a 30 e a quantidade 
				// de denúncias seja igual a 5.
				if((relato.getConfirmado() < 30) && (relato.getDenunciado() == 5)) {
					categoriasServices.removerRelato(relato.getId());
					relato = null;
				}
			}
		}
		// Por fim, o relato não sendo nulo, é atualizado.
		if (relato != null) {
			atualizar(relato);
		}
		return reacaoDTO; // Retornamos a instância devidamente preenchida.
	}
	
	private void salvarValidacao(Validacao validacao) {
		validacoesRepository.save(validacao);
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
