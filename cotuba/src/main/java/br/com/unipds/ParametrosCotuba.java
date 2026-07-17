package br.com.unipds;

import java.nio.file.Path;

public class ParametrosCotuba {
	private Path diretorioDosMD;
	private FormatoEBook formato;
	private Path arquivoDeSaida;
	private boolean modoVerboso = false;

	public Path getDiretorioDosMD() {
		return diretorioDosMD;
	}

	public void setDiretorioDosMD(Path diretorioDosMD) {
		this.diretorioDosMD = diretorioDosMD;
	}

	public FormatoEBook getFormato() {
		return formato;
	}

	public void setFormato(FormatoEBook formato) {
		this.formato = formato;
	}

	public Path getArquivoDeSaida() {
		return arquivoDeSaida;
	}

	public void setArquivoDeSaida(Path arquivoDeSaida) {
		this.arquivoDeSaida = arquivoDeSaida;
	}

	public boolean isModoVerboso() {
		return modoVerboso;
	}

	public void setModoVerboso(boolean modoVerboso) {
		this.modoVerboso = modoVerboso;
	}
}
