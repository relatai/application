package br.relatai.tcc.domain;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A classe Validacao tem por finalidade de manter o registro de cada votação 
 * realizada por usuário cadastrado pela aplicação.
 */
// Esta anotação define o nome da coleção associada no banco de dados.
@Document(collection="validacao") 
public class Validacao {
	// Atributo identificador do documento.
	private String id; 
	// Atributo que associa o usuário votante.
	private Usuario usuario;   
	// Atributo que armazena a data em que o usuário realiza a votação.
	private LocalDate data; 
	// Atributo que armazena a hora em que o usuário realiza a votação.
	private LocalTime hora;	 
	// Atributo que registra a descrição caso o usuário realize uma denúncia.
	private String descricao; 
	// Atributo que recebe true (para confirmação) ou false (para denúncia).
	private boolean reacao;  
		
	public Validacao() {} // Método construtor da classe padrão.

	// Anotação que define que o atributo receberá um identificador 
	// automaticamente pelo banco de dados.
	@Id 
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public Usuario getUsuario() {return usuario;}
	public void setUsuario(Usuario usuario) {this.usuario = usuario;}

	// A data de publicação será devolvida do banco de dados
	// no padrão brasileiro.
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public LocalDate getData() {return data;}
	public void setData(LocalDate data) {this.data = data;}

	// A hora de publicação será devolvida do banco de dados com 
	// fuso horário de São Paulo.
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm", 
			timezone="America/Sao_Paulo")
	public LocalTime getHora() {return hora;}
	public void setHora(LocalTime hora) {this.hora = hora;}

	@JsonInclude(Include.NON_NULL)
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public boolean isReacao() {return reacao;}
	public void setReacao(boolean reacao) {this.reacao = reacao;}	
	
	// Sobrescrita do método "hashCode()".
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	// Sobrescrita do método "equals()".
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Validacao other = (Validacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}