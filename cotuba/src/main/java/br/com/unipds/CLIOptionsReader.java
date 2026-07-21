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

		var parametrosCotubaBuilder = ParametrosCotubaBuilder.builder();

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
				parametrosCotubaBuilder.diretorioDosMD(Paths.get(nomeDoDiretorioDosMD));
				if (!Files.isDirectory(parametrosCotubaBuilder.diretorioDosMD())) {
					throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
				}
			} else {
				Path diretorioAtual = Paths.get("");
				parametrosCotubaBuilder.diretorioDosMD(diretorioAtual);
			}

			String nomeDoFormatoDoEbook = cmd.getOptionValue("format");

			if (nomeDoFormatoDoEbook != null) {
				try {
					parametrosCotubaBuilder.formato(FormatoEbook.valueOf(nomeDoFormatoDoEbook.toUpperCase()));
				} catch (IllegalArgumentException ex) {
					throw new IllegalArgumentException("Formato do ebook inválido: " + nomeDoFormatoDoEbook);
				}
			} else {
				parametrosCotubaBuilder.formato(FormatoEbook.PDF);
			}

			String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");
			if (nomeDoArquivoDeSaidaDoEbook != null) {

				parametrosCotubaBuilder.arquivoDeSaida(Paths.get(nomeDoArquivoDeSaidaDoEbook));
			} else {
				parametrosCotubaBuilder
						.arquivoDeSaida(Paths.get("book." + parametrosCotubaBuilder.formato().name().toLowerCase()));
			}
			if (Files.isDirectory(parametrosCotubaBuilder.arquivoDeSaida())) {
				// deleta arquivos do diretório recursivamente
				Files.walk(parametrosCotubaBuilder.arquivoDeSaida()).sorted(Comparator.reverseOrder()).map(Path::toFile)
						.forEach(File::delete);
			} else {
				Files.deleteIfExists(parametrosCotubaBuilder.arquivoDeSaida());
			}

			parametrosCotubaBuilder.modoVerboso(cmd.hasOption("verbose"));
		} catch (IllegalArgumentException ex) {
			// Erros de validação de negócio (formato inválido, diretório inexistente etc.)
			// já têm mensagem clara o suficiente — não devem ser envolvidos numa mensagem
			// genérica.
			throw ex;
		} catch (Exception ex) {
			// Erros técnicos inesperados (I/O, etc.) continuam sendo envolvidos.
			throw new IllegalStateException("Erro ao processar argumentos de linha de comando.", ex);
		}

		return parametrosCotubaBuilder.build();
	}
}