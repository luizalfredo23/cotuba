package br.com.unipds;

import java.util.List;

public interface RenderizadorMD {
	List<Capitulo> renderizar(List<Markdown> markdownList);

}