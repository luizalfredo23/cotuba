package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class RedenrizadorMD {
	public List<Capitulo> renderizar(Path diretoriosMDS) {

		var repositorioMDS = new RepositorioMarkdowns();

		List<Capitulo> capitulos = repositorioMDS.buscar(diretoriosMDS);

		return capitulos.stream().map(capitulo -> {
			Parser parser = Parser.builder().build();
			Node document = null;
			try {
				String markdown = capitulo.getMarkdown();

				document = parser.parse(markdown);
				document.accept(new AbstractVisitor() {

					@Override
					public void visit(Heading heading) {
						if (heading.getLevel() == 1) {
							// capítulo
							String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
							capitulo.setTitulo(tituloDoCapitulo);
						} else if (heading.getLevel() == 2) {
							// seção
						} else if (heading.getLevel() == 3) {
							// título
						}
					}

				});

			} catch (Exception ex) {
				throw new IllegalStateException("Erro ao fazer parse do arquivo " + capitulo.getArquivoMarkdown(), ex);
			}

			try {
				HtmlRenderer renderer = HtmlRenderer.builder().build();
				String html = renderer.render(document);

				capitulo.setHtml(html);

				return capitulo;

			} catch (Exception ex) {
				throw new IllegalStateException(
						"Erro ao renderizar para HTML o arquivo " + capitulo.getArquivoMarkdown(), ex);
			}
		}).toList();

	}

}