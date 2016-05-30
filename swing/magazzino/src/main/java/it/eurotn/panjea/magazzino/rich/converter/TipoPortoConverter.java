package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.converter.PanjeaConverter;

import java.util.List;

import com.jidesoft.converter.ConverterContext;

public class TipoPortoConverter extends PanjeaConverter<TipoPorto> {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	@Override
	public Object fromString(String descrizioneTipoPorto, ConverterContext arg1) {
		List<TipoPorto> tipiPorto = magazzinoAnagraficaBD.caricaTipiPorto(descrizioneTipoPorto);
		if (tipiPorto.size() > 0) {
			return tipiPorto.get(0);
		}
		return new TipoPorto();
	}

	@Override
	public Class<TipoPorto> getClasse() {
		return TipoPorto.class;
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
		if (arg0 != null && arg0 instanceof TipoPorto) {
			return ((TipoPorto) arg0).getDescrizione();
		} else {
			return "";
		}
	}

}
