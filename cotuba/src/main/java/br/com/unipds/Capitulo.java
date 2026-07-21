package br.com.unipds;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Capitulo(String titulo, Markdown markdown, String html) {

}