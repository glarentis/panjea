package it.eurotn.rich.components.messagealert;

import it.eurotn.rich.dialog.MessageAlert;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import org.springframework.richclient.core.DefaultMessage;

public class ProgressBarMessageAlert extends MessageAlert {

	private JProgressBar progressBarDocumenti;
	private JLabel progressLabelDocumenti;

	private JLabel titleLabel;

	/**
	 * Costruttore.
	 * 
	 * @param title
	 *            titolo da visualizzare
	 */
	public ProgressBarMessageAlert(final String title) {
		super(new DefaultMessage(""));
		this.titleLabel = new JLabel(title);
	}

	@Override
	protected void applyAlertPreferences() {
		super.applyAlertPreferences();
		getAlert().setTimeout(0);
		getAlert().setTransient(false);
	}

	/**
	 * Chiuda il messaggio di alert.
	 */
	@Override
	public void closeAlert() {
		getAlert().hidePopup();
	}

	@Override
	protected JComponent getComponent() {

		JPanel rootPanel = new JPanel(new BorderLayout());

		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(getInfoIcon());
		iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		rootPanel.add(iconLabel, BorderLayout.LINE_START);

		JPanel panel = new JPanel(new BorderLayout());

		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		panel.add(titleLabel, BorderLayout.NORTH);

		JPanel panelProgress = new JPanel(new BorderLayout());
		panelProgress.setOpaque(false);

		progressLabelDocumenti.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		panelProgress.add(progressLabelDocumenti, BorderLayout.NORTH);

		progressBarDocumenti = new JProgressBar(0, 0);
		progressBarDocumenti.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
		progressBarDocumenti.setStringPainted(true);
		progressBarDocumenti.setString("0/0");
		panelProgress.add(progressBarDocumenti, BorderLayout.CENTER);

		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.setOpaque(false);
		panel2.add(panelProgress, BorderLayout.NORTH);

		panel.add(panel2, BorderLayout.CENTER);

		rootPanel.add(panel, BorderLayout.CENTER);

		return rootPanel;
	}

	/**
	 * @return getter of iconInfo
	 */
	private Icon getInfoIcon() {
		return UIManager.getIcon("OptionPane.informationIcon");
	}

	/**
	 * Incrementa il valore della progress bar.
	 * 
	 */
	public void incrementaOperazione() {
		progressBarDocumenti.setValue(progressBarDocumenti.getValue() + 1);
		progressBarDocumenti.setString(progressBarDocumenti.getValue() + "/" + progressBarDocumenti.getMaximum());
	}

	/**
	 * Setta la descrizione dell'attuale operazione.
	 * 
	 * @param descrizioneOperazione
	 *            descrizione dell'operazione da visualizzare
	 */
	public void setDescrizioneOperazione(String descrizioneOperazione) {
		if (progressLabelDocumenti == null) {
			progressLabelDocumenti = new JLabel();
		}

		progressLabelDocumenti.setText(descrizioneOperazione);
	}

	/**
	 * Setta il numero di operazioni da compiere.
	 * 
	 * @param numeroOperazioni
	 *            numero operazioni
	 */
	public void setNumeroOperazioni(int numeroOperazioni) {
		progressBarDocumenti.setMaximum(numeroOperazioni);
	}

	/**
	 * Setta il valore della progress bar.
	 * 
	 * @param operazione
	 *            operazione
	 * 
	 */
	public void setOperazione(int operazione) {
		progressBarDocumenti.setValue(operazione);
		progressBarDocumenti.setString(operazione + "/" + progressBarDocumenti.getMaximum());
	}
}