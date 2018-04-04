package br.relatai.tcc.services;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import br.relatai.tcc.domain.Relato;
import br.relatai.tcc.util.StaticGenericConstantResources;

/**
 * 
 * @author jether.rodrigues
 * 
 *         A classe usa o padrão Fluent Interface e possui a única
 *         responsabilidade de receber um StringBase64, converter para um aquivo
 *         e realizar o upload na nuvem utilizando a API da Cloudinary.
 * 
 *         10/03/2018
 */
@Component
public final class ConvertBase64AndUploadToCloudinaryImageService {
	// Criação de uma constante do tipo Logger baseada na classe "ConvertBase64AndUploadToCloudinaryImageService".
	private static final Logger logger = LoggerFactory.getLogger(ConvertBase64AndUploadToCloudinaryImageService.class);

	// Variáveis de atribuição.
	private String stringImageBase64;
	private String cloudinaryImagemUrl;
	private String uuidFileName;
	private File convertedToFile;
	private Map<String, String> config;

	public ConvertBase64AndUploadToCloudinaryImageService() {
		config = new HashMap<>(); // Cria-se um mapa de strings.
		config.put("cloud_name", StaticGenericConstantResources.CLOUD_NAME); // Atribuímos a primeira chave/valor.
		config.put("api_key", StaticGenericConstantResources.API_KEY); // Atribuímos a segunda chave/valor.
		config.put("api_secret", StaticGenericConstantResources.API_SECRET);  // Atribuímos a terceira chave/valor.
	}
	
	// Método público que recebe o nome da imagem convertida em base64.
	public ConvertBase64AndUploadToCloudinaryImageService mePassaStringBase64(String stringImageBase64) {
		// A variável "uuidFileName" receberá um identificador gerado aleatoriamente.
		this.uuidFileName = UUID.randomUUID().toString().replaceAll("-", "");
		this.stringImageBase64 = stringImageBase64; // Atribuímos o parâmetro recebido na variável "stringImageBase64".
		return this; // Retorna o próprio método.
	}

	// Método público que invoca o método "converterImagemAPartirDeStringBase64()".
	public ConvertBase64AndUploadToCloudinaryImageService ireiConverter() {
		// É atribuída a variável "convertedToFile" o resultado da construção do arquivo.
		this.convertedToFile = converterImagemAPartirDeStringBase64();
		return this; // Retorna o próprio método.
	}

	// Método público que realiza o upload do arquivo para nuvem.
	public ConvertBase64AndUploadToCloudinaryImageService realizarUpload() throws IOException {
		fazerUpload(); // Este método é invocado, pois ele de fato realizará o upload. 
		return this; // Retorna o próprio método.
	}

	// Método público que retorna a url da imagem gerada no Cloudinary.
	public String eRetornarUrlGeradaAposUpload() {
		return this.cloudinaryImagemUrl;
	}
	
	// Método auxiliar privado que realiza a conexão com o Cloudinary e envia a imagem.
	private void fazerUpload() throws IOException {
		int versao = LocalDate.now().getYear();	// Atribuímos a variável "versao" o ano corrente.		
		// O método "configurarConta()" é invocado e passado como parâmetro para o construtor da classe.
		Cloudinary cloudinary = new Cloudinary(config); 
		logger.info("File: " + this.uuidFileName); // Logger do tipo info é escrito com a identificação do arquivo.
		// É criado um mapa de parâmetros que define propriedades da imagem. 
		@SuppressWarnings("rawtypes")
		// O "ObjectUtils" define as propriedades da imagem, a exemplo, identificação pública da imagem e 
		// o tamanho que será armazenado.
		Map params = ObjectUtils.asMap("public_id", this.uuidFileName, 
				"resource_type", "auto", 
				"use_filename", true,
				"transformation", new Transformation().width(400).height(400).crop("limit"),
				"version", versao);
		// Comandos de envio da imagem ao Cloudinary.
		cloudinary.uploader().upload(this.convertedToFile, params);
		cloudinary.url().version(versao).generate(this.convertedToFile.getName());		
		// Atribui-se a variável "cloudinaryImagemUrl" a concatenação da constante "URL_UPLOADED"
		// com o arquivo da imagem gerado.
		this.cloudinaryImagemUrl = StaticGenericConstantResources.URL_UPLOADED.concat(this.convertedToFile.getName());
	}
	
	// Método público que remove no Cloudinary a imagem associada a um relato. 
	public void removerImagem(Relato relato) throws Exception {		
		// Instancia um objeto "cloudinary" com as configurações da conta.
		Cloudinary cloudinary = new Cloudinary(config); 
		// Recuperação do "public_id" extraído da URL salva no registro de relato. 
		String publicId = relato.getFoto().substring(relato.getFoto().lastIndexOf("/")+1, relato.getFoto().lastIndexOf("."));
		// Remoção da imagem no Cloudinary através do "public_id" recuperado na linha anterior.
		cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	}

	// O método privado devolve um arquivo gerado a partir de uma string base64.
	private File converterImagemAPartirDeStringBase64() {
		// Atribui o valor de "stringImageBase64" removendo uma contra-barra onde encontrar
		// duas contra-barras no array "base64".
		String[] base64 = this.stringImageBase64.split("\\,");  
		String extensao = base64[0].split("\\;")[0].split("\\/")[1]; // Recupera a extensão da imagem.
		// A variável "tmpDir" recebe o caminho do diretório temporário do Sistema Operacional.
		String tmpDir = System.getProperty("java.io.tmpdir"); 
		// A variável "path" recebe a construção do qualified name da imagem.
		String path = tmpDir.concat(File.separator).concat(this.uuidFileName).concat(".").concat(extensao);
		File upload = new File(path); // Cria um arquivo e recebe por parâmetro a variável "path".
		upload.setExecutable(true); // Define que o arquivo pode ser executável.
		upload.setReadable(true); // Define que o arquivo pode ser lido.
		upload.setWritable(true);  // Define que o arquivo pode ser gravável.
		FileOutputStream outputStream = null; // Cria-se uma referência do tipo "FileOutputStream". 
		try {
			byte bArray[] = Base64.decodeBase64(base64[1]); // Decodifica a String em base64.
			outputStream = new FileOutputStream(upload); // Atribui o arquivo "upload" ao objeto "outputStream".
			outputStream.write(bArray); // Grava o arquivo "bArray".
		} catch (IOException e) {
			logger.error(e.getMessage()); // Se ocorrer algum erro um log de erro será gerado.
		} finally {
			try {
				outputStream.close(); // Fecha o conexão com o arquivo.
			} catch (IOException e) {
				logger.error(e.getMessage()); // Se ocorrer algum erro um log de erro será gerado.
			}
		}
		return upload; // Retorna o arquivo gerado.
	}
}
