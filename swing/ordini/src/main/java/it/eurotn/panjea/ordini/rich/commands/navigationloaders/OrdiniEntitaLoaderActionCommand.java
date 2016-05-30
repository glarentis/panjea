package it.eurotn.panjea.ordini.rich.commands.navigationloaders;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.commands.navigationloaders.EntitaLoaderActionCommand;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine.STATO_ORDINE;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OrdiniEntitaLoaderActionCommand extends EntitaLoaderActionCommand {

	/**
	 * constructor.
	 */
	public OrdiniEntitaLoaderActionCommand() {
		super("ordiniEntitaLoaderActionCommand");
	}

	@Override
	protected OpenEditorEvent getOpenEditorEvent() {
		Entita entita = getEntita();

		if (entita == null) {
			return null;
		}

		switch (entita.getTipo()) {
		case CLIENTE:
		case FORNITORE:
			ParametriRicercaAreaOrdine parametriRicerca = new ParametriRicercaAreaOrdine();
			parametriRicerca.setEntita(entita.getEntitaLite());
			parametriRicerca.setStatoOrdine(STATO_ORDINE.NON_EVASO);
			parametriRicerca.setEffettuaRicerca(true);
			return new OpenEditorEvent(parametriRicerca);
		default:
			return null;
		}
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Cliente.class, ClienteLite.class, Fornitore.class, FornitoreLite.class,
				EntitaDocumento.class };
	}
}
