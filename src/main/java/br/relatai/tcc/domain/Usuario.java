package br.relatai.tcc.domain;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * A existência de usuários cadastrados tem a finalidade de controlarmos os papéis
 * que cada usuário pode exercer no uso da aplicação, hora relator, hora votante ou anônimo.  
 */

@Document(collection="usuario") //Esta anotação define o nome da coleção associada no banco de dados.
public class Usuario {

	private String id; // Atributo identificador do documento.
	private String celular;	 // Atributo que recebe o número do celular cifrado em Base64.
	private LocalDate dataCadastro;  // Atributo que armazena a data em que o usuário realizou o cadastro.
	
	public Usuario() {} // Método construtor da classe padrão.
	
	// Método construtor da classe parametrizado.
	public Usuario(String id, String celular, LocalDate dataCadastro) {
		this.id = id;
		this.celular = celular;
		this.dataCadastro = dataCadastro;
	}
	
	@Id // Anotação que define que a propriedade receberá um identificador automaticamente pelo banco de dados.
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}		
	
	public String getCelular() {return celular;}
	public void setCelular(String celular) {this.celular = celular;}
	
	// A anotação @Field define o nome do atributo no banco de dados.
	// A data de publicação será devolvida do banco de dados no padrão brasileiro.	
	@Field("data_cadastro") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")	
	public LocalDate getDataCadastro() {return dataCadastro;}
	public void setDataCadastro(LocalDate dataCadastro) {this.dataCadastro = dataCadastro;}	
}