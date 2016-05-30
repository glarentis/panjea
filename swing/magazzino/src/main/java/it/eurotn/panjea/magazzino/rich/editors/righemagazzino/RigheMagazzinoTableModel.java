package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaNotaDTO;
import it.eurotn.panjea.magazzino.util.RigaTestataDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellSpan;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.SpanTableModel;

public class RigheMagazzinoTableModel extends DefaultBeanTableModel<RigaMagazzinoDTO> implements SpanTableModel {

	private static final long serialVersionUID = -8196452951427785455L;

	private static CellSpan cellSpenNote = new CellSpan(0, 0, 1, 9);
	private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(6);
	// fisso i decimali a 4 ma per non avere qta troncate dovrei prendere il numero dei decimali più alto su tutte le
	// righe
	private static ConverterContext numberQtaContext = new NumberWithDecimalConverterContext(4);

	private Integer numeroDecimaliQta = 0;
	private Integer numeroDecimaliPrezzo = 0;

	/**
	 * Costruttore.
	 */
	public RigheMagazzinoTableModel() {
		super(RigheMagazzinoTablePage.PAGE_ID, new String[] { "articolo.codice", "codiceEntita", "descrizione",
				"prezzoUnitarioReale", "qta", "qtaChiusa", "prezzoNetto", "variazione", "prezzoTotale" },
				RigaMagazzinoDTO.class, RigaArticoloDTO.class);
	}

	/**
	 * Aggiorna il numero dei decimali quantità e prezzo in base alle righe contenute nel modello.
	 */
	public void aggiornaNumeroDecimali() {
		numeroDecimaliPrezzo = 0;
		numeroDecimaliQta = 0;

		for (RigaMagazzinoDTO rigaMagazzinoDTO : getObjects()) {
			if (rigaMagazzinoDTO instanceof RigaArticoloDTO) {
				numeroDecimaliQta = Math.max(numeroDecimaliQta, rigaMagazzinoDTO.getNumeroDecimaliQta());
				numeroDecimaliPrezzo = Math.max(numeroDecimaliPrezzo, rigaMagazzinoDTO.getNumeroDecimaliPrezzo());
			}
		}
	}

	@Override
	public CellSpan getCellSpanAt(int row, int column) {
		RigaMagazzinoDTO rigaMagazzinoDTO = getObject(row);

		if (rigaMagazzinoDTO instanceof RigaArticoloDTO) {
			return null;
		} else {
			if (column == 0) {
				cellSpenNote.setRow(row);
				return cellSpenNote;
			} else {
				return null;
			}
		}
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {

		switch (column) {
		case 3:
			numberPrezzoContext.setUserObject(numeroDecimaliPrezzo);
			return numberPrezzoContext;
		case 6:
			numberPrezzoContext.setUserObject(4);
			return numberPrezzoContext;
		case 8:
			numberPrezzoContext.setUserObject(2);
			return numberPrezzoContext;
		case 4:
		case 5:
			numberQtaContext.setUserObject(numeroDecimaliQta);
			return numberQtaContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return LivelloRigaMagazzinoCellRenderer.LIVELLO_RIGA_MAGAZZINO_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		RigaMagazzinoDTO rigaMagazzinoDTO = getObject(row);

		if (rigaMagazzinoDTO instanceof RigaArticoloDTO) {
			return super.getValueAt(row, column);
		} else {
			if (column == 0) {
				if (rigaMagazzinoDTO instanceof RigaNotaDTO) {
					return ((RigaNotaDTO) rigaMagazzinoDTO).getNota();
				} else if (rigaMagazzinoDTO instanceof RigaTestataDTO) {
					return ((RigaTestataDTO) rigaMagazzinoDTO).getDescrizione();
				} else {
					return null;
				}

			} else {
				return null;
			}
		}
	}

	@Override
	public boolean isCellSpanOn() {
		return true;
	}

	/**
	 * @param numeroDecimaliPrezzo
	 *            The numeroDecimaliPrezzo to set.
	 */
	public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
		this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
	}

	/**
	 * @param numeroDecimaliQta
	 *            The numeroDecimaliQta to set.
	 */
	public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
		this.numeroDecimaliQta = numeroDecimaliQta;
	}
}
