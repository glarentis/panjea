package it.eurotn.panjea.contabilita.rich.forms.areacontabile;

import it.eurotn.panjea.contabilita.domain.NoteAreaContabile;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ApplicationDialog;

import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialLineBorder;
import com.jidesoft.swing.PartialSide;

public class NoteContabilitaEntitaDialog extends ApplicationDialog {

	private NoteAreaContabile note = null;

	/**
	 * Costruttore.
	 *
	 * @param note
	 *            note.
	 */
	NoteContabilitaEntitaDialog(final NoteAreaContabile note) {
		this.note = note;
		this.setTitle("Note documento");
	}

	@Override
	protected JComponent createDialogContentPane() {

		JPanel rootPanel = new JPanel(new GridLayout(3, 0));

		int nrNote = 0;
		if (note.getNoteSede() != null && !note.getNoteSede().isEmpty()) {
			nrNote++;
			rootPanel.add(getComponentForNote("Note sede", note.getNoteSede()));
		}
		if (note.getNoteEntita() != null && !note.getNoteEntita().isEmpty()) {
			nrNote++;
			rootPanel.add(getComponentForNote("Note contabilità", note.getNoteEntita()));
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
			JEditorPane editorPane = htmlPane.getWysEditor();
			editorPane.setName(title.replaceAll(" ", ""));
			panel.add(editorPane, BorderLayout.CENTER);

			panel.setBorder(new JideTitledBorder(new PartialLineBorder(Color.darkGray, 1, PartialSide.NORTH), title));
		}

		return panel;
	}

	@Override
	protected boolean onFinish() {
		return true;
	}

}