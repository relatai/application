package br.relatai.tcc.dominio;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@Document(collection="validacao")
public class Validacao {

	private String id;
	private Usuario usuario;	
	private LocalDate data;
	private LocalTime hora;	
	private String descricao;	
	private boolean reacao;
	
	@Id
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public Usuario getUsuario() {return usuario;}
	public void setUsuario(Usuario usuario) {this.usuario = usuario;}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public LocalDate getData() {return data;}
	public void setData(LocalDate data) {this.data = data;}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm", timezone="America/Sao_Paulo")
	public LocalTime getHora() {return hora;}
	public void setHora(LocalTime hora) {this.hora = hora;}

	@JsonInclude(Include.NON_NULL)
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public boolean isReacao() {return reacao;}
	public void setReacao(boolean reacao) {this.reacao = reacao;}	
}
