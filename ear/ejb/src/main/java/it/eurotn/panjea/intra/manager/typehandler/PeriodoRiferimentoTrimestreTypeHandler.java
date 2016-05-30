package it.eurotn.panjea.intra.manager.typehandler;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra.PeriodoRiferimentoTrimestre;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class PeriodoRiferimentoTrimestreTypeHandler implements TypeHandler {

	@Override
	public String format(Object arg0) {
		PeriodoRiferimentoTrimestre riferimento = (PeriodoRiferimentoTrimestre) arg0;
		switch (riferimento) {
		case MESE1:
			return "8";
		case MESE12:
			return "9";
		case MESE123:
			return "0";
		default:
			break;
		}
		throw new IllegalArgumentException("valore non previsto per il periodo di riferimento " + riferimento);
	}

	@Override
	public Class<?> getType() {
		return PeriodoRiferimentoTrimestre.class;
	}

	@Override
	public Object parse(String arg0) throws TypeConversionException {
		return "";
	}
}
