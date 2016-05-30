package it.eurotn.panjea.exporter.typehandlers;

import java.math.BigDecimal;

public class OperazioneDecimaliDividiCommand<E> extends OperazioneDecimaliCommand<E> {

	@Override
	public BigDecimal execute(E value, int numDecimali) {
		BigDecimal valore = new BigDecimal(value.toString());
		double ordineDiGrandezza = Math.pow(10.0, numDecimali);
		return valore.divide(BigDecimal.valueOf(ordineDiGrandezza));
	}

}
