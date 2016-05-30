package it.eurotn.panjea.anagrafica.rich.editors.audit;

import it.eurotn.panjea.anagrafica.rich.bd.IAuditBD;
import it.eurotn.panjea.audit.envers.RevInf;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.springframework.richclient.progress.ProgressMonitor;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableScrollPane;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

public class AuditEditor extends AbstractEditor {

	private IAuditBD auditBD;
	private JComponent rootPanel;
	private Map<RevInf, List<Object>> auditData;

	/**
	 * @return Returns the auditBD.
	 */
	public IAuditBD getAuditBD() {
		return auditBD;
	}

	@Override
	public JComponent getControl() {
		if (rootPanel == null) {
			rootPanel = new JPanel(new BorderLayout());
		}
		if (auditData != null) {
			return rootPanel;
		}
		it.eurotn.panjea.utils.AuditObject auditObject = (it.eurotn.panjea.utils.AuditObject) getEditorInput();
		// JideTable table = null;
		auditData = auditBD.getVersioni(auditObject.getAuditObject());
		if (auditData.size() > 0) {
			AuditTableModel tableModel = new AuditTableModel(auditObject.getAuditObject(), auditData);
			TableScrollPane tableScrollPane = new TableScrollPane(tableModel);
			JideTableWidget.customizeTable((JideTable) tableScrollPane.getMainTable());
			JideTableWidget.customizeTable((JideTable) tableScrollPane.getRowHeaderTable());
			tableScrollPane.getRowHeaderTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tableScrollPane.getMainTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableUtils.autoResizeAllColumns(tableScrollPane.getRowHeaderTable());
			TableUtils.autoResizeAllColumns(tableScrollPane.getMainTable());
			rootPanel.add(tableScrollPane, BorderLayout.CENTER);
		} else {
			rootPanel.add(new JLabel("Nessun dato di audit disponibile"));
		}
		return rootPanel;
	}

	@Override
	public String getId() {
		return "auditEditor";
	}

	@Override
	public void initialize(Object editorObject) {
		if (rootPanel != null) {
			auditData = null;
			rootPanel.removeAll();
			getControl();
		}
	}

	@Override
	public void save(ProgressMonitor arg0) {
	}

	/**
	 * @param auditBD
	 *            The auditBD to set.
	 */
	public void setAuditBD(IAuditBD auditBD) {
		this.auditBD = auditBD;
	}

}
