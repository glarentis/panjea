package it.eurotn.panjea.intra.manager.typehandler;

import it.eurotn.panjea.intra.domain.ModalitaErogazione;

import org.apache.commons.lang3.StringUtils;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class ModalitaErogazioneTypeHandler implements TypeHandler {

	@Override
	public String format(Object arg0) {
		ModalitaErogazione modalitaErogazione = (ModalitaErogazione) arg0;
		return modalitaErogazione == null ? "" : StringUtils.left(modalitaErogazione.name(), 1);
	}

	@Override
	public Class<?> getType() {
		return ModalitaErogazione.class;
	}

	@Override
	public Object parse(String arg0) throws TypeConversionException {
		return "";
	}
}
