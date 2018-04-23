package br.relatai.tcc.domain;

/** 
 * Classe para apenas transferência de dados que alimenta os totais
 * de confirmações e denúncias do relato instanciado pelo usuário.
*/
public class ReacaoDTO {
	// Atributo que recebe o identificador do relato instanciado.
	private String id; 	
	// Atributo que recebe o total de confirmações do relato instanciado.
	private int confirmado; 	
	// Atributo que recebe o total de denúncias do relato instanciado.
	private int denunciado; 
	// Atributo que retorna mensagem de acordo com a operação realizada.
	private String mensagem; 
		
	public ReacaoDTO() {} // Método construtor da classe padrão.
	
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public int getConfirmado() {return confirmado;}
	public void setConfirmado(int confirmado) {this.confirmado = confirmado;}
	
	public int getDenunciado() {return denunciado;}
	public void setDenunciado(int denunciado) {this.denunciado = denunciado;}
	
	public String getMensagem() {return mensagem;}
	public void setMensagem(String mensagem) {this.mensagem = mensagem;}	
}

