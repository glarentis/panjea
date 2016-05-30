package it.eurotn.panjea.anagrafica.rich.editors.rubrica;

import it.eurotn.rich.dialog.AbstractFilterSelectionDialog;

import java.awt.Dimension;

import javax.swing.JComponent;

import org.springframework.richclient.application.Application;

import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.spring.richclient.docking.editor.EditorDescriptor;

public class ChooseEmailDialog extends AbstractFilterSelectionDialog {

	private RubricaEditor rubricaEditor;
	private EditorDescriptor rubricaEditorDescriptor;

	private int[] hideColumns = new int[] { 1, 4, 6, 9, 12, 14, 15 };

	/**
	 * Costruttore.
	 */
	public ChooseEmailDialog() {
		super("Seleziona una elemento della rubrica con un indirizzo e-mail valido", Application.instance()
				.getActiveWindow().getControl());
		setPreferredSize(new Dimension(1024, 400));
	}

	@Override
	protected JComponent createSelectionComponent() {
		rubricaEditor = (RubricaEditor) rubricaEditorDescriptor.createPageComponent();
		rubricaEditor.initialize(null);
		rubricaEditor.setDefaultAction(getFinishCommand());
		JComponent selectiomnComponent = rubricaEditor.getControl();

		for (int idxColumn : hideColumns) {
			TableColumnChooser.hideColumn(rubricaEditor.getTreeTable(), idxColumn);
		}
		TableUtils.autoResizeAllColumns(rubricaEditor.getTreeTable());

		if (getFilter() != null && !getFilter().isEmpty()) {
			rubricaEditor.setFilter(getFilter());
		}
		return selectiomnComponent;
	}

	@Override
	protected void disposeDialogContentPane() {
		super.disposeDialogContentPane();
		rubricaEditor.dispose();
	}

	/**
	 * @return Returns the rubricaEditorDescriptor.
	 */
	public EditorDescriptor getRubricaEditorDescriptor() {
		return rubricaEditorDescriptor;
	}

	@Override
	protected Object getSelectedObject() {
		RubricaRow row = rubricaEditor.getRigaSelezionata();
		if (row != null) {
			return rubricaEditor.getRigaSelezionata().getRubricaDTO();
		}
		return null;
	}

	@Override
	protected boolean onFinish() {
		// sovrascrivo perche' getSelectedObject() puo' tornare null
		// (onSelect lancia UnsupportedOperationExc in caso di null)
		if (getSelectedObject() != null) {
			onSelect(getSelectedObject());
		}
		return true;
	}

	/**
	 * @param hideColumns
	 *            the hideColumns to set
	 */
	public void setHideColumns(int[] hideColumns) {
		this.hideColumns = hideColumns;
	}

	/**
	 * @param rubricaEditorDescriptor
	 *            The rubricaEditorDescriptor to set.
	 */
	public void setRubricaEditorDescriptor(EditorDescriptor rubricaEditorDescriptor) {
		this.rubricaEditorDescriptor = rubricaEditorDescriptor;
	}

}