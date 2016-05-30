package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.DefaultExpandableRow;

public class ComponenteRow extends DefaultExpandableRow {

	private Componente componente;
	protected static ConverterContext qtaAttrezzaggioNumeroDecimaliContext = new NumberWithDecimalConverterContext(6);
	protected IMagazzinoAnagraficaBD bd;

	/**
	 * @param componente
	 *            componente modello della riga
	 * @param bd
	 *            business delegate.
	 */
	public ComponenteRow(final Componente componente, final IMagazzinoAnagraficaBD bd) {
		this.componente = componente;
		this.bd = bd;
		setExpandable(true);
	}

	/**
	 * Costruttore.
	 * 
	 * @param bd
	 *            bd
	 */
	public ComponenteRow(final IMagazzinoAnagraficaBD bd) {
		this.bd = bd;
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
			return ArticoloLite.class;
		}
	}

	/**
	 * @return componente della riga
	 */
	public Componente getComponente() {
		return componente;
	}

	@Override
	public ConverterContext getConverterContextAt(int column) {
		if (column == 2) {
			Integer numeroDecimaliQta = componente.getArticolo().getNumeroDecimaliQta();
			qtaAttrezzaggioNumeroDecimaliContext.setUserObject(numeroDecimaliQta);
			return qtaAttrezzaggioNumeroDecimaliContext;
		}
		return super.getConverterContextAt(column);
	}

	@Override
	public Object getValueAt(int column) {
		switch (column) {
		case 1:
			return componente.getFormula();
		case 2:
			return componente.getQtaAttrezzaggio();
		case 3:
			return componente.getOrdinamento();
		default:
			return componente.getArticolo();
		}
	}

	/**
	 * @param componente
	 *            The componente to set.
	 */
	public void setComponente(Componente componente) {
		this.componente = componente;
	}

	@Override
	public void setValueAt(Object paramObject, int col) {
		switch (col) {
		case 1:
			componente.setFormula((String) paramObject);
			componente = bd.salvaComponente(componente);
			break;
		case 2:
			componente.setQtaAttrezzaggio((Double) paramObject);
			componente = bd.salvaComponente(componente);
			break;
		case 3:
			componente.setOrdinamento((Integer) paramObject);
			componente = bd.salvaComponente(componente);
			break;
		default:
			break;
		}
		super.setValueAt(paramObject, col);
	}
}