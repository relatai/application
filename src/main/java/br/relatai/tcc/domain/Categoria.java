package br.relatai.tcc.domain;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*
 * O objetivo da classe Categoria é manter classificações, a fim de que cada relato
 * criado esteja contido dentro de uma determinada categoria.  
 */

@Document(collection = "categoria") // Esta anotação define o nome da coleção/associação no banco de dados.
public class Categoria {
	
	private String id; // Atributo identificador do documento.
	private String nome; // Atributo que recebe o nome da categoria.
	private String descricao; // Atributo que recebe a descrição do objetivo desta categoria. 
	@DBRef
	private List<Relato> relatos; // Atributo de listagem de relatos associados de forma vinculada pelo identificador.  
		
	public Categoria() {} // Método construtor padrão da classe.
	
	@Id // Anotação que define que o atributo receberá um identificador automaticamente pelo banco de dados.
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}
		
	@JsonInclude(Include.NON_NULL) // Esta anotação oculta a lista de relatos, caso esteja vazia.
	public List<Relato> getRelatos() {return relatos;}
	public void setRelatos(List<Relato> relatos) {this.relatos = relatos;}		
}