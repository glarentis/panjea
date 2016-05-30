package it.eurotn.panjea.intra.manager.typehandler;

import it.eurotn.panjea.intra.domain.ModalitaTrasporto;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class ModalitaTrasportoTypeHandler implements TypeHandler {

	@Override
	public String format(Object arg0) {
		ModalitaTrasporto modalitaTrasporto = (ModalitaTrasporto) arg0;
		int num = modalitaTrasporto.ordinal() + 1;
		return new Integer(num).toString();
	}

	@Override
	public Class<?> getType() {
		return ModalitaTrasporto.class;
	}

	@Override
	public Object parse(String arg0) throws TypeConversionException {
		return ModalitaTrasporto.ACQUA;
	}

}
