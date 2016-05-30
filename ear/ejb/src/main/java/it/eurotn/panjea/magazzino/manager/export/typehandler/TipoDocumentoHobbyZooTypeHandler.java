package it.eurotn.panjea.magazzino.manager.export.typehandler;

import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseDdt;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class TipoDocumentoHobbyZooTypeHandler implements TypeHandler {

	@Override
	public String format(Object obj) {

		String result = " ";

		TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) obj;

		if (tipoAreaMagazzino != null) {

			String classeTipoDoc = tipoAreaMagazzino.getTipoDocumento().getClasseTipoDocumento();

			if (ClasseDdt.class.getName().equals(classeTipoDoc)) {
				result = "BOLLA";
			} else if (ClasseFattura.class.getName().equals(classeTipoDoc)) {

				result = "FATTURA";
				if (tipoAreaMagazzino.getTipoMovimento() == TipoMovimento.SCARICO
						&& tipoAreaMagazzino.isValoriFatturato()) {
					result = "FATT.ACC";
				}

				if (tipoAreaMagazzino.getTipoDocumento().isNotaCreditoEnable()) {
					result = "NOTA CREADITO";
				}
			}
		}

		return result;
	}

	@Override
	public Class<?> getType() {
		return TipoAreaMagazzino.class;
	}

	@Override
	public Object parse(String s) throws TypeConversionException {
		return null;
	}

}
