package br.com.unipds;

import jakarta.enterprise.util.AnnotationLiteral;

public class FormatoEbookFilter extends AnnotationLiteral<FormatoEbookQualifier> implements FormatoEbookQualifier {
	private final FormatoEBook formato;
	
	public FormatoEbookFilter(FormatoEBook formato) {
		this.formato = formato;
	}
	
	@Override
	public FormatoEBook value() {
		return formato;
	}
	
	public static FormatoEbookFilter of(FormatoEBook formato) {
		return new FormatoEbookFilter(formato);
	}

}
