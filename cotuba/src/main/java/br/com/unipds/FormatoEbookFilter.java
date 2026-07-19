package br.com.unipds;

import jakarta.enterprise.util.AnnotationLiteral;

public class FormatoEbookFilter extends AnnotationLiteral<FormatoEbookQualifier> implements FormatoEbookQualifier {
	private final FormatoEbook formato;
	
	public FormatoEbookFilter(FormatoEbook formato) {
		this.formato = formato;
	}
	
	@Override
	public FormatoEbook value() {
		return formato;
	}
	
	public static FormatoEbookFilter of(FormatoEbook formato) {
		return new FormatoEbookFilter(formato);
	}

}
