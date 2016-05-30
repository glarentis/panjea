package it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo;

import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.panjea.magazzino.domain.RigaInventarioArticolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.math.BigDecimal;
import java.util.Calendar;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class InventarioArticoloTableModel extends DefaultBeanTableModel<InventarioArticolo> {

	public static final String MODEL_ID = "inventarioArticoloTableModel";

	private static final long serialVersionUID = 6603191041254544923L;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private EditorContext giacenzaRealeEditorContext = GiacenzaRealeCellRenderer.GIACENZA_REALE_CONTEXT;

	private static ConverterContext numberContext;
	{
		numberContext = new NumberWithDecimalConverterContext();
		numberContext.setUserObject(new Integer(6));
	}

	/**
	 * Costruttore.
	 * 
	 */
	public InventarioArticoloTableModel() {
		super(MODEL_ID,
				new String[] { "articolo.categoria", "articolo", "giacenzaCalcolata", "qtaRighe", "rettifica" },
				InventarioArticolo.class);
		magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 2:
		case 3:
		case 4:
			return numberContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 3:
			InventarioArticolo inventarioArticolo = getElementAt(row);
			giacenzaRealeEditorContext.setUserObject(inventarioArticolo.getArticolo().getNumeroDecimaliQta());
			return giacenzaRealeEditorContext;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {

		switch (column) {
		case 3:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void setValueAt(Object editedValue, int row, int column) {
		if (editedValue != null) {
			InventarioArticolo inventarioArticolo = getElementAt(row);

			// ricarico l'inventario per essere sicuro di avere tutte le sue righe visto che per ottimizzare, il
			// caricamento della tabella non le carica.
			inventarioArticolo = magazzinoDocumentoBD.caricaInventarioArticolo(inventarioArticolo);

			RigaInventarioArticolo rigaInventarioArticolo = new RigaInventarioArticolo();
			rigaInventarioArticolo.setData(Calendar.getInstance().getTime());
			rigaInventarioArticolo.setInventarioArticolo(inventarioArticolo);
			rigaInventarioArticolo.setQuantita((BigDecimal) editedValue);

			inventarioArticolo.getRigheInventarioArticolo().add(rigaInventarioArticolo);
			inventarioArticolo = magazzinoDocumentoBD.salvaInventarioArticolo(inventarioArticolo);

			Integer numeroRighe = inventarioArticolo.getRigheInventarioArticolo() != null ? inventarioArticolo
					.getRigheInventarioArticolo().size() : 0;
			BigDecimal qtaRighe = BigDecimal.ZERO;
			if (inventarioArticolo.getRigheInventarioArticolo() != null) {
				for (RigaInventarioArticolo riga : inventarioArticolo.getRigheInventarioArticolo()) {
					qtaRighe = qtaRighe.add(riga.getQuantita());
				}
			}
			inventarioArticolo.setNumeroRighe(new Long(numeroRighe));
			inventarioArticolo.setQtaRighe(qtaRighe);

			setObject(inventarioArticolo, row);
			super.setValueAt(inventarioArticolo.getQtaRighe(), row, column);
		} else {
			super.setValueAt(editedValue, row, column);
		}

	}
}
