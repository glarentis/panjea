package it.eurotn.panjea.parametriricerca.domain;

public class NessunoStrategy extends DateStrategy {

	private static final long serialVersionUID = 2157466492414614194L;

	@Override
	public Periodo calcola(Periodo periodo) {
		Periodo result = new Periodo();
		result.setDataIniziale(null);
		result.setDataFinale(null);
		return super.calcola(result);
	}

	@Override
	public boolean isAllowedSelectDate() {
		return false;
	}
}
