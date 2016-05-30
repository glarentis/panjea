package it.eurotn.panjea.magazzino.rich.control.table.renderer;

import it.eurotn.panjea.magazzino.domain.AttributoCategoria;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.CategorieTreeTablePage;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class AggiornamentoAttributoCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 1503378619590468740L;

	public static final EditorContext AGGIORNAMENTO_ATTRIBUTO_CONTEXT = new EditorContext(
			"AGGIORNAMENTO_ATTRIBUTO_CONTEXT");

	public static final Icon ICON_CATEGORIA = RcpSupport.getIcon(Categoria.class.getName());
	public static final Icon ICON_SOTTOCATEGORIE = RcpSupport.getIcon(CategorieTreeTablePage.CATEGORIA_ROOT_NODE_ICON);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<AttributoCategoria> tableModel = (DefaultBeanTableModel<AttributoCategoria>) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

		if (actualRow == -1) {
			return label;
		}

		AttributoCategoria attributoCategoria = tableModel.getObject(actualRow);

		StringBuffer sb = new StringBuffer();
		sb.append("<HTML><ul>");
		if (attributoCategoria.isConsideraSottoCategorie()) {
			label.setIcon(ICON_SOTTOCATEGORIE);
			sb.append("<li>Aggiorna anche le sotto categorie</li>");
		} else {
			label.setIcon(ICON_CATEGORIA);
			sb.append("<li>Aggiorna solo la categoria " + attributoCategoria.getCategoria().getDescrizione() + "</li>");
		}
		sb.append("<li>Tipo di aggiornamento: " + label.getText() + "</li>");
		sb.append("</ul></HTML>");
		label.setToolTipText(sb.toString());

		return label;
	}

}
