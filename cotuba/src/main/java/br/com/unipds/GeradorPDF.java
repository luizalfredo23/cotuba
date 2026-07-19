package br.com.unipds;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.properties.AreaBreakType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@FormatoEbookQualifier(FormatoEBook.PDF)
public class GeradorPDF implements GeradorEbook {
	@Override
	public void gerar(Ebook ebook) {
		List<Capitulo> capitulos = ebook.getConteudo();
		Path arquivoSaida = ebook.getArquivoDeSaida();
		
		try (var writer = new PdfWriter(Files.newOutputStream(arquivoSaida));
				var pdf = new PdfDocument(writer);
				var pdfDocument = new Document(pdf)) {

			pdf.getDocumentInfo().setTitle(ebook.getTitulo());
			pdf.getDocumentInfo().setAuthor(ebook.getAutor());

			capitulos.forEach(capitulo -> {

				List<IElement> convertToElements = HtmlConverter.convertToElements(capitulo.getHtml());

				if (pdf.getNumberOfPages() == 0) {
					pdf.addNewPage();
				}
				PdfOutline rootOutline = pdf.getOutlines(false);
				if (rootOutline == null) {
					pdf.initializeOutlines();
					rootOutline = pdf.getOutlines(false);
				}

				PdfOutline chapterOutline = rootOutline.addOutline(capitulo.getTitulo());
				chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

				for (IElement element : convertToElements) {
					pdfDocument.add((IBlockElement) element);
				}
				// TODO: não adicionar página depois do último capítulo
				pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

			});

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalStateException("Erro ao gerar PDF: " + arquivoSaida.toAbsolutePath(), ex);
		}
	}

}
