package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;

import com.jidesoft.converter.ConverterContext;

public class FaseLavorazioneRow extends ComponenteRow {

	private FaseLavorazioneArticolo fase;

	/**
	 * 
	 * @param faseParam
	 *            fase wrappata
	 * @param bd
	 *            business delegate.
	 */
	public FaseLavorazioneRow(final FaseLavorazioneArticolo faseParam, final IMagazzinoAnagraficaBD bd) {
		super(bd);
		this.fase = faseParam;
		setExpandable(false);
	}

	@Override
	public Class<?> getCellClassAt(int column) {
		switch (column) {
		case 1:
			return String.class;
		case 2:
			return Double.class;
		case 3:
			return Integer.class;
		default:
			return FaseLavorazione.class;
		}
	}

	@Override
	public ConverterContext getConverterContextAt(int col) {
		if (col == 2) {
			if (fase.getComponente() != null && fase.getComponente().getArticolo() != null) {
				qtaAttrezzaggioNumeroDecimaliContext.setUserObject(fase.getComponente().getArticolo()
						.getNumeroDecimaliQta());
			}
			if (fase.getArticolo() != null) {
				qtaAttrezzaggioNumeroDecimaliContext.setUserObject(fase.getArticolo().getNumeroDecimaliQta());
			}

			return qtaAttrezzaggioNumeroDecimaliContext;
		}
		return super.getConverterContextAt(col);
	}

	/**
	 * @return Returns the fase.
	 */
	public FaseLavorazioneArticolo getFase() {
		return fase;
	}

	@Override
	public Object getValueAt(int column) {
		switch (column) {
		case 1:
			return fase.getDescrizione();
		case 2:
			return fase.getQtaAttrezzaggio();
		case 3:
			return fase.getOrdinamento();
		default:
			return fase.getFaseLavorazione();
		}
	}

	@Override
	public void setValueAt(Object paramObject, int col) {
		switch (col) {
		case 1:
			fase.setDescrizione((String) paramObject);
			fase = bd.salvaFaseLavorazioneArticolo(fase);
			break;
		case 2:
			fase.setQtaAttrezzaggio((Double) paramObject);
			fase = bd.salvaFaseLavorazioneArticolo(fase);
			break;
		case 3:
			fase.setOrdinamento((Integer) paramObject);
			fase = bd.salvaFaseLavorazioneArticolo(fase);
			break;
		default:
			break;
		}
	}

}
