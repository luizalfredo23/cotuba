package br.com.unipds;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

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
	    
	    try (SeContainer container = SeContainerInitializer.newInstance().initialize()){
	    	ParametrosCotuba parametrosCotuba = cliReader.readOptions(args);
//
//	        Path diretorioDosMD = parametrosCotuba.getDiretorioDosMD();
//	        var formato = parametrosCotuba.getFormato();
//	        var arquivoDeSaida = parametrosCotuba.getArquivoDeSaida();
//	        modoVerboso = parametrosCotuba.isModoVerboso();
	        
	        container.select(CotubaService.class).get().executar(parametrosCotuba);

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