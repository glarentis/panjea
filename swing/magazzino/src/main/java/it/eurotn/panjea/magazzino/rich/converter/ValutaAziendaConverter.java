package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.rich.bd.IValutaBD;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class ValutaAziendaConverter extends PanjeaConverter {

	private IValutaBD valutaBD;

	@Override
	public Object fromString(String codiceValutaAzienda, ConverterContext arg1) {
		return valutaBD.caricaValutaAzienda(codiceValutaAzienda);
	}

	@Override
	public Class<?> getClasse() {
		return ValutaAzienda.class;
	}

	/**
	 * @param valutaBD
	 *            The valutaBD to set.
	 */
	public void setValutaBD(IValutaBD valutaBD) {
		this.valutaBD = valutaBD;
	}

	@Override
	public boolean supportFromString(String arg0, ConverterContext arg1) {
		return true;
	}

	@Override
	public boolean supportToString(Object arg0, ConverterContext arg1) {
		return true;
	}

	@Override
	public String toString(Object arg0, ConverterContext arg1) {
		if (arg0 != null && arg0 instanceof ValutaAzienda) {
			return ((ValutaAzienda) arg0).getCodiceValuta();
		} else {
			return "";
		}
	}

}
