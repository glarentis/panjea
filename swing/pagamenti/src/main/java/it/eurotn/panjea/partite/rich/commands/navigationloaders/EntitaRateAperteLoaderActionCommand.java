/**
 * 
 */
package it.eurotn.panjea.partite.rich.commands.navigationloaders;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.commands.navigationloaders.EntitaLoaderActionCommand;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 * 
 */
public class EntitaRateAperteLoaderActionCommand extends EntitaLoaderActionCommand {

	/**
	 * Costruttore.
	 */
	public EntitaRateAperteLoaderActionCommand() {
		super("entitaRateAperteLoaderActionCommand");
	}

	@Override
	protected OpenEditorEvent getOpenEditorEvent() {
		Entita entita = getEntita();

		if (entita == null) {
			return null;
		}

		switch (entita.getTipo()) {
		case CLIENTE:
			return getOpenEditorEventParametriRicercaRate(entita, TipoPartita.ATTIVA);
		case FORNITORE:
			return getOpenEditorEventParametriRicercaRate(entita, TipoPartita.PASSIVA);
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param entita
	 *            entita
	 * @param tipoPartita
	 *            tipopartita
	 * @return OpenEditorEvent
	 */
	private OpenEditorEvent getOpenEditorEventParametriRicercaRate(final Entita entita, final TipoPartita tipoPartita) {
		ParametriRicercaRate parametriRicerca = ParametriRicercaRate.creaParametriRicercaRateAperte(entita
				.getEntitaLite());
		parametriRicerca.setTipoPartita(tipoPartita);
		return new OpenEditorEvent(parametriRicerca);
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Cliente.class, ClienteLite.class, Fornitore.class, FornitoreLite.class,
				EntitaDocumento.class };
	}

}
