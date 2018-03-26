package br.relatai.tcc.dominio;

public class ReacaoDTO {

	private String id;
	private int confirmado;
	private int denunciado;
	private String mensagem;
	
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public int getConfirmado() {return confirmado;}
	public void setConfirmado(int confirmado) {this.confirmado = confirmado;}
	
	public int getDenunciado() {return denunciado;}
	public void setDenunciado(int denunciado) {this.denunciado = denunciado;}
	
	public String getMensagem() {return mensagem;}
	public void setMensagem(String mensagem) {this.mensagem = mensagem;}	
	
}
