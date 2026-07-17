package br.com.unipds;

import java.nio.file.Path;

public class Main {

	void main(String[] args) {
		int exitCode = executar(args);
		if (exitCode != 0) {
			System.exit(exitCode);
		}
	}

	int executar(String[] args) {
	    var cliReader = new CLIOptionsReader();
	    
	    boolean modoVerboso = false;
	    
	    try {
	    	ParametrosCotuba parametrosCotuba = cliReader.readOptions(args);
//
//	        Path diretorioDosMD = parametrosCotuba.getDiretorioDosMD();
//	        var formato = parametrosCotuba.getFormato();
//	        var arquivoDeSaida = parametrosCotuba.getArquivoDeSaida();
//	        modoVerboso = parametrosCotuba.isModoVerboso();
	        
	        new CotubaService().executar(parametrosCotuba);

	        return 0;

	    } catch (Exception ex) {
	        System.err.println(ex.getMessage());
	        if (modoVerboso) {
	            System.err.println();
	            ex.printStackTrace();
	        }
	        return 1;
	    }
	}

}