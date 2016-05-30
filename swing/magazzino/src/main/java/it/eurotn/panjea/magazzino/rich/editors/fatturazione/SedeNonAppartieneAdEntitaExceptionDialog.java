package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.exception.SedeNonAppartieneAdEntitaException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.MessageDialog;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.JideBoxLayout;

public class SedeNonAppartieneAdEntitaExceptionDialog extends MessageDialog {

	private final SedeNonAppartieneAdEntitaException sedeException;

	/**
	 * 
	 * @param exception
	 *            exception
	 */
	public SedeNonAppartieneAdEntitaExceptionDialog(final SedeNonAppartieneAdEntitaException exception) {
		super("Sedi non appartenenti ad entità", "Sedi non valide");
		this.sedeException = exception;
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = getComponentFactory().createPanel();
		JideBoxLayout layout = new JideBoxLayout(rootPanel, JideBoxLayout.Y_AXIS);
		layout.setGap(2);
		rootPanel.setLayout(layout);

		for (Documento documento : sedeException.getDocumenti()) {
			JLabel label = new JLabel(ObjectConverterManager.toString(documento, Documento.class, null));
			rootPanel.add(label);
		}
		return rootPanel;
	}

}
