package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.factory.table.CustomArticoloRenderer;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class CodiceArticoloCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -6526605820703910193L;

	public static final EditorContext CODICE_ARTICOLO_CONTEXT = new EditorContext("CODICE_ARTICOLO_CONTEXT");
	private final IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(
			IconSource.class);

	/**
	 * Costruttore.
	 */
	public CodiceArticoloCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		String codiceArticolo = (String) value;
		// label.setIcon(null);
		label.setText(codiceArticolo);
		label.setHorizontalAlignment(SwingConstants.LEFT);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<ArticoloRicerca> tableModel = (DefaultBeanTableModel<ArticoloRicerca>) TableModelWrapperUtils
		.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow != -1) {
			ArticoloRicerca articolo = tableModel.getObject(actualRow);
			if (articolo != null) {
				if (articolo.isPadre()) {
					label.setIcon(this.iconSource.getIcon(CustomArticoloRenderer.KEY_ICON_ARTICOLO_PADRE));
				} else if (articolo.isDistinta()) {
					label.setIcon(this.iconSource.getIcon(CustomArticoloRenderer.KEY_ICON_ARTICOLO_DISTINTA));
				} else {
					label.setIcon(this.iconSource.getIcon(ArticoloLite.class.getName()));
				}
			}
		}
		return label;
	}

}
