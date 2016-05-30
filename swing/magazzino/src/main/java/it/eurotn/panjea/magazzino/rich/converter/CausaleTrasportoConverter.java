package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.converter.PanjeaConverter;

import java.util.List;

import com.jidesoft.converter.ConverterContext;

public class CausaleTrasportoConverter extends PanjeaConverter {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	@Override
	public Object fromString(String descrizioneCausale, ConverterContext arg1) {
		List<CausaleTrasporto> causaliTrasporto = magazzinoAnagraficaBD.caricaCausaliTraporto(descrizioneCausale);
		CausaleTrasporto causaleTrasporto = new CausaleTrasporto();
		if (causaliTrasporto != null && !causaliTrasporto.isEmpty()) {
			causaleTrasporto = causaliTrasporto.get(0);
		}
		return causaleTrasporto;
	}

	@Override
	public Class<?> getClasse() {
		return CausaleTrasporto.class;
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
		if (arg0 != null && arg0 instanceof CausaleTrasporto) {
			return ((CausaleTrasporto) arg0).getDescrizione();
		} else {
			return "";
		}
	}

}
