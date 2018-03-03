package br.relatai.tcc.dominio;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection="usuario")
public class Usuario {

	private String id;
	private String celular;	
	private LocalDate dataCadastro;
	
	public Usuario() {}
	
	public Usuario(String id, String celular, LocalDate dataCadastro) {
		this.id = id;
		this.celular = celular;
		this.dataCadastro = dataCadastro;
	}
	
	@Id
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}		
	
	public String getCelular() {return celular;}
	public void setCelular(String celular) {this.celular = celular;}
			
	@Field("data_cadastro") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")	
	public LocalDate getDataCadastro() {return dataCadastro;}
	public void setDataCadastro(LocalDate dataCadastro) {this.dataCadastro = dataCadastro;}	
}