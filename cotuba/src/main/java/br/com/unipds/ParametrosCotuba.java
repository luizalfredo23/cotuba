package br.com.unipds;

import java.nio.file.Path;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record ParametrosCotuba(Path diretorioDosMD, FormatoEbook formato, Path arquivoDeSaida, boolean modoVerboso) {
}
