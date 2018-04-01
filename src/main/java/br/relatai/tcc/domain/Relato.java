package br.relatai.tcc.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "relato") //Esta anotação define o nome da coleção associada no banco de dados.
public class Relato {
	
	private String id; // Atributo identificador do documento.
	@DBRef
	private List<Usuario> usuario; // Este atributo foi definida como listagem para receber somente a vinculação do identificador.
	private LocalDate dataPublicacao; // Este atributo recebe a data de criação do relato.
	private LocalTime horaPublicacao; // Este atributo recebe a hora de criação do relato.
	private String descricao; // Neste atributo, o usuário descreve o problema. 
	private Double latitude; // Este atributo recebe a latitude do celular do usuário.
	private Double longitude; // Este atributo recebe a longitude do celular do usuário.
	private String foto; // O atributo foto armazena a URL da imagem associada no Cloudinary.
	private int confirmado;	// Este atributo recebe a contagem de reações positivas realizadas por demais usuários.  
	private int denunciado;	// Este atributo recebe a contagem de reações negativas realizadas por demais usuários.	
	@DBRef
	private List<Validacao> validacoes; // Atributo de vinculação de listagem das validações (reações) realizadas pelos usuários.
		
	public Relato() {} // Método construtor da classe padrão.

	@Id // Anotação que define que o atributo receberá um identificador automaticamente pelo banco de dados.
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}		
	
	public List<Usuario> getUsuario() {return usuario;}
	public void setUsuario(List<Usuario> usuario) {this.usuario = usuario;}
	
	// A anotação @Field define o nome do atributo no banco de dados.
	// A data de publicação será devolvida do banco de dados no padrão brasileiro.	
	@Field("data_publicacao")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")	
	public LocalDate getDataPublicacao() {return dataPublicacao;}
	public void setDataPublicacao(LocalDate dataPublicacao) {this.dataPublicacao = dataPublicacao;}
	
	// A hora de publicação será devolvida do banco de dados com fuso horário de São Paulo.
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "America/Sao_Paulo")
	@Field("hora_publicacao")
	public LocalTime getHoraPublicacao() {return horaPublicacao;}
	public void setHoraPublicacao(LocalTime horaPublicacao) {this.horaPublicacao = horaPublicacao;}
	
	@JsonInclude(Include.NON_NULL) // Se o atributo estiver nula não será exibida.
	public int getConfirmado() {return confirmado;}
	public void setConfirmado(int confirmado) {this.confirmado = confirmado;}

	@JsonInclude(Include.NON_NULL)
	public int getDenunciado() {return denunciado;}
	public void setDenunciado(int denunciado) {this.denunciado = denunciado;}
	
	@JsonInclude(Include.NON_NULL)
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Double getLatitude() {return latitude;}
	public void setLatitude(Double latitude) {this.latitude = latitude;}

	public Double getLongitude() {return longitude;}
	public void setLongitude(Double longitude) {this.longitude = longitude;}

	public String getFoto() {return foto;}
	public void setFoto(String foto) {this.foto = foto;}
	
	@JsonInclude(Include.NON_NULL)
	public List<Validacao> getValidacoes() {return validacoes;}
	public void setValidacoes(List<Validacao> validacoes) {this.validacoes = validacoes;}

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
		Relato other = (Relato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
