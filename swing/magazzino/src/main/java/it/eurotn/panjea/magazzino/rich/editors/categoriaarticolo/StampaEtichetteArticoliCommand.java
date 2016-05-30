package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class StampaEtichetteArticoliCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "stampaEtichetteArticoliCommand";

	public static final String PARAM_ARTICOLI = "paramArticoli";

	/**
	 * Costruttore.
	 * 
	 */
	public StampaEtichetteArticoliCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		@SuppressWarnings("unchecked")
		List<ArticoloRicerca> articoli = (List<ArticoloRicerca>) getParameter(PARAM_ARTICOLI,
				new ArrayList<ArticoloRicerca>());

		if (!articoli.isEmpty()) {
			ParametriStampaEtichetteArticolo parametriEtichette = new ParametriStampaEtichetteArticolo();

			for (ArticoloRicerca articoloRicerca : articoli) {
				if (articoloRicerca.isAbilitato()) {
					EtichettaArticolo etichettaArticolo = new EtichettaArticolo();
					etichettaArticolo.setArticolo(articoloRicerca.createProxyArticoloLite());
					etichettaArticolo.setNumeroCopiePerStampa(1);
					etichettaArticolo.setNumeroDecimali(articoloRicerca.getNumeroDecimaliPrezzo());
					etichettaArticolo.setPercApplicazioneCodiceIva(articoloRicerca.getPercApplicazioneCodiceIva());
					parametriEtichette.getEtichetteArticolo().add(etichettaArticolo);
				}
			}

			LifecycleApplicationEvent event = new OpenEditorEvent(parametriEtichette);
			Application.instance().getApplicationContext().publishEvent(event);
		}

	}

}
