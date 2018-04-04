package br.relatai.tcc.domain;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * O objetivo da classe Categoria é manter classificações, a fim de que cada relato
 * criado esteja contido dentro de uma determinada categoria.  
 */
//Esta anotação define o nome da coleção/associação no banco de dados.
@Document(collection = "categoria") 
public class Categoria {
	// Atributo identificador do documento.
	private String id; 
	// Atributo que recebe o nome da categoria.
	private String nome; 
	// Atributo que recebe a descrição do objetivo desta categoria.
	private String descricao;  
	// Atributo de listagem de relatos associados de forma vinculada pelo identificador. 
	@DBRef
	private List<Relato> relatos; 
		
	public Categoria() {} // Método construtor padrão da classe.
	
	// Anotação que define que o atributo receberá um identificador automaticamente.
	@Id 
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}
	
	// Esta anotação oculta a lista de relatos, caso esteja vazia.
	@JsonInclude(Include.NON_NULL) 
	public List<Relato> getRelatos() {return relatos;}
	public void setRelatos(List<Relato> relatos) {this.relatos = relatos;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}