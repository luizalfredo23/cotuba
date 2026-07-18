package br.com.unipds;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RepositorioMarkdownsDiretorio implements RepositorioMarkdowns {
	@Override
	public List<Capitulo> buscar(Path diretorioMD) {
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

		try (Stream<Path> streamMDs = Files.list(diretorioMD)) {
			List<Path> arquivosMD = streamMDs.filter(matcher::matches).sorted().toList();

			if (arquivosMD.isEmpty()) {
				throw new IllegalStateException(
						"Não foram encontrados capítulos (arquivos .md) no diretório: "
								+ diretorioMD.toAbsolutePath());
			}
			
			return arquivosMD.stream().map(arquivoMD ->{
				var capitulo = new Capitulo();
				
				String markdown;
				try {
					markdown = Files.readString(arquivoMD);
					capitulo.setMarkdown(markdown);
				} catch (IOException e) {
					throw new IllegalStateException("Erro ao ler o arquivo " + arquivoMD.toAbsolutePath(), e);
				}
				
				capitulo.setArquivoMarkdown(arquivoMD);
				return capitulo;
				
			}).toList();
			
		} catch (IOException ex) {
			throw new IllegalStateException(
					"Erro tentando encontrar arquivos .md em " + diretorioMD.toAbsolutePath(), ex);
		}
	}

}
