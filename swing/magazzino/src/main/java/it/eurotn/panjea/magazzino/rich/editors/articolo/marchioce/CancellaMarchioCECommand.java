package it.eurotn.panjea.magazzino.rich.editors.articolo.marchioce;

import it.eurotn.panjea.magazzino.rich.articoli.marchice.IArticoloMarchiCEDAO;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

public class CancellaMarchioCECommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "cancellaMarchioCECommand";
	public static final String PARAM_CODICE_ARTICOLO = "paramCodiceArticolo";
	public static final String PARAM_NOME_IMMAGINE = "paramNomeImmagine";

	private final IArticoloMarchiCEDAO articoloMarchiCEDAO;

	/**
	 * Costruttore.
	 */
	public CancellaMarchioCECommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		articoloMarchiCEDAO = (IArticoloMarchiCEDAO) Application.instance().getApplicationContext()
				.getBean("articoloMarchiCEDAO");
	}

	@Override
	protected void doExecuteCommand() {

		final String codiceArticolo = (String) getParameter(PARAM_CODICE_ARTICOLO, null);
		final String nomeImmagine = (String) getParameter(PARAM_NOME_IMMAGINE, null);

		if (codiceArticolo != null && nomeImmagine != null) {

			ConfirmationDialog confirmationDialog = new ConfirmationDialog("ATTENZIONE",
					"Cancellare il marchio selezionato?") {

				@Override
				protected void onConfirm() {
					articoloMarchiCEDAO.cancellaMarchioCE(codiceArticolo, nomeImmagine);
				}
			};
			confirmationDialog.showDialog();
		}

	}

}
