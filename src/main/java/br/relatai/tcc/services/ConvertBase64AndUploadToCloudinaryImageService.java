package br.relatai.tcc.services;

import java.io.*;
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
	private static final Logger logger = LoggerFactory.getLogger(ConvertBase64AndUploadToCloudinaryImageService.class);

	private String stringImageBase64;
	private String cloudinaryImagemUrl;
	private String uuidFileName;
	private File convertedToFile;

	public ConvertBase64AndUploadToCloudinaryImageService mePassaStringBase64(String stringImageBase64) {
		this.uuidFileName = UUID.randomUUID().toString().replaceAll("-", "");
		this.stringImageBase64 = stringImageBase64;
		return this;
	}

	public ConvertBase64AndUploadToCloudinaryImageService ireiConverter() {
		this.convertedToFile = converterImagemAPartirDeStringBase64();
		return this;
	}

	public ConvertBase64AndUploadToCloudinaryImageService realizarUpload() throws IOException {
		fazerUpload();
		return this;
	}

	public String eRetornarUrlGeradaAposUpload() {
		return this.cloudinaryImagemUrl;
	}

	private void fazerUpload() throws IOException {
		int versao = Calendar.YEAR;

		Map<String, String> config = new HashMap<>();
		config.put("cloud_name", StaticGenericConstantResources.CLOUD_NAME);
		config.put("api_key", StaticGenericConstantResources.API_KEY);
		config.put("api_secret", StaticGenericConstantResources.API_SECRET);

		Cloudinary cloudinary = new Cloudinary(config);

		logger.info("File: " + this.uuidFileName);

		@SuppressWarnings("rawtypes")
		Map params = ObjectUtils.asMap("public_id", this.uuidFileName, 
				"resource_type", "auto", 
				"use_filename", true,
				"transformation", new Transformation().width(400).height(400).crop("limit"),
				"version", versao);

		cloudinary.uploader().upload(this.convertedToFile, params);
		cloudinary.url().version(versao).generate(this.convertedToFile.getName());
		
		this.cloudinaryImagemUrl = StaticGenericConstantResources.URL_UPLOADED + this.convertedToFile.getName();
	}
	
	public void removerImagem(Relato relato) throws Exception {
		Map<String, String> config = new HashMap<>();
		config.put("cloud_name", StaticGenericConstantResources.CLOUD_NAME);
		config.put("api_key", StaticGenericConstantResources.API_KEY);
		config.put("api_secret", StaticGenericConstantResources.API_SECRET);

		Cloudinary cloudinary = new Cloudinary(config);
		String publicId = relato.getFoto().substring(relato.getFoto().lastIndexOf("/")+1, relato.getFoto().lastIndexOf("."));		
		cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	}

	private File converterImagemAPartirDeStringBase64() {
		String[] base64 = this.stringImageBase64.split("\\,");
		String extensao = base64[0].split("\\;")[0].split("\\/")[1];

		String tmpDir = System.getProperty("java.io.tmpdir");

		String path = tmpDir + File.separator + this.uuidFileName + "." + extensao;
		File upload = new File(path);
		upload.setExecutable(true);
		upload.setReadable(true);
		upload.setWritable(true);

		FileOutputStream outputStream = null;
		try {
			byte bArray[] = Base64.decodeBase64(base64[1]);
			outputStream = new FileOutputStream(upload);
			outputStream.write(bArray);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return upload;
	}
}
