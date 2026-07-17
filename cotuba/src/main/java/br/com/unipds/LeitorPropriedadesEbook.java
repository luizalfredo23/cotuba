package br.com.unipds;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Properties;

public class LeitorPropriedadesEbook {
	public void ler(Path diretorioMD, Ebook ebook) {

		Path arquivoProperties = diretorioMD.resolve("ebook.properties");

		if (arquivoProperties.toFile().exists()) {
			Properties properties = new Properties();

			try (var inputStream = java.nio.file.Files.newBufferedReader(arquivoProperties, StandardCharsets.UTF_8)) {
				properties.load(inputStream);

				ebook.setTitulo(properties.getProperty("cotuba.ebook.titulo"));
				ebook.setAutor(properties.getProperty("cotuba.ebook.autor"));
			} catch (Exception ex) {
				throw new IllegalStateException("Erro ao ler o arquivo " + arquivoProperties, ex);
			}

		} else {
			throw new IllegalArgumentException(
					"Arquivo ebook.properties não encontrado em: " + diretorioMD.toAbsolutePath());
		}
	}
}
