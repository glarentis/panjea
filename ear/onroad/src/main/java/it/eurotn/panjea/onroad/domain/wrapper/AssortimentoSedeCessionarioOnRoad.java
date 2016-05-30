package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;

public class AssortimentoSedeCessionarioOnRoad {

	private SedeEntita sedeCliente;
	private SedeEntita sedeFornitore;
	private Articolo articolo;
	private String codiceArticolo;

	/**
	 * Costruttore.
	 */
	public AssortimentoSedeCessionarioOnRoad() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param codiceArticoloEntita
	 *            codiceArticoloEntita
	 * @param sedeEntita
	 *            sedeCliente o sedeFornitore
	 */
	public AssortimentoSedeCessionarioOnRoad(final CodiceArticoloEntita codiceArticoloEntita,
			final SedeEntita sedeEntita) {
		super();
		setArticolo(codiceArticoloEntita.getArticolo());
		setCodiceArticolo(codiceArticoloEntita.getCodice());
		setSedeEntita(sedeEntita);
	}

	/**
	 * @return the articolo
	 */
	public Articolo getArticolo() {
		return articolo;
	}

	/**
	 * @return codice agente o 0000 se null
	 */
	public String getCodiceAgente() {
		String codiceAgente = "0000";
		if (sedeCliente != null) {
			AgenteLite agente = sedeCliente.getAgente();
			if (agente != null && agente.getCodiceAton() != null && !agente.getCodiceAton().isEmpty()) {
				codiceAgente = agente.getCodiceAton();
			}
		}
		return codiceAgente;
	}

	/**
	 * @return the codiceArticolo
	 */
	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	/**
	 * @return codice assortimento
	 */
	public String getCodiceAssortimento() {
		if (sedeCliente != null && sedeFornitore != null) {
			return getSedeCliente().getEntita().getCodice() + "" + getSedeFornitore().getEntita().getCodice();
		}
		return "";
	}

	/**
	 * @return the codiceDestinatario
	 */
	public String getCodiceDestinatario() {
		if (sedeCliente != null && !sedeCliente.getTipoSede().isSedePrincipale()) {
			return sedeCliente.getCodice();
		} else if (sedeCliente != null) {
			return sedeCliente.getEntita().getCodice() + "";
		}
		return "";
	}

	/**
	 * @return articoloId#sedeClienteId#sedeFornitoreId
	 */
	public String getMapKeyAssort() {
		return getArticolo().getId() + "#" + getSedeCliente().getId() + "#" + getSedeFornitore().getId();
	}

	/**
	 * @return the sedeCliente
	 */
	public SedeEntita getSedeCliente() {
		return sedeCliente;
	}

	/**
	 * @return the sedeFornitore
	 */
	public SedeEntita getSedeFornitore() {
		return sedeFornitore;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param codiceAgente
	 *            the codiceAgente to set
	 */
	public void setCodiceAgente(String codiceAgente) {
		// non faccio nulla, non rilancio una eccezione perchè uso la copyproperties
	}

	/**
	 * @param codiceArticolo
	 *            the codiceArticolo to set
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	/**
	 * @param codiceAssortimento
	 *            the codiceAssortimento to set
	 */
	public void setCodiceAssortimento(String codiceAssortimento) {
		// non faccio nulla, non rilancio una eccezione perchè uso la copyproperties
	}

	/**
	 * @param codiceDestinatario
	 *            the codiceDestinatario to set
	 */
	public void setCodiceDestinatario(String codiceDestinatario) {
		// non faccio nulla, non rilancio una eccezione perchè uso la copyproperties
	}

	/**
	 * @param sedeCliente
	 *            the sedeCliente to set
	 */
	public void setSedeCliente(SedeEntita sedeCliente) {
		this.sedeCliente = sedeCliente;
	}

	/**
	 * @param sedeEntita
	 *            sedeCliente or sedeFornitore to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		if (sedeEntita.getEntita() instanceof Cliente) {
			setSedeCliente(sedeEntita);
		} else {
			setSedeFornitore(sedeEntita);
		}
	}

	/**
	 * @param sedeFornitore
	 *            the sedeFornitore to set
	 */
	public void setSedeFornitore(SedeEntita sedeFornitore) {
		this.sedeFornitore = sedeFornitore;
	}

}
