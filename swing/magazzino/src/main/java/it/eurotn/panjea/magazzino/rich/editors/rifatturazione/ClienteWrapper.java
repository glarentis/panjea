package it.eurotn.panjea.magazzino.rich.editors.rifatturazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;

public class ClienteWrapper {

	private ClienteLite cliente;
	private TipoEntita tipoEntita;

	/**
	 * Costruttore.
	 */
	public ClienteWrapper() {
		super();
		this.tipoEntita = TipoEntita.CLIENTE;
	}

	/**
	 * @return the cliente
	 */
	public ClienteLite getCliente() {
		return cliente;
	}

	/**
	 * @return the tipoEntita
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * @param cliente
	 *            the cliente to set
	 */
	public void setCliente(ClienteLite cliente) {
		this.cliente = cliente;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.tipoEntita = tipoEntita;
	}

}
