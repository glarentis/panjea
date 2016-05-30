/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.VersioneListino;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;

/**
 * Renderer applicato alle celle che visualizzano la data vigore della versione listino. La data valida verrà
 * visualizzata con un'icona accanto.
 *
 * @author fattazzo
 *
 */
public class VersioneListinoDataCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 7516401127095340330L;

	public static final EditorContext VERSIONE_LISTINO_DATA_MAGAZZINO_CONTEXT = new EditorContext(
			"VERSIONE_LISTINO_DATA_MAGAZZINO_CONTEXT");
	private SimpleDateFormat dateFormat;
	private final IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(
			IconSource.class);

	{
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * Costruttore.
	 */
	public VersioneListinoDataCellRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		VersioneListino versioneListinoInVigore = (VersioneListino) VERSIONE_LISTINO_DATA_MAGAZZINO_CONTEXT
				.getUserObject();

		label.setIcon(null);
		label.setHorizontalAlignment(SwingConstants.RIGHT);

		Date dataVersione = (Date) value;
		Date dataValida = null;
		if (versioneListinoInVigore != null) {
			dataValida = versioneListinoInVigore.getDataVigore();
			// se la data della versione valida corrisponde alla data setto l'icona
			if (dataVersione != null && dataValida != null && dataVersione.compareTo(dataValida) == 0) {
				label.setIcon(this.iconSource.getIcon("apply"));
			}
		}
		if (dataVersione != null) {
			label.setText(dateFormat.format(dataVersione));
		}

		return label;
	}
}
