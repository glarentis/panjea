package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.converter.PanjeaConverter;

import java.util.List;

import com.jidesoft.converter.ConverterContext;

public class AspettoEsterioreConverter extends PanjeaConverter {
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	@Override
	public Object fromString(String descrizioneAspettoEsteriore, ConverterContext arg1) {
		List<AspettoEsteriore> aspettiEsteriore = magazzinoAnagraficaBD
				.caricaAspettiEsteriori(descrizioneAspettoEsteriore);
		AspettoEsteriore aspettoEsteriore = new AspettoEsteriore();
		if (aspettiEsteriore != null && !aspettiEsteriore.isEmpty()) {
			aspettoEsteriore = aspettiEsteriore.get(0);
		}
		return aspettoEsteriore;
	}

	@Override
	public Class<?> getClasse() {
		return AspettoEsteriore.class;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
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
		if (arg0 != null && arg0 instanceof AspettoEsteriore) {

			return ((AspettoEsteriore) arg0).getDescrizione();
		} else {
			return "";
		}
	}

}
