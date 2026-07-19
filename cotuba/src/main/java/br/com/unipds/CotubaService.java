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
		var diretorioDosMD = parametros.getDiretorioDosMD();
		var formato = parametros.getFormato();
		var arquivoDeSaida = parametros.getArquivoDeSaida();
		
		Ebook ebook = new Ebook();
		
		leitorPropriedades.ler(diretorioDosMD, ebook);
		
		List<Capitulo> capitulos = repositorioMDS.buscar(diretorioDosMD);
		
		
        ebook.setFormato(formato);
        ebook.setArquivoDeSaida(arquivoDeSaida);
        ebook.setConteudo(capitulos);
        
        
        redenrizadorMD.renderizar(capitulos);
        
        GeradorEbook geradorEbook = geradoresEbook.select(FormatoEbookFilter.of(formato)).get();

        geradorEbook.gerar(ebook);
        
        System.out.println("Arquivo gerado com sucesso: " + ebook.getArquivoDeSaida().toAbsolutePath());
	}
}
