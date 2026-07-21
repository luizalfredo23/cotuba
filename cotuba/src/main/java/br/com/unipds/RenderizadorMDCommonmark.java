package br.com.unipds;

import java.util.List;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RenderizadorMDCommonmark implements RenderizadorMD {

	@Inject
	public RenderizadorMDCommonmark(RepositorioMarkdowns repositorioMDS) {
		super();
	}

	@Override
	public List<Capitulo> renderizar(List<Markdown> markdownList) {
		return markdownList.stream().map(markdown -> {
			CapituloBuilder capituloBuilder = CapituloBuilder.builder();
			
			Parser parser = Parser.builder().build();
			Node document = null;
			
			capituloBuilder.markdown(markdown);
			
			try {
				String markdownConteudo = markdown.conteudo();

				document = parser.parse(markdownConteudo);
				document.accept(new AbstractVisitor() {

					@Override
					public void visit(Heading heading) {
						if (heading.getLevel() == 1) {
							// capítulo
							String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
							capituloBuilder.titulo(tituloDoCapitulo);
						} else if (heading.getLevel() == 2) {
							// seção
						} else if (heading.getLevel() == 3) {
							// título
						}
					}

				});
				
				

			} catch (Exception ex) {
				throw new IllegalStateException("Erro ao fazer parse do arquivo " + markdown.arquivo(), ex);
			}

			try {
				HtmlRenderer renderer = HtmlRenderer.builder().build();
				String html = renderer.render(document);

				capituloBuilder.html(html);
			} catch (Exception ex) {
				throw new IllegalStateException(
						"Erro ao renderizar para HTML o arquivo " + markdown.arquivo(), ex);
			}
			
			return capituloBuilder.build();
		}).toList();
	}

}