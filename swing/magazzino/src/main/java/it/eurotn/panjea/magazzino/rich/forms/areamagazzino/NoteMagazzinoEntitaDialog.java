package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ApplicationDialog;

import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialLineBorder;
import com.jidesoft.swing.PartialSide;

public class NoteMagazzinoEntitaDialog extends ApplicationDialog {

	private NoteAreaMagazzino note = null;

	/**
	 * Costruttore.
	 *
	 * @param note
	 *            note.
	 */
	NoteMagazzinoEntitaDialog(final NoteAreaMagazzino note) {
		this.note = note;
		this.setTitle("Note documento");
	}

	@Override
	protected JComponent createDialogContentPane() {

		JPanel rootPanel = new JPanel(new GridLayout(3, 0));

		int nrNote = 0;
		if (note.getNoteBlocco() != null && !note.getNoteBlocco().isEmpty()) {
			nrNote++;
			rootPanel.add(getComponentForNote("Note blocco", note.getNoteBlocco()));
		}
		if (note.getNoteSede() != null && !note.getNoteSede().isEmpty()) {
			nrNote++;
			rootPanel.add(getComponentForNote("Note sede", note.getNoteSede()));
		}
		if (note.getNoteEntita() != null && !note.getNoteEntita().isEmpty()) {
			nrNote++;
			rootPanel.add(getComponentForNote("Note magazzino", note.getNoteEntita()));
		}

		rootPanel.setLayout(new GridLayout(nrNote, 0));

		return rootPanel;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new AbstractCommand[] { getFinishCommand() };
	}

	/**
	 * Crea il componente per le note.
	 *
	 * @param title
	 *            titolo
	 * @param nota
	 *            note
	 * @return componente
	 */
	private JComponent getComponentForNote(String title, String nota) {
		JPanel panel = new JPanel(new BorderLayout());

		if (nota != null) {
			HTMLEditorPane htmlPane = new HTMLEditorPane();
			htmlPane.setText(nota);
			htmlPane.getWysEditor().setEditable(false);
			htmlPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			final JEditorPane editorPane = htmlPane.getWysEditor();
			editorPane.setName(title.replaceAll(" ", ""));
			panel.add(new JScrollPane(editorPane), BorderLayout.CENTER);
			editorPane.setCaretPosition(0);
			panel.setBorder(new JideTitledBorder(new PartialLineBorder(Color.darkGray, 1, PartialSide.NORTH), title));
		}
		return panel;
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();
		// Automaticamente il focus su un componente di testo seleziona tutto il teso. Rimuovo perchè in questo caso
		// risulta scomodo. Lo riattivo alla chiusura del dialogo
		PanjeaSwingUtil.removeTraceFocusAndSelectAll();
	}

	@Override
	protected boolean onFinish() {
		PanjeaSwingUtil.traceFocusAndSelectAll();
		return true;
	}

}