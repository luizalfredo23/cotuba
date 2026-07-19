package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public class Ebook {
	
	private String titulo;
	private String autor;
	private FormatoEbook formato;
	private List<Capitulo> conteudo;
	private Path arquivoDeSaida;
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public FormatoEbook getFormato() {
		return formato;
	}
	public void setFormato(FormatoEbook formato) {
		this.formato = formato;
	}
	public List<Capitulo> getConteudo() {
		return conteudo;
	}
	public void setConteudo(List<Capitulo> conteudo) {
		this.conteudo = conteudo;
	}
	public Path getArquivoDeSaida() {
		return arquivoDeSaida;
	}
	public void setArquivoDeSaida(Path arquivoDeSaida) {
		this.arquivoDeSaida = arquivoDeSaida;
	}
	
}
