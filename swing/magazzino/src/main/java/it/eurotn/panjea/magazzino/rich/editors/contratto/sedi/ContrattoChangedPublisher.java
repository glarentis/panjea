package it.eurotn.panjea.magazzino.rich.editors.contratto.sedi;

import it.eurotn.panjea.magazzino.domain.Contratto;

import org.springframework.binding.value.support.AbstractPropertyChangePublisher;

public class ContrattoChangedPublisher extends AbstractPropertyChangePublisher {

	private String publisherId = null;

	/**
	 * Costruttore di default.
	 */
	public ContrattoChangedPublisher(String publisherId) {
		super();
		this.publisherId = publisherId;
	}

	/**
	 * Pubblica che il contratto e' cambiato.<br/>
	 * 
	 * @param contratto
	 *            il nuovo contratto da pubblicate
	 */
	public void publish(Contratto contratto) {
		firePropertyChange(publisherId, null, contratto);
	}

}