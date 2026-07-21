package br.com.unipds;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LeitorPropriedadesEbookArquivo implements LeitorPropriedadesEbook {
	@Override
	public PropriedadesEbook ler(Path diretorioMD) {

		Path arquivoProperties = diretorioMD.resolve("ebook.properties");

		if (arquivoProperties.toFile().exists()) {
			Properties properties = new Properties();

			try (var inputStream = java.nio.file.Files.newBufferedReader(arquivoProperties, StandardCharsets.UTF_8)) {
				properties.load(inputStream);
				
				return new PropriedadesEbook(properties.getProperty("cotuba.ebook.titulo"),properties.getProperty("cotuba.ebook.autor") );
			} catch (Exception ex) {
				throw new IllegalStateException("Erro ao ler o arquivo " + arquivoProperties, ex);
			}

		} else {
			throw new IllegalArgumentException(
					"Arquivo ebook.properties não encontrado em: " + diretorioMD.toAbsolutePath());
		}
	}
}
