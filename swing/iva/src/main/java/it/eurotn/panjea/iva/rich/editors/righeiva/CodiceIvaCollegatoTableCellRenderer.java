package it.eurotn.panjea.iva.rich.editors.righeiva;

import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

/**
 * Renderer per la colonna codice iva che se presente un documento INTRA o ART17 mostra una icona per segnalare la
 * presenza di un codice iva collegato; il tooltip sulla cella invece mostra codice e descrizione del codice iva
 * collegato.
 *
 * @author Leonardo
 */
public class CodiceIvaCollegatoTableCellRenderer extends ContextSensitiveCellRenderer {

	public static final EditorContext CODICE_IVA_COLLEGATO_CELL_RENDERER_CONTEXT = new EditorContext(
			"CODICE_IVA_COLLEGATO_CELL_RENDERER_CONTEXT");

	private static final long serialVersionUID = -3141273136692479793L;
	private final Icon icon;

	/**
	 * Crea TableCellRenderer per il codice iva collegato.
	 */
	public CodiceIvaCollegatoTableCellRenderer() {
		super();
		IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);
		icon = iconSource.getIcon("codiceivacollegato");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object arg1, boolean arg2, boolean arg3, int row,
			int col) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, arg1, arg2, arg3, row, col);

		if (!(TableModelWrapperUtils.getActualTableModel(table.getModel()) instanceof RigheIvaTableModel)) {
			return label;
		}
		// setto a null l'icona dato che l'editor delle righe puo' rimanere sempre aperto
		label.setIcon(null);
		label.setToolTipText(null);
		// Recupero la riga iva
		RigheIvaTableModel tableModel = (RigheIvaTableModel) TableModelWrapperUtils.getActualTableModel(table
				.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

		if (actualRow == -1) {
			// Sono su un totale
			label.setIcon(null);
			label.setBackground(DefaultCellStyleProvider.SUMMARY_COLOR);
			return label;
		}
		RigaIva ri = tableModel.getObject(actualRow);
		if (ri.getAreaIva().getAreaContabile() != null) {
			if (ri.getAreaIva().getAreaContabile().getTipoAreaContabile().getGestioneIva()
					.compareTo(GestioneIva.NORMALE) != 0) {
				label.setIcon(icon);
				if (ri.getCodiceIvaCollegato() != null) {
					label.setToolTipText(ri.getCodiceIvaCollegato().getCodice() + " - "
							+ ri.getCodiceIvaCollegato().getDescrizioneInterna());
				}
			}
		}
		return label;
	}
}