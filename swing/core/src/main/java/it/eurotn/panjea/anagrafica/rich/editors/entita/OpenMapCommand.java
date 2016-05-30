package it.eurotn.panjea.anagrafica.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.rich.editors.webbrowser.PanjeaUrl;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenMapCommand extends ActionCommand {
	public static final String SEDE_ANAGRAFICA = "sedeAnagrafica";

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public OpenMapCommand() {
		super("openMapCommand");
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		final SedeAnagrafica sedeAnagrafica = (SedeAnagrafica) getParameter(SEDE_ANAGRAFICA);
		PanjeaUrl url = new PanjeaUrl() {

			@Override
			public String getDisplayName() {
				return String.format("Mappa per %s", sedeAnagrafica.getDescrizione());
			}

		};
		url.setIndirizzo(sedeAnagrafica.getMapUrl());
		LifecycleApplicationEvent event = new OpenEditorEvent(url);
		Application.instance().getApplicationContext().publishEvent(event);
	}

}