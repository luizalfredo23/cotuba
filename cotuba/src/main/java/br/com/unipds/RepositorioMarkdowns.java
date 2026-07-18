package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public interface RepositorioMarkdowns {
	List<Capitulo> buscar(Path diretorioMD);
}