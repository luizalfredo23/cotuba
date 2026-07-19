package br.com.unipds;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

@ApplicationScoped
@FormatoEbookQualifier(FormatoEbook.EPUB)
public class GeradorEPUB implements GeradorEbook {
	public void gerar(Ebook ebook) {
		
		List<Capitulo> capitulos = ebook.getConteudo();
		Path arquivoSaida = ebook.getArquivoDeSaida();

		try {
			var epub = new Book();

			epub.getMetadata().addTitle(ebook.getTitulo());
			epub.getMetadata().addAuthor(new Author(ebook.getAutor()));

			boolean[] ehPrimeiroCapitulo = { true };

			capitulos.forEach(capitulo ->{
				String epubHtml = """
						  <html xmlns="http://www.w3.org/1999/xhtml">
						    <head>
						      <title>%s</title>
						    </head>
						    <body>
						      %s
						    </body>
						  </html>
						""".formatted(capitulo.getTitulo(), capitulo.getHtml());
				var chapter = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
				epub.addSection(capitulo.getTitulo(), chapter);

				if (ehPrimeiroCapitulo[0]) {
					epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
					ehPrimeiroCapitulo[0] = false;
				}
				
			});
				

						

					
				

			var epubWriter = new EpubWriter();

			try {
				epubWriter.write(epub, Files.newOutputStream(arquivoSaida));
			} catch (IOException ex) {
				throw new IllegalStateException(
						"Erro ao criar arquivo EPUB: " + arquivoSaida.toAbsolutePath(), ex);
			}

		} catch (Exception ex) {
			throw new IllegalStateException("Erro ao gerar EPUB: " + arquivoSaida.toAbsolutePath(), ex);
		}

	}
}
