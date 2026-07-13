package br.com.unipds;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class RedenrizadorMD {
	public List<String> renderizar(Path diretorioMD) {
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
		List<String> resultado = new ArrayList<>();

		try (Stream<Path> streamMDs = Files.list(diretorioMD)) {
			List<Path> arquivosMD = streamMDs.filter(matcher::matches).sorted().toList();

			if (arquivosMD.isEmpty()) {
				throw new IllegalStateException(
						"Não foram encontrados capítulos (arquivos .md) no diretório: "
								+ diretorioMD.toAbsolutePath());
			}

			return arquivosMD.stream().map(arquivoMD -> {
				Parser parser = Parser.builder().build();
				Node document = null;
				try {
					document = parser.parseReader(Files.newBufferedReader(arquivoMD));
					document.accept(new AbstractVisitor() {

						@Override
						public void visit(Heading heading) {
							if (heading.getLevel() == 1) {
								// capítulo
								String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
								// TODO: usar título do capítulo
							} else if (heading.getLevel() == 2) {
								// seção
							} else if (heading.getLevel() == 3) {
								// título
							}
						}

					});

				} catch (Exception ex) {
					throw new IllegalStateException("Erro ao fazer parse do arquivo " + arquivoMD, ex);
				}

				try {
					HtmlRenderer renderer = HtmlRenderer.builder().build();
					return renderer.render(document);
				} catch (Exception ex) {
					throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD, ex);
				}
			}).toList();

		} catch (IOException ex) {
			throw new IllegalStateException(
					"Erro tentando encontrar arquivos .md em " + diretorioMD.toAbsolutePath(), ex);
		}
	}

}