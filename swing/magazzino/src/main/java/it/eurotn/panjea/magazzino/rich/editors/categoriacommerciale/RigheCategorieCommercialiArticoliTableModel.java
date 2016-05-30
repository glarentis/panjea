package it.eurotn.panjea.magazzino.rich.editors.categoriacommerciale;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class RigheCategorieCommercialiArticoliTableModel extends DefaultBeanEditableTableModel<ArticoloLite> {

	private static final long serialVersionUID = 2706037394279730422L;

	private static ConverterContext qtaContext;

	static {
		qtaContext = new NumberWithDecimalConverterContext();
		qtaContext.setUserObject(2);
	}

	/**
	 * Costruttore.
	 * 
	 * @param categoriaCommercialeArticolo
	 *            categoria commerciale dell'articolo
	 * 
	 */
	public RigheCategorieCommercialiArticoliTableModel(final CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
		super("categoriaCommercialeArticolo", new String[] { "codice", "categoria.codice" }, ArticoloLite.class);
	}

	@Override
	protected ArticoloLite createNewObject() {
		return null;
	}

	//
	// @Override
	// public ConverterContext getConverterContextAt(int row, int column) {
	// return super.getConverterContextAt(i, j);
	// }

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		// switch (col) {
		// case 0:
		// SearchContext searchContext = new SearchContext("codice", null, null);
		// searchContext.addPropertyFilter(LottoSearchObject.ARTICOLO_KEY, "rigaArticolo.articolo");
		// searchContext.addPropertyFilter(LottoSearchObject.DEPOSITO_KEY,
		// "rigaArticolo.areaMagazzino.depositoOrigine");
		// searchContext.addPropertyFilter(LottoSearchObject.TIPO_MOVIMENTO_KEY,
		// "rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento");
		//
		// List<Lotto> lotti = new ArrayList<Lotto>();
		// for (RigaLotto rigaLotto : getObjects()) {
		// lotti.add(rigaLotto.getLotto());
		// }
		// searchContext.addPropertyFilterValue(LottoSearchObject.FILTER_LIST_KEY, "lottiAssegnati", lotti);
		// return searchContext;
		// case 1:
		// qtaEditorContext.setUserObject(numeroDecimaliQta);
		// return qtaEditorContext;
		// default:
		// break;
		// }
		return super.getEditorContextAt(row, col);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column < 2;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		// if (column == 0 && value != null) {
		// if (rigaArticolo != null
		// && (getElementAt(row).getLotto() == null || getElementAt(row).getLotto().getId() == null)) {
		// Double qtaRiga = rigaArticolo.getQta() != null ? rigaArticolo.getQta() : 0.0;
		// Double qtaLotto = qtaRiga - getTotaleRigheLotti();
		//
		// // se sono su un movimento di scarico aggiungo qta in base alla rimanenza del lotto
		// if (rigaArticolo.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento() == TipoMovimento.SCARICO) {
		// qtaLotto = qtaLotto > ((Lotto) value).getRimanenza() ? ((Lotto) value).getRimanenza() : qtaLotto;
		// }
		// getElementAt(row).setQuantita(qtaLotto);
		// }
		// }

		super.setValueAt(value, row, column);

	}

}
