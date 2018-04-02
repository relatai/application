package br.relatai.tcc.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.relatai.tcc.domain.Categoria;
import br.relatai.tcc.domain.Relato;
import br.relatai.tcc.domain.Validacao;
import br.relatai.tcc.repository.CategoriasRepository;
import br.relatai.tcc.repository.RelatosRepository;
import br.relatai.tcc.repository.ValidacoesRepository;
import br.relatai.tcc.services.exceptions.ObjetoNaoEncontradoException;

/**
 * Esta classe de serviço possui as regras de negócio para tratamento de operações realizadas nas categorias. 
 */
@Service
public class CategoriasServices {

	// Injeção de dependências do repositório de categorias.
	@Autowired
	private CategoriasRepository categoriasRepository;
	
	// Injeção de dependências do repositório de relatos.
	@Autowired
	private RelatosRepository relatosRepository;

	// Injeção de dependências do repositório de validações.
	@Autowired
	private ValidacoesRepository validacoesRepository;
	
	// Injeção de dependências das regras de negócio do cloudinary.
	@Autowired 
	private ConvertBase64AndUploadToCloudinaryImageService uploadToCloudinary;
	
	// Método público que lista todas as categorias existentes.
	public List<Categoria> listar(){
		// A lista de categorias recebe todos os documentos da collection "categoria" na base de dados.
		List<Categoria> categorias = categoriasRepository.findAllByOrderByNomeAsc();
		return categorias; // Retorna a lista de categorias.
	}
	
	// Método privado, disponível apenas dentro desta classe, recupera uma categoria através de
	// um identificador recebido via parâmetro.
	private Categoria buscarPorId(String id) {
		// O objeto "categoria" recebe o documento buscado pelo identificador na collection "categoria".  
		Categoria categoria = categoriasRepository.findOne(id);		
		// Verificação se o identificador recebido via parâmetro retornará um objeto nulo.
		if(categoria == null) {
			// Se a condição for verdadeira será disparada a mensagem abaixo.
			throw new ObjetoNaoEncontradoException("A categoria não pôde ser encontrada.");
		}		
		return categoria; // Retorna o objeto "categoria".
	}

	// Método público que recebe diversos identificadores de categorias para listar todos os relatos
	// contidos nestas categorias. 
	public List<Categoria> buscarDiversasCategorias(List<String> ids) {
		Categoria categoria = null; // Criação de um objeto "categoria".
		List<Categoria> categorias = new ArrayList<>(); // Criação de uma lista "categorias" instanciada.
		// forEach que trata cada identificador da lista "ids" recebida por parâmetro. 
		for(String i : ids) {			
			categoria = buscarPorId(i); // Recupera o objeto "categoria" através do identificador.
			categorias.add(categoria); // Adiciona o objeto "categoria" na lista "categorias".
		}
		return categorias; // Retorna a lista de categorias.
	}
	
	// Método público para salvar uma nova categoria.
	public Categoria salvar(Categoria categoria) {		
		categoria.setId(null); // Força o identificador ser nulo para que o banco de dados o atribua. 
		// O retorno da categoria salva favorece a recuperação do identificador atribuído pelo
		// banco de dados.
		return categoriasRepository.save(categoria);		
	}
	
	// Método público para atualização de uma categoria existente.
	public void atualizar(Categoria categoria) {
		// Invocação do método que verifica se o objeto "categoria" recebido por parâmetro de fato existe. 
		verificarExistencia(categoria);
		// O método "save()" de qualquer repositório Spring MongoDB exerce a função de salvar e também atualizar.
		categoriasRepository.save(categoria);  
	}
	
	// Método privado que realiza a verificação da existência.
	private void verificarExistencia(Categoria categoria) {
		// Utiliza o método "buscarPorId()" para verificar a existência do objeto recebido através de parâmetro.
		// Passamos o identificador do objeto "categoria" via parâmetro. 
		buscarPorId(categoria.getId());
	}
	
	// Método público destinado a salvar um relato.
	// Apenas dois parâmetros são necessários: identificador da "categoria" e o objeto "relato". 
	public Categoria relatar(String categoriaId, Relato relato) {
		// Recupera-se um objeto "categoria" através do identificador recebido via parâmetro.
		Categoria categoria = buscarPorId(categoriaId);
		relatosRepository.save(relato);	// Salva-se o relato.
		// Verifica se a lista de relatos dentro do objeto categoria instanciado está nula.
		if(categoria.getRelatos() == null) {
			List<Relato> relatos = new ArrayList<>(); // Cria-se uma lista de relatos.		
			relatos.add(relato); // Adiciona-se o objeto "relato" na lista de relatos.
			categoria.setRelatos(relatos); // Adiciona-se a lista de relatos dentro da lista de relatos da categoria.
		// Caso a lista de relatos da categoria instanciada não esteja vazia.
		}else { 
			// O objeto "relato" é adicionado na lista de relatos da categoria instanciada.
			categoria.getRelatos().add(relato); 
		}		
		atualizar(categoria); // Atualiza-se a categoria instanciada.
		return categoria; // Retorna-se a categoria.
	}
	
	// Método que remove relatos considerados abandonados na aplicação, obedecendo a determinados critérios.
	public void RemoverRelatosAbandonados() throws Exception {
		int dias; // Variável inteira que recebe a quantidade de dias.		
		// Laço que verifica cada relato dentro da lista de relatos.
		for(Relato r : relatosRepository.findAll()) {
			// O método between recebe duas datas, calcula o intervalo e retorna em dias para variável dias.
			dias = Period.between(r.getDataPublicacao(), LocalDate.now()).getDays();
			// Verifica se o valor do atributo "confirmado" permanece igual a zero acima de dois dias.
			// E, também verifica se o mesmo atributo permanece com valor inferior a trinta acima de seis dias.
			if(((r.getConfirmado() == 0) && (dias > 1)) || ((r.getConfirmado() < 30) && (dias > 6))) {				
				removerRelato(r.getId()); // O método removerRelato() é invocado.				
				continue; // Retoma o looping.
			}			
		}
	}
	
	// Método público que remove um determinado relato da lista da collection "categoria" e simultaneamente
	// da collection "relato".
	public void removerRelato(String relatoId) throws Exception {	
		// O método "findByRelatosIn()" verifica se o identificador de relato recebido via parâmetro existe
		// em algum relato criado dentro de uma categoria. Essa categoria é atribuída ao objeto "categoria".		
		Categoria categoria = categoriasRepository.findByRelatosIn(relatoId);		
		// Em cada relato dentro da categoria instanciada será executada a rotina a seguir.
		for(Relato r : categoria.getRelatos()) {
			// Verifica se o identificador da referência "r" do objeto "relato" é igual ao identificador
			// recebido via parâmetro.
			if(r.getId().equals(relatoId)) {
				// Verifica se existem validações associadas.
				if(r.getValidacoes() != null) {
					// Para cada validação da lista.
					for(Validacao v : r.getValidacoes()) {					
						validacoesRepository.delete(v); // Remove cada validação encontrada. 
						continue; // Retoma o looping.
					}					
				}
				// O método "remove()" apaga o item, recebido via parâmetro, da lista de relatos.
				categoria.getRelatos().remove(r);
				categoriasRepository.save(categoria); // A categoria é atualizada.
				relatosRepository.delete(relatoId); // O documento é removido da collection "relato". 
				uploadToCloudinary.removerImagem(r); // A imagem é removida do cloudinary.
				break; // A estrutura de laço é interrompida.
			}			
		}		
	}	
}

