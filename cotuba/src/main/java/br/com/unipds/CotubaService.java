package br.com.unipds;

import static br.com.unipds.FormatoEBook.EPUB;
import static br.com.unipds.FormatoEBook.PDF;

import java.util.List;

public class CotubaService {
	public void executar(ParametrosCotuba parametros) {
		var diretorioDosMD = parametros.getDiretorioDosMD();
		var formato = parametros.getFormato();
		var arquivoDeSaida = parametros.getArquivoDeSaida();
		
		Ebook ebook = new Ebook();
		
		LeitorPropriedadesEbook leitorPropriedades = new LeitorPropriedadesEbook();
		leitorPropriedades.ler(diretorioDosMD, ebook);
		
        ebook.setFormato(formato);
        ebook.setArquivoDeSaida(arquivoDeSaida);
        
        List<Capitulo> contentList = new RedenrizadorMD().renderizar(diretorioDosMD);
        
        ebook.setConteudo(contentList);
        
        
        
        
        if (PDF.equals(ebook.getFormato())) {
            new GeradorPDF().gerarPDF(ebook);
        } else if (EPUB.equals(ebook.getFormato())) {
            new GeradorEPUB().gerarEPUB(ebook);
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
        }

        System.out.println("Arquivo gerado com sucesso: " + ebook.getArquivoDeSaida().toAbsolutePath());
        
	}
}
