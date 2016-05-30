package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class CarrelloPagamentiTableModel extends DefaultBeanTableModel<PagamentoPM> {

	private static final long serialVersionUID = -5859243944165539006L;

	private static ConverterContext numberPrezzoContext;
	private static EditorContext numberPrezzoEditorContext;
	private ValutaAziendaCache valutaCache;
	private List<PagamentoPM> parzialePagamenti;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));
		numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);
	}

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id per il tableModel
	 */
	public CarrelloPagamentiTableModel(final String modelId) {
		super(modelId, new String[] { "chiusuraForzataRata", "importo.importoInValuta", "entita", "dataScadenza",
				"importoRata", "residuo", "numeroRata", "totalePagato", "dataDocumento", "tipoDocumento",
				"numeroDocumento" }, PagamentoPM.class);
		valutaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
	}

	@Override
	public void addObject(PagamentoPM pagamentoPM) {
		getParzialePagamenti().add(pagamentoPM);
		super.addObject(pagamentoPM);
	}

	/**
	 * @return codice della valuta contenuta nel carrello (il carrello può avere solamente 1 valuta.)Null se non ho
	 *         rate.
	 */
	public String getCodiceValuta() {
		String codiceValuta = null;
		if (getRowCount() > 0) {
			codiceValuta = getElementAt(0).getImporto().getCodiceValuta();
		}
		return codiceValuta;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 1:
		case 4:
		case 5:
		case 7:
			numberPrezzoContext.setUserObject(valutaCache.caricaValutaAzienda(getCodiceValuta()).getNumeroDecimali());
			return numberPrezzoContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 1:
			numberPrezzoEditorContext.setUserObject(valutaCache.caricaValutaAzienda(getCodiceValuta())
					.getNumeroDecimali());
			return numberPrezzoEditorContext;
		default:
			return null;
		}
	}

	/**
	 * @return List<PagamentoPM>
	 */
	private List<PagamentoPM> getParzialePagamenti() {
		if (parzialePagamenti == null) {
			parzialePagamenti = new ArrayList<PagamentoPM>();
		}
		return parzialePagamenti;
	}

	/**
	 * @return totale in valuta dei pagamenti contenuti nel carrello.
	 */
	public BigDecimal getTotalePagamenti() {
		BigDecimal totale = BigDecimal.ZERO;
		for (PagamentoPM pagamentoPM : getObjects()) {
			if (pagamentoPM.getImporto() != null && pagamentoPM.getImporto().getImportoInValuta() != null) {
				totale = totale.add(pagamentoPM.getImporto().getImportoInValuta());
			}
		}
		return totale;
	}

	/**
	 * @return totale in valuta dei pagamenti contenuti nel carrello.
	 */
	public BigDecimal getTotaleParzialePagamenti() {
		BigDecimal totale = BigDecimal.ZERO;
		for (PagamentoPM pagamentoPM : getParzialePagamenti()) {
			if (pagamentoPM.getImporto() != null && pagamentoPM.getImporto().getImportoInValuta() != null) {
				totale = totale.add(pagamentoPM.getImporto().getImportoInValuta());
			}
		}
		return totale;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		PagamentoPM pagamentoPM = getElementAt(row);
		switch (column) {
		case 0:
			return pagamentoPM.getRata().getStatoRata() != StatoRata.CHIUSA
					&& pagamentoPM.getRata().getStatoRata() != StatoRata.IN_LAVORAZIONE;
		case 1:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void removeObject(PagamentoPM pagamentoPM) {
		getParzialePagamenti().remove(pagamentoPM);
		super.removeObject(pagamentoPM);
	}

	/**
	 * Reset della lista che contiene i pagamenti per la somma degli importi.
	 */
	public void resetTotaleParziale() {
		getParzialePagamenti().clear();
	}

	@Override
	public void setRows(Collection<PagamentoPM> rows) {
		getParzialePagamenti().clear();
		getParzialePagamenti().addAll(rows);
		super.setRows(rows);
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		super.setValueAt(arg0, arg1, arg2);
		(getObject(arg1)).getImporto().calcolaImportoValutaAzienda(
				valutaCache.caricaValutaAziendaCorrente().getNumeroDecimali());
	}
}
