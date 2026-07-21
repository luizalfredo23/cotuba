package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Ebook(String titulo, String autor, FormatoEbook formato, List<Capitulo> conteudo, Path arquivoDeSaida) {
}
