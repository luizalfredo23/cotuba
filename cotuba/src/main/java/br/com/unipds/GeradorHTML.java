package br.com.unipds;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@FormatoEbookQualifier(FormatoEbook.HTML)
public class GeradorHTML implements GeradorEbook {
	public void gerar(Ebook ebook) {
		Path arquivoSaida = ebook.arquivoDeSaida();
		
		try {
			Path diretorioSaida = Files.createDirectory(arquivoSaida);
			
			Map<Capitulo, Path> capitulosMap = new HashMap<>();
			
			int index = 1;
			for (Capitulo capitulo : ebook.conteudo()) {
				String nomeCapitulo = nomeCapitulo(index, capitulo);
				System.out.println("Gerando capítulo: " + nomeCapitulo);
				
				Path arquivoCapitulo = diretorioSaida.resolve(nomeCapitulo);
				
				escreverArquivo(arquivoCapitulo, capitulo);

				capitulosMap.put(capitulo, arquivoCapitulo);
				
				index++;
			}
			
			escreveSumario(diretorioSaida, ebook, capitulosMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Erro ao ebook HTML: " + arquivoSaida, e);
		}
	}

	private void escreveSumario(Path diretorioSaida, Ebook ebook, Map<Capitulo, Path> capitulosMap) throws IOException {
		String summary =ebook.conteudo().stream().map(capitulo -> {
			return String.format(""
					+ "<li>"
					+ "	<a href=\"%s\">%s</a>"
					+ "</li>", capitulosMap.get(capitulo).getFileName(), capitulo.titulo());
		}).collect(joining("\n"));
		
		String sumarioHtml = """
				  <html xmlns="http://www.w3.org/1999/xhtml">
				    <head>
				      <title>%s</title>
				    </head>
				    <body>
				      <h1>%s</h1>
				      <h2>Por: %s</h2>
				      <h3>Sumário</h3>
				      <ul>
				      	%s
				      </ul> 
				    </body>
				  </html>
				""".formatted(ebook.titulo(), ebook.titulo(), ebook.autor(), summary);
		
		
		Path arquivoIndex = diretorioSaida.resolve("index.html");
		Files.writeString(arquivoIndex, sumarioHtml, StandardCharsets.UTF_8);
		
	}

	private void escreverArquivo(Path arquivoCapitulo, Capitulo capitulo) throws IOException {
		String epubHtml = """
				  <html xmlns="http://www.w3.org/1999/xhtml">
				    <head>
				      <title>%s</title>
				    </head>
				    <body>
				      %s
				    </body>
				  </html>
				""".formatted(capitulo.titulo(), capitulo.html());
		
		Files.writeString(arquivoCapitulo, epubHtml, StandardCharsets.UTF_8);
	}

	private String nomeCapitulo(int index, Capitulo capitulo) {
		String titulo = capitulo.titulo().toLowerCase().replaceAll("\\W", "");
		return "%02d-%s.html".formatted(index, titulo);
	}
}
