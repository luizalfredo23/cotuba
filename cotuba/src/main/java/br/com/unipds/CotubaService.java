package br.com.unipds;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class CotubaService {
	private final RenderizadorMD redenrizadorMD;
	private final LeitorPropriedadesEbook leitorPropriedades;
	private final RepositorioMarkdowns repositorioMDS;
	private final Instance<GeradorEbook> geradoresEbook;
	
	@Inject
	public CotubaService(RenderizadorMD redenrizadorMD, LeitorPropriedadesEbook leitorPropriedades, @Any Instance<GeradorEbook> geradoresEbook) {
		this.redenrizadorMD = redenrizadorMD;
		this.leitorPropriedades = leitorPropriedades;
		this.repositorioMDS = new RepositorioMarkdownsDiretorio();
		this.geradoresEbook = geradoresEbook;
	}

	public void executar(ParametrosCotuba parametros) {
		var diretorioDosMD = parametros.diretorioDosMD();
		var formato = parametros.formato();
		var arquivoDeSaida = parametros.arquivoDeSaida();
		
		EbookBuilder ebookBuilder = EbookBuilder.builder();
		
		PropriedadesEbook propriedadesEbook = leitorPropriedades.ler(diretorioDosMD);
		
		List<Markdown> markdownList = repositorioMDS.buscar(diretorioDosMD);
		
        ebookBuilder.formato(formato);
        ebookBuilder.arquivoDeSaida(arquivoDeSaida);
        ebookBuilder.conteudo(redenrizadorMD.renderizar(markdownList));
        ebookBuilder.titulo(propriedadesEbook.titulo());
        ebookBuilder.autor(propriedadesEbook.autor());
        
        Ebook ebook = ebookBuilder.build();

        GeradorEbook geradorEbook = geradoresEbook.select(FormatoEbookFilter.of(ebook.formato())).get();

		geradorEbook.gerar(ebook);
        
        System.out.println("Arquivo gerado com sucesso: " + ebookBuilder.arquivoDeSaida().toAbsolutePath());
	}
}
