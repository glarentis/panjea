package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.converter.PanjeaConverter;

import java.util.List;

import com.jidesoft.converter.ConverterContext;

public class TrasportoCuraConverter extends PanjeaConverter {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	@Override
	public Object fromString(String descrizioneTrasportoCura, ConverterContext arg1) {
		List<TrasportoCura> trasportiCura = magazzinoAnagraficaBD.caricaTrasportiCura(descrizioneTrasportoCura);
		TrasportoCura trasportoCura = new TrasportoCura();
		if (trasportiCura != null && !trasportiCura.isEmpty()) {
			trasportoCura = trasportiCura.get(0);
		}
		return trasportoCura;
	}

	@Override
	public Class<?> getClasse() {
		return TrasportoCura.class;
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
		if (arg0 != null && arg0 instanceof TrasportoCura) {
			return ((TrasportoCura) arg0).getDescrizione();
		} else {
			return "";
		}
	}

}
