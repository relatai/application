package br.relatai.tcc.domain;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * A classe Validacao tem por finalidade de manter o registro de cada votação realizada por usuário
 * cadastrado pela aplicação.
 */
@Document(collection="validacao") //Esta anotação define o nome da coleção associada no banco de dados.
public class Validacao {

	private String id; // Atributo identificador do documento..
	private Usuario usuario; // Atributo que associa o usuário votante.  
	private LocalDate data; // Atributo que armazena a data em que o usuário realiza a votação.
	private LocalTime hora;	 // Atributo que armazena a hora em que o usuário realiza a votação.
	private String descricao;  // Atributo que registra a descrição caso o usuário realize uma denúncia.
	private boolean reacao;  // Atributo que recebe true (para confirmação) ou false (para denúncia).
		
	public Validacao() {} // Método construtor da classe padrão.

	@Id // Anotação que define que o atributo receberá um identificador automaticamente pelo banco de dados.
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public Usuario getUsuario() {return usuario;}
	public void setUsuario(Usuario usuario) {this.usuario = usuario;}

	// A data de publicação será devolvida do banco de dados no padrão brasileiro.
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public LocalDate getData() {return data;}
	public void setData(LocalDate data) {this.data = data;}

	// A hora de publicação será devolvida do banco de dados com fuso horário de São Paulo.
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm", timezone="America/Sao_Paulo")
	public LocalTime getHora() {return hora;}
	public void setHora(LocalTime hora) {this.hora = hora;}

	@JsonInclude(Include.NON_NULL)
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public boolean isReacao() {return reacao;}
	public void setReacao(boolean reacao) {this.reacao = reacao;}	
}
