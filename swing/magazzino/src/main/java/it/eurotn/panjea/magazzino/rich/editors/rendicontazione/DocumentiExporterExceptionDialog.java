package it.eurotn.panjea.magazzino.rich.editors.rendicontazione;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.service.exception.DocumentiExporterException;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

public class DocumentiExporterExceptionDialog extends ApplicationDialog {

	public static final String DIALOG_ID = "documentiExporterExceptionDialog";
	public static final String CODICE_ESPORTAZIONE_MANCANTE = "codiceEsportazioneMancante";
	public static final String SEDI_CON_CODICE_MANCANTE_TITLE = "sediConCodiceMancanteTitle";

	private final DocumentiExporterException exception;

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            eccezione da gestire
	 */
	public DocumentiExporterExceptionDialog(final DocumentiExporterException exception) {
		super();
		this.exception = exception;
	}

	/**
	 * @return controlli per la visualizzaizone del codice esportazione mancante
	 */
	private JComponent createCodiceEsportazioneMancanteControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
				.getService(MessageSourceAccessor.class);
		String tipoEsportazione = exception.getNomeTipoEsportazione();
		String text = messageSourceAccessor.getMessage(DIALOG_ID + "." + CODICE_ESPORTAZIONE_MANCANTE,
				new Object[] { tipoEsportazione }, Locale.getDefault());
		JLabel label = getComponentFactory().createLabel(text);
		rootPanel.add(label, BorderLayout.CENTER);

		return rootPanel;
	}

	@Override
	protected JComponent createDialogContentPane() {

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 15));

		if (exception.getNomeTipoEsportazione() != null) {
			rootPanel.add(createCodiceEsportazioneMancanteControl(), BorderLayout.NORTH);
		}
		if (!exception.getSediSenzaCodice().isEmpty()) {
			rootPanel.add(createSediMancantiControl(), BorderLayout.CENTER);
		}

		return rootPanel;
	}

	/**
	 * @return controlli per la visualizzaizone delle sedi che non hanno il codice settato
	 */
	private JComponent createSediMancantiControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.setBorder(BorderFactory.createTitledBorder(RcpSupport.getMessage(DIALOG_ID + "."
				+ SEDI_CON_CODICE_MANCANTE_TITLE)));

		JideTableWidget<SedeEntita> tableWidget = new JideTableWidget<SedeEntita>("SediConCodiceMancanteTable",
				new String[] { "sede.descrizione", "sede.indirizzo", "tipoSede.descrizione",
						"sede.datiGeografici.localita" }, SedeEntita.class);
		tableWidget.setRows(exception.getSediSenzaCodice());
		rootPanel.add(tableWidget.getComponent(), BorderLayout.CENTER);

		return rootPanel;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return (new AbstractCommand[] { getFinishCommand() });
	}

	@Override
	protected String getTitle() {
		return RcpSupport.getMessage(DIALOG_ID + ".title");
	}

	@Override
	protected boolean onFinish() {
		return true;
	}

}
