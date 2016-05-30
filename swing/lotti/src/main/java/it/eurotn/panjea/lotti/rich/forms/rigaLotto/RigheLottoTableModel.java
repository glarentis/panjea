package it.eurotn.panjea.lotti.rich.forms.rigaLotto;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.rich.search.LottoSearchObject;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import java.util.ArrayList;
import java.util.List;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class RigheLottoTableModel extends DefaultBeanEditableTableModel<RigaLotto> {

	private static final long serialVersionUID = 2706037394279730422L;

	private RigaArticolo rigaArticolo;

	private static ConverterContext qtaContext;
	private static EditorContext qtaEditorContext;

	private int numeroDecimaliQta;

	static {
		qtaContext = new NumberWithDecimalConverterContext();
		qtaContext.setUserObject(2);

		qtaEditorContext = new EditorContext("qtaEditorContext", 2);
	}

	/**
	 * Costruttore.
	 *
	 * @param rigaArticolo
	 *            riga articolo
	 *
	 */
	public RigheLottoTableModel(final RigaArticolo rigaArticolo) {
		super("righeLotto", new String[] { "lotto", "quantita", "lotto.priorita", "lotto.dataScadenza" },
				RigaLotto.class);
		this.rigaArticolo = rigaArticolo;
	}

	@Override
	protected RigaLotto createNewObject() {
		RigaLotto rigaLotto = new RigaLotto();
		rigaLotto.setQuantita(0.0);
		rigaLotto.setRigaArticolo(rigaArticolo);
		return rigaLotto;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 1:
			qtaContext.setUserObject(numeroDecimaliQta);
			return qtaContext;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		switch (col) {
		case 0:
			SearchContext searchContext = new SearchContext("codice");
			searchContext.addPropertyFilter(LottoSearchObject.ARTICOLO_KEY, "rigaArticolo.articolo");
			searchContext.addPropertyFilter(LottoSearchObject.DEPOSITO_KEY,
					"rigaArticolo.areaMagazzino.depositoOrigine");
			searchContext.addPropertyFilter(LottoSearchObject.TIPO_MOVIMENTO_KEY,
					"rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento");
			searchContext.addPropertyFilter(LottoSearchObject.STORNO_KEY,
					"rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoDocumento.notaCreditoEnable");

			List<Lotto> lotti = new ArrayList<Lotto>();
			for (RigaLotto rigaLotto : getObjects()) {
				lotti.add(rigaLotto.getLotto());
			}
			searchContext.addPropertyFilterValue(LottoSearchObject.FILTER_LIST_KEY, "lottiAssegnati", lotti);
			return searchContext;
		case 1:
			qtaEditorContext.setUserObject(numeroDecimaliQta);
			return qtaEditorContext;
		default:
			break;
		}
		return super.getEditorContextAt(row, col);
	}

	/**
	 * @param lotto
	 *            lotto da escludere dal calcolo del totale qta già assegnata
	 * @return totale delle righe lotto nel table model
	 */
	public Double getTotaleRigheLotti(Lotto lotto) {
		Double result = 0.0;
		for (RigaLotto rigaLotto : getObjects()) {
			if (rigaLotto.getQuantita() != null && !lotto.equals(rigaLotto.getLotto())) {
				result = result + rigaLotto.getQuantita();
			}
		}
		return result;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column < 2;
	}

	@Override
	protected boolean isRowObjectChanged(Object value, int row, int column) {
		Object oldValue = getValueAt(row, column);
		if (value instanceof Lotto) {
			Lotto oldLotto = (Lotto) oldValue;
			Lotto newLotto = (Lotto) value;

			return newLotto != null && oldLotto != null && newLotto.getCodice().equals(oldLotto.getCodice())
					&& newLotto.getDataScadenza().equals(oldLotto.getDataScadenza())
					&& newLotto.getPriorita().equals(oldLotto.getPriorita());
		}
		return super.isRowObjectChanged(value, row, column);
	}

	/**
	 *
	 * @param paramRigaArticolo
	 *            riga articolo
	 */
	private void setNumeroDecimaliQta(RigaArticolo paramRigaArticolo) {
		this.numeroDecimaliQta = 2;
		if (paramRigaArticolo != null && paramRigaArticolo.getNumeroDecimaliQta() != null) {
			this.numeroDecimaliQta = paramRigaArticolo.getNumeroDecimaliQta();
		}
	}

	/**
	 * @param rigaArticolo
	 *            The rigaArticolo to set.
	 */
	public void setRigaArticolo(RigaArticolo rigaArticolo) {
		this.rigaArticolo = rigaArticolo;
		setNumeroDecimaliQta(rigaArticolo);
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		if (column == 0 && value != null) {
			Lotto lotto = (Lotto) value;
			if (rigaArticolo != null && lotto != null) {
				Double qtaRiga = rigaArticolo.getQta() != null ? rigaArticolo.getQta() : 0.0;
				Double qtaLotto = qtaRiga - getTotaleRigheLotti(lotto);
				// se sono su un movimento di scarico aggiungo qta in base alla
				// rimanenza del lotto
				if (rigaArticolo.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento() == TipoMovimento.SCARICO) {
					qtaLotto = qtaLotto > lotto.getRimanenza() ? lotto.getRimanenza() : qtaLotto;
				}
				// getElementAt(row).setQuantita(qtaLotto);
				super.setValueAt(qtaLotto, row, 1);
				fireTableRowsUpdated(row, row);
			}
		}

		super.setValueAt(value, row, column);

	}

}
