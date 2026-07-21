package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public interface RepositorioMarkdowns {
	List<Markdown> buscar(Path diretorioMD);
}