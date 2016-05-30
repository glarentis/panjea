package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.rich.bd.AnagraficaOrdiniBD;
import it.eurotn.panjea.ordini.rich.bd.IAnagraficaOrdiniBD;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.richclient.util.RcpSupport;

public class EntitaSenzaTipoDocumentoEvasioneDialog extends PanjeaTitledPageApplicationDialog {

	private IAnagraficaOrdiniBD anagraficaOrdiniBD;

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            eccazione da gestire
	 * 
	 */
	public EntitaSenzaTipoDocumentoEvasioneDialog(final EntitaSenzaTipoDocumentoEvasioneException exception) {
		super(new EntitaSenzaTipoDocumentoEvasionePage(exception));
		setPreferredSize(new Dimension(800, 600));
		this.anagraficaOrdiniBD = RcpSupport.getBean(AnagraficaOrdiniBD.BEAN_ID);
	}

	@Override
	protected boolean isMessagePaneVisible() {
		return false;
	}

	@Override
	protected boolean onFinish() {
		Collection<EntitaEvasione> entitaEvasione = ((EntitaSenzaTipoDocumentoEvasionePage) getDialogPage())
				.getEntitaEvasione();

		Map<TipoAreaMagazzino, Set<EntitaLite>> mapAssociazione = new HashMap<TipoAreaMagazzino, Set<EntitaLite>>();

		for (EntitaEvasione entitaEv : entitaEvasione) {
			if (entitaEv.getTipoAreaEvasione().isNew()) {
				continue;
			}
			if (mapAssociazione.containsKey(entitaEv.getTipoAreaEvasione())) {
				mapAssociazione.get(entitaEv.getTipoAreaEvasione()).add(entitaEv.getEntita());
			} else {
				Set<EntitaLite> setEntita = new HashSet<EntitaLite>();
				setEntita.add(entitaEv.getEntita());
				mapAssociazione.put(entitaEv.getTipoAreaEvasione(), setEntita);
			}
		}

		anagraficaOrdiniBD.associaTipoAreaEvasione(mapAssociazione);

		return true;
	}

}
