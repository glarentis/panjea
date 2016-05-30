package it.eurotn.panjea.magazzino.rich.editors.manutenzionelistino;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.control.table.renderer.QtaScaglioneCellRenderer;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.util.List;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableTableModel;

public class RigheManutenzioneListinoTableModel extends DefaultBeanTableModel<RigaManutenzioneListino> implements
		NavigableTableModel {

	private static final long serialVersionUID = -826433795393763031L;

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	private static ConverterContext numberPrezzoContext;
	private static EditorContext numberPrezzoEditorContext;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);

	}

	/**
	 * Default constructor.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            il BD per salvare le modifiche dopo edit
	 */
	public RigheManutenzioneListinoTableModel(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super(RisultatiRicercaManutenzioneListinoTablePage.PAGE_ID, new String[] { "articolo", "numero", "quantita",
				"valoreOriginale", "valore", "numeroDecimali", "statoRigaManutenzioneListino", "provenienzaDecimali" },
				RigaManutenzioneListino.class);
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 3:
		case 4:
			if (row != -1) {
				RigaManutenzioneListino riga = getElementAt(row);
				numberPrezzoContext.setUserObject(riga.getNumeroDecimali());
				return numberPrezzoContext;
			}
			return null;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 1:
			return NumeroManutenzioneCellRenderer.NUMERO_MANUTENZIONE_CELL_RENDERER_CONTEXT;
		case 2:
			return QtaScaglioneCellRenderer.QTA_SCAGLIONE_CONTEXT;
		case 4:
		case 5:
			RigaManutenzioneListino riga = getElementAt(row);
			numberPrezzoEditorContext.setUserObject(riga.getNumeroDecimali());
			return numberPrezzoEditorContext;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 4:
		case 5:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean isNavigableAt(int row, int column) {
		switch (column) {
		case 4:
		case 5:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean isNavigationOn() {
		return true;
	}

	@Override
	protected Object setColumnValue(Object row, Object value, int column) {
		Object rowReturn = super.setColumnValue(row, value, column);
		List<RigaManutenzioneListino> righeSalvate = magazzinoAnagraficaBD
				.salvaRigaManutenzioneListino((RigaManutenzioneListino) rowReturn);

		// devo reimpostare nel model la rigaManutenzione salvata altrimenti modifiche successive alla stessa riga
		// sollevano una staleObjectException perchè l'oggetto corrente non è aggiornato; questo è un caso particolare
		// dove devo salvare la riga manutenzione a modifica effettuata
		for (RigaManutenzioneListino rigaManutenzioneListino : righeSalvate) {
			int indexOfRigaSalvata = this.getObjects().indexOf(rigaManutenzioneListino);
			setObject(rigaManutenzioneListino, indexOfRigaSalvata);

			if (((RigaManutenzioneListino) rowReturn).equals(rigaManutenzioneListino)) {
				rowReturn = rigaManutenzioneListino;
			}
		}

		return rowReturn;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
