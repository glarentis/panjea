package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.JideSplitPane;

public class SedePerRifatturazioneExceptionDialog extends ConfirmationDialog {

	public static final String DIALOG_ID = "sedePerRifatturazioneExceptionDialog";
	public static final String MOVIMENTI_SENZA_SEDE_TITLE = DIALOG_ID + ".movimentiSenzaSede.title";

	public static final String MOVIMENTI_CON_SEDE_DIVERSA_TITLE = DIALOG_ID + ".movimentiConSedeDiversa.title";
	public static final String MOVIMENTI_CON_SEDE_DIVERSA_MESSAGE = DIALOG_ID + ".movimentiConSedeDiversa.message";

	public static final String CONFIRMATION_MESSAGE = DIALOG_ID + ".confirmation.message";

	private final SedePerRifatturazioneAssenteException sedePerRifatturazioneAssenteException;

	private boolean confirm = false;

	/**
	 * Costruttore.
	 * 
	 * @param sedePerRifatturazioneAssenteException
	 *            eccezione da visualizzare nel dialog
	 */
	public SedePerRifatturazioneExceptionDialog(
			final SedePerRifatturazioneAssenteException sedePerRifatturazioneAssenteException) {
		super();
		this.sedePerRifatturazioneAssenteException = sedePerRifatturazioneAssenteException;
		this.setCloseAction(CloseAction.HIDE);
		this.setPreferredSize(new Dimension(800, 600));
	}

	/**
	 * Crea i componenti per la visualizzazione delle aree magazzino che non
	 * hanno nessuna sede per rifatturazione impostata.
	 * 
	 * @return controlli creati
	 */
	private JComponent createAreeMagazzinoSedeAssenteComponent() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		JideTableWidget<AreaMagazzino> tableWidget = new JideTableWidget<AreaMagazzino>(
				"areeMagazzinoSenzaSede",
				new String[] { "documento.tipoDocumento", "dataRegistrazione", "documento.codice",
						"documento.entita.codice", "documento.entita.anagrafica.denominazione", "documento.sedeEntita" },
				AreaMagazzino.class);
		rootPanel.add(tableWidget.getComponent(), BorderLayout.CENTER);
		tableWidget.setRows(sedePerRifatturazioneAssenteException.getAreeMagazzinoSenzaSedeRifatturazione());

		GuiStandardUtils.attachBorder(rootPanel);
		rootPanel.setBorder(BorderFactory.createTitledBorder(RcpSupport.getMessage(MOVIMENTI_SENZA_SEDE_TITLE)));

		return rootPanel;
	}

	/**
	 * Crea i componenti per la visualizzazione delle aree magazzino che hanno
	 * comesede per rifatturazione una sede diversa da quella selezionata.
	 * 
	 * @return controlli creati
	 */
	private JComponent createAreeMagazzinoSedeDiversaComponent() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		JideTableWidget<AreaMagazzino> tableWidget = new JideTableWidget<AreaMagazzino>("areeMagazzinoSenzaSede",
				new String[] { "documento.tipoDocumento", "dataRegistrazione", "documento.codice",
						"documento.entita.codice", "documento.entita.anagrafica.denominazione", "sedeEntita" },
				AreaMagazzino.class);
		rootPanel.add(tableWidget.getComponent(), BorderLayout.CENTER);
		tableWidget.setRows(sedePerRifatturazioneAssenteException.getAreeMagazzinoConDiversaSedeRifatturazione());

		JLabel labelMovimentiSedeDiversa = getComponentFactory().createLabel(MOVIMENTI_CON_SEDE_DIVERSA_MESSAGE);
		labelMovimentiSedeDiversa.setBackground(Color.RED);
		rootPanel.add(labelMovimentiSedeDiversa, BorderLayout.SOUTH);

		GuiStandardUtils.attachBorder(rootPanel);
		rootPanel.setBorder(BorderFactory.createTitledBorder(RcpSupport.getMessage(MOVIMENTI_CON_SEDE_DIVERSA_TITLE)));

		getFinishCommand().setEnabled(false);

		return rootPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.dialog.ConfirmationDialog#
	 * createDialogContentPane()
	 */
	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		boolean movimentiConSediDiverse = !sedePerRifatturazioneAssenteException
				.getAreeMagazzinoConDiversaSedeRifatturazione().isEmpty();

		rootPanel.add(createSedePerRifatturazioneComponent(), BorderLayout.NORTH);

		JideSplitPane splitPane = new JideSplitPane();
		splitPane.setOrientation(JideSplitPane.VERTICAL_SPLIT);
		splitPane.add(createAreeMagazzinoSedeAssenteComponent());
		if (movimentiConSediDiverse) {
			splitPane.add(createAreeMagazzinoSedeDiversaComponent());
		}
		splitPane.setShowGripper(true);
		rootPanel.add(splitPane, BorderLayout.CENTER);

		JLabel labelConfirmationMessage = getComponentFactory().createLabel(CONFIRMATION_MESSAGE);
		rootPanel.add(labelConfirmationMessage, BorderLayout.SOUTH);

		return rootPanel;
	}

	/**
	 * Crea i componenti per visualizzare la sede scelta per la rifatturazione.
	 * 
	 * @return controlli creati
	 */
	private JComponent createSedePerRifatturazioneComponent() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		JLabel labelSedePerRifatturazione = getComponentFactory().createLabel("");
		labelSedePerRifatturazione.setText(RcpSupport.getMessage("sedePerRifatturazione") + ": "
				+ ObjectConverterManager.toString(sedePerRifatturazioneAssenteException.getSedePerRifatturazione()));
		labelSedePerRifatturazione.setIcon(RcpSupport.getIcon(SedeMagazzino.class.getName()));
		rootPanel.add(labelSedePerRifatturazione, BorderLayout.LINE_START);

		GuiStandardUtils.attachBorder(rootPanel);

		return rootPanel;
	}

	/**
	 * @return the confirm
	 */
	public boolean isConfirm() {
		return confirm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.dialog.ApplicationDialog#onCancel()
	 */
	@Override
	protected void onCancel() {
		super.onCancel();
		confirm = false;
	}

	@Override
	protected void onConfirm() {
		confirm = true;
	}

}
