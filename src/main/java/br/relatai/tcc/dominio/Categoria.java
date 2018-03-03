package br.relatai.tcc.dominio;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "categoria")
public class Categoria {

	private String id;	
	private String nome;
	private String descricao;
	private boolean situacao;
	private LocalDate dataSolicitacao;
	@DBRef
	private List<Relato> relatos;
	
	public Categoria() {}
	
	@Id
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}	
	
	public boolean isSituacao() {return situacao;}
	public void setSituacao(boolean situacao) {this.situacao = situacao;}
	
	@JsonInclude(Include.NON_NULL) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")	
	@Field("data_solicitacao")
	public LocalDate getDataSolicitacao() {return dataSolicitacao;}
	public void setDataSolicitacao(LocalDate dataSolicitacao) {this.dataSolicitacao = dataSolicitacao;}
	
	@JsonInclude(Include.NON_NULL)
	public List<Relato> getRelatos() {return relatos;}
	public void setRelatos(List<Relato> relatos) {this.relatos = relatos;}		
}