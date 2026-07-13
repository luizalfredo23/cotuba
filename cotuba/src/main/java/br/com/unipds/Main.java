package br.com.unipds;

import java.nio.file.Path;
import java.util.List;
import br.com.unipds.GeradorEPUB;
import br.com.unipds.GeradorPDF;
import br.com.unipds.RedenrizadorMD;
import br.com.unipds.CLIOptionsReader;

public class Main {

	void main(String[] args) {
		int exitCode = executar(args);
		if (exitCode != 0) {
			System.exit(exitCode);
		}
	}

	int executar(String[] args) {
	    var cliReader = new CLIOptionsReader();

	    try {
	        cliReader.readOptions(args);

	        Path diretorioDosMD = cliReader.getDiretorioDosMD();
	        var formato = cliReader.getFormato();
	        var arquivoDeSaida = cliReader.getArquivoDeSaida();
	        var modoVerboso = cliReader.isModoVerboso();

	        List<String> contentList = new RedenrizadorMD().renderizar(diretorioDosMD);
	        
	        if ("pdf".equals(formato)) {
	            new GeradorPDF().gerarPDF(contentList, arquivoDeSaida);
	        } else if ("epub".equals(formato)) {
	            new GeradorEPUB().gerarEPUB(contentList, arquivoDeSaida);
	        } else {
	            throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
	        }

	        System.out.println("Arquivo gerado com sucesso: " + arquivoDeSaida);
	        return 0;

	    } catch (Exception ex) {
	        System.err.println(ex.getMessage());
	        if (cliReader.isModoVerboso()) {
	            System.err.println();
	            ex.printStackTrace();
	        }
	        return 1;
	    }
	}

}