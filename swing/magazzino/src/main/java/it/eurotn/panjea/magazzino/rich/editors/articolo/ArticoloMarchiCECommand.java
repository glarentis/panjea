package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.editors.articolo.marchioce.ArticoloMarchiCEDialog;
import it.eurotn.panjea.rich.bd.IPreferenceBD;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class ArticoloMarchiCECommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "articoloMarchiCECommand";
	public static final String PARAM_ARTICOLO = "paramArticolo";

	/**
	 * Costruttore.
	 */
	public ArticoloMarchiCECommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		IPreferenceBD preferenceBD = (IPreferenceBD) Application.instance().getApplicationContext()
				.getBean("preferenceBD");

		Boolean gestioneMarchiCE = null;
		try {
			Preference preference = preferenceBD.caricaPreference("marchiCE");
			gestioneMarchiCE = new Boolean(preference.getValore());
		} catch (Exception e) {
			logger.error("--> Errore durante il recupero della preference marchiCE", e);
		}
		if (gestioneMarchiCE == null) {
			gestioneMarchiCE = false;
		}

		setVisible(gestioneMarchiCE);
	}

	@Override
	protected void doExecuteCommand() {

		Articolo articolo = (Articolo) getParameter(PARAM_ARTICOLO, null);

		if (articolo != null) {
			ArticoloMarchiCEDialog articoloMarchiCEDialog = new ArticoloMarchiCEDialog(articolo.getCodice());
			articoloMarchiCEDialog.showDialog();
		}
	}

}
