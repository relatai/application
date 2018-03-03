package br.relatai.tcc.dominio;

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

@Document(collection = "relato")
public class Relato {
	
	private String id;
	@DBRef
	private List<Usuario> usuario;
	private LocalDate dataPublicacao;		
	private LocalTime horaPublicacao;		
	private String descricao;		
	private Double latitude;		
	private Double longitude;		
	private String foto;	
	private int confirmado;	
	private int denunciado;	
	private List<Validacao> validacoes;
	
	public Relato() {}

	@Id
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}		
	
	public List<Usuario> getUsuario() {return usuario;}
	public void setUsuario(List<Usuario> usuario) {this.usuario = usuario;}
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Field("data_publicacao")
	public LocalDate getDataPublicacao() {return dataPublicacao;}
	public void setDataPublicacao(LocalDate dataPublicacao) {this.dataPublicacao = dataPublicacao;}
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "America/Sao_Paulo")
	@Field("hora_publicacao")
	public LocalTime getHoraPublicacao() {return horaPublicacao;}
	public void setHoraPublicacao(LocalTime horaPublicacao) {this.horaPublicacao = horaPublicacao;}
	
	@JsonInclude(Include.NON_NULL)
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
}
