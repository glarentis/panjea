package it.eurotn.panjea.magazzino.rich.editors.schedearticolo;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class CreaSchedeArticoloCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "creaSchedeArticoloCommand";

	public static final String PARAM_ARTICOLI = "paramArticoli";
	public static final String PARAM_ANNO = "paramAnno";
	public static final String PARAM_MESE = "paramMese";

	/**
	 * Costruttore.
	 */
	public CreaSchedeArticoloCommand() {
		this(COMMAND_ID);
	}

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 *            id del comando
	 */
	public CreaSchedeArticoloCommand(final String commandId) {
		super(commandId);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		@SuppressWarnings("unchecked")
		final Collection<ArticoloRicerca> articoli = (Collection<ArticoloRicerca>) getParameter(PARAM_ARTICOLI,
				new ArrayList<ArticoloRicerca>());
		final Integer anno = (Integer) getParameter(PARAM_ANNO, null);
		final Integer mese = (Integer) getParameter(PARAM_MESE, null);

		if (articoli.isEmpty() || anno == null || mese == null) {
			System.err.println("Impostare tutti i parametri per la creazione delle schede articolo");
			return;
		}

		ParametriCreazioneSchedeArticoli parametri = new ParametriCreazioneSchedeArticoli();
		parametri.setAnno(anno);
		parametri.setMese(mese);
		parametri.getArticoli().addAll(articoli);
		LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
		Application.instance().getApplicationContext().publishEvent(event);

	}

}