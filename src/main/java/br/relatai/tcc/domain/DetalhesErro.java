package br.relatai.tcc.domain;

/**
 * Classe auxiliar que retorna mensagem quando exceções são disparadas.
 */
public class DetalhesErro {

	private String titulo;	// Atributo que informa o título da mensagem.
	private Long status; //  Atributo que informa o tipo de erro.	
	private Long timestamp;	//  Atributo que informa a data do sistema.
	private String mensagemDesenvolvedor; //  Atributo que informa a mensagem propriamente dita.
		
	public DetalhesErro() {} // Método construtor da classe padrão.

	public String getTitulo() {return titulo;}
	public void setTitulo(String titulo) {this.titulo = titulo;}

	public Long getStatus() {return status;}
	public void setStatus(Long status) {this.status = status;}

	public Long getTimestamp() {return timestamp;}
	public void setTimestamp(Long timestamp) {this.timestamp = timestamp;}

	public String getMensagemDesenvolvedor() {return mensagemDesenvolvedor;}
	public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}
}
