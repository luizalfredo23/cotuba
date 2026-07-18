package br.com.unipds;

import static br.com.unipds.FormatoEBook.EPUB;
import static br.com.unipds.FormatoEBook.PDF;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
public class CotubaService {
	private final RenderizadorMD redenrizadorMD;
	private final LeitorPropriedadesEbook leitorPropriedades;
	private final RepositorioMarkdowns repositorioMDS;
	private final GeradorEbook geradorPDF;
	private final GeradorEbook geradorEPUB;
	
	@Inject
	public CotubaService(RenderizadorMD redenrizadorMD, LeitorPropriedadesEbook leitorPropriedades,@Named("geradorPDF") GeradorEbook geradorPDF, @Named("geradorEPUB") GeradorEbook geradorEPUB) {
		this.redenrizadorMD = redenrizadorMD;
		this.leitorPropriedades = leitorPropriedades;
		this.repositorioMDS = new RepositorioMarkdownsDiretorio();
		this.geradorPDF = geradorPDF;
		this.geradorEPUB = geradorEPUB;
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
        
        GeradorEbook geradorEbook;
        
        if (PDF.equals(ebook.getFormato())) {
        	geradorEbook = geradorPDF;
        } else if (EPUB.equals(ebook.getFormato())) {
            geradorEbook = geradorEPUB;
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
        }

        geradorEbook.gerar(ebook);
        
        System.out.println("Arquivo gerado com sucesso: " + ebook.getArquivoDeSaida().toAbsolutePath());
	}
}
