package it.eurotn.panjea.magazzino.manager.export.typehandler;

import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseDdt;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class TipoDocumentoDMOTypeHandler implements TypeHandler {

	@Override
	public String format(Object obj) {

		String result = " ";

		TipoDocumento tipoDocumento = (TipoDocumento) obj;

		if (tipoDocumento != null) {

			String classeTipoDoc = tipoDocumento.getClasseTipoDocumento();

			if (ClasseDdt.class.getName().equals(classeTipoDoc)) {
				result = "D";
			} else if (ClasseFattura.class.getName().equals(classeTipoDoc)) {
				result = "F";
				if (tipoDocumento.isNotaCreditoEnable()) {
					result = "N";
				}
			}
		}

		return result;
	}

	@Override
	public Class<?> getType() {
		return TipoDocumento.class;
	}

	@Override
	public Object parse(String s) throws TypeConversionException {
		return null;
	}

}
