package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenSituazioneCauzioniCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "openSituazioneCauzioniCommand";

	public static final String PARAM_ID_ENTITA = "paramIdEntita";
	public static final String PARAM_ID_ARTICOLO = "paramIdArticolo";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public OpenSituazioneCauzioniCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {

		EntitaLite entita = (EntitaLite) getParameter(PARAM_ID_ENTITA, null);
		ArticoloLite articolo = (ArticoloLite) getParameter(PARAM_ID_ARTICOLO, null);

		// verifico se l'articolo è di tipo cuzione
		if (articolo != null && articolo.getId() != null) {
			// Carico l'articolo lite. L'articolo che proviene dalla getParamenter non ha tutti i parametri che mi
			// servono-
			articolo = magazzinoAnagraficaBD.caricaArticoloLite(articolo.getId());
			if (!articolo.getCategoria().isCauzione()) {
				articolo = null;
			}
		}

		ParametriRicercaSituazioneCauzioni parametri = new ParametriRicercaSituazioneCauzioni();
		parametri.setArticolo(articolo);
		parametri.setEntita(entita);
		parametri.setEffettuaRicerca(true);

		LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
		Application.instance().getApplicationContext().publishEvent(event);
	}

}
