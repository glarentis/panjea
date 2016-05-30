package it.eurotn.panjea.ordini.rich.editors.righeordine.divisioneriga;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JLabel;

import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class DividiRigaOrdineTablePage extends AbstractTablePageEditor<RigaArticolo> {

	private Double qta;

	public DividiRigaOrdineTablePage(RigaArticolo rigaArticoloOriginale) {
		super("evasionePreventivoTablePage", new DividiRigaOrdineTableModel(rigaArticoloOriginale));
		qta = rigaArticoloOriginale.getQta();
		setShowTitlePane(false);
		setTitle("Divisione riga");
	}

	@Override
	public JComponent getHeaderControl() {
		return new JLabel("Qta tot riga:" + qta);
	}

	@Override
	public Collection<RigaArticolo> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
		// getTable().getTable().requestFocusInWindow();
		// getTable().getTable().editCellAt(0, 0);
	}

	@Override
	public Collection<RigaArticolo> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
