package br.com.unipds;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CLIOptionsReader {

	ParametrosCotuba readOptions(String[] args) {

		var parametrosCotuba = new ParametrosCotuba();

		var options = new Options();

		var opcaoDeDiretorioDosMD = new Option("d", "dir", true,
				"Diretório que contém os arquivos md. Default: diretório atual.");
		options.addOption(opcaoDeDiretorioDosMD);

		var opcaoDeFormatoDoEbook = new Option("f", "format", true,
				"Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
		options.addOption(opcaoDeFormatoDoEbook);

		var opcaoDeArquivoDeSaida = new Option("o", "output", true,
				"Arquivo de saída do ebook. Default: book.{formato}.");
		options.addOption(opcaoDeArquivoDeSaida);

		var opcaoModoVerboso = new Option("v", "verbose", false, "Habilita modo verboso.");
		options.addOption(opcaoModoVerboso);

		CommandLineParser cmdParser = new DefaultParser();
		var ajuda = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = cmdParser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			ajuda.printHelp("cotuba", options);
			throw new IllegalStateException("Erro ao processar argumentos de linha de comando.");
		}

		try {
			String nomeDoDiretorioDosMD = cmd.getOptionValue("dir");

			if (nomeDoDiretorioDosMD != null) {
				parametrosCotuba.setDiretorioDosMD(Paths.get(nomeDoDiretorioDosMD));
				if (!Files.isDirectory(parametrosCotuba.getDiretorioDosMD())) {
					throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
				}
			} else {
				Path diretorioAtual = Paths.get("");
				parametrosCotuba.setDiretorioDosMD(diretorioAtual);
			}

			String nomeDoFormatoDoEbook = cmd.getOptionValue("format");

			if (nomeDoFormatoDoEbook != null) {
				try {
					parametrosCotuba.setFormato(FormatoEbook.valueOf(nomeDoFormatoDoEbook.toUpperCase()));
				} catch (IllegalArgumentException ex) {
					throw new IllegalArgumentException("Formato do ebook inválido: " + nomeDoFormatoDoEbook);
				}
			} else {
				parametrosCotuba.setFormato(FormatoEbook.PDF);
			}

			String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");
			if (nomeDoArquivoDeSaidaDoEbook != null) {

				parametrosCotuba.setArquivoDeSaida(Paths.get(nomeDoArquivoDeSaidaDoEbook));
			} else {
				parametrosCotuba
						.setArquivoDeSaida(Paths.get("book." + parametrosCotuba.getFormato().name().toLowerCase()));
			}
			if (Files.isDirectory(parametrosCotuba.getArquivoDeSaida())) {
				// deleta arquivos do diretório recursivamente
				Files.walk(parametrosCotuba.getArquivoDeSaida()).sorted(Comparator.reverseOrder()).map(Path::toFile)
						.forEach(File::delete);
			} else {
				Files.deleteIfExists(parametrosCotuba.getArquivoDeSaida());
			}

			parametrosCotuba.setModoVerboso(cmd.hasOption("verbose"));
		} catch (IllegalArgumentException ex) {
			// Erros de validação de negócio (formato inválido, diretório inexistente etc.)
			// já têm mensagem clara o suficiente — não devem ser envolvidos numa mensagem
			// genérica.
			throw ex;
		} catch (Exception ex) {
			// Erros técnicos inesperados (I/O, etc.) continuam sendo envolvidos.
			throw new IllegalStateException("Erro ao processar argumentos de linha de comando.", ex);
		}

		return parametrosCotuba;
	}
}