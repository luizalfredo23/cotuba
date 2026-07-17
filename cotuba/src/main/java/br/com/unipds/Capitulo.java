package br.com.unipds;

import java.nio.file.Path;

public class Capitulo {
	private String titulo;
	private String markdown;
	private String html;
	private Path arquivoMarkdown;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMarkdown() {
		return markdown;
	}
	public void setMarkdown(String markdown) {
		this.markdown = markdown;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public Path getArquivoMarkdown() {
		return arquivoMarkdown;
	}
	public void setArquivoMarkdown(Path arquivoMarkdown) {
		this.arquivoMarkdown = arquivoMarkdown;
	}
}