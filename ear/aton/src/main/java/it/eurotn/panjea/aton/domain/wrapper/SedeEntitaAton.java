package it.eurotn.panjea.aton.domain.wrapper;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;

public class SedeEntitaAton {

	private SedeEntita sedeEntita;
	private SedePagamento sedePagamento;
	private SedeMagazzino sedeMagazzino;

	/**
	 * Costruttore.
	 *
	 * @param sedeEntita
	 *            sede entità
	 * @param sedePagamento
	 *            sede pagamento
	 * @param sedeMagazzino
	 *            sede magazzino
	 */
	public SedeEntitaAton(final SedeEntita sedeEntita, final SedePagamento sedePagamento,
			final SedeMagazzino sedeMagazzino) {
		super();
		this.sedeEntita = sedeEntita;
		this.sedePagamento = sedePagamento;
		this.sedeMagazzino = sedeMagazzino;
	}

	/**
	 *
	 * @return 0 se libera 1 se bloccata
	 */
	public String getBloccata() {
		return getSedeEntita().getEntita().getBloccoSede().isBlocco() || getSedeEntita().getBloccoSede().isBlocco() ? "0"
				: "1";
	}

	/**
	 * @return codiceCategoriaSedeMagazzino
	 */
	public String getCodiceCategoriaSedeMagazzino() {
		String codiceCategoriaSedeMagazzino = "";
		CategoriaSedeMagazzino categoriaSedeMagazzino = sedeMagazzino.getCategoriaSedeMagazzino();
		if (categoriaSedeMagazzino != null) {
			codiceCategoriaSedeMagazzino = categoriaSedeMagazzino.getDescrizione();
		}
		return codiceCategoriaSedeMagazzino;
	}

	/**
	 * @return the codiceDestinatario
	 */
	public String getCodiceDestinatario() {
		if (!sedeEntita.getTipoSede().isSedePrincipale()) {
			return sedeEntita.getCodice();
		}
		return "";
	}

	/**
	 * @return the ragioneSocialeEntita
	 */
	public String getRagioneSocialeEntita() {
		if (!sedeEntita.getTipoSede().isSedePrincipale()) {
			return sedeEntita.getSede().getDescrizione();
		} else {
			return sedeEntita.getEntita().getAnagrafica().getDenominazione();
		}
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the sedeMagazzino
	 */
	public SedeMagazzino getSedeMagazzino() {
		return sedeMagazzino;
	}

	/**
	 * @return the sedePagamento
	 */
	public SedePagamento getSedePagamento() {
		return sedePagamento;
	}

	/**
	 * metodo fake.
	 *
	 * @param bloccata
	 *            .
	 */
	public void setBloccata(String bloccata) {
		throw new UnsupportedOperationException("metodo fake per esportare con BEANIO");
	}

	/**
	 * @param codiceCategoriaSedeMagazzino
	 *            the codiceCategoriaSedeMagazzino to set
	 */
	public void setCodiceCategoriaSedeMagazzino(String codiceCategoriaSedeMagazzino) {

	}

	/**
	 * @param codiceDestinatario
	 *            the codiceDestinatario to set
	 */
	public void setCodiceDestinatario(String codiceDestinatario) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ragioneSocialeEntita
	 *            the ragioneSocialeEntita to set
	 */
	public void setRagioneSocialeEntita(String ragioneSocialeEntita) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param sedeMagazzino
	 *            the sedeMagazzino to set
	 */
	public void setSedeMagazzino(SedeMagazzino sedeMagazzino) {
		this.sedeMagazzino = sedeMagazzino;
	}

	/**
	 * @param sedePagamento
	 *            the sedePagamento to set
	 */
	public void setSedePagamento(SedePagamento sedePagamento) {
		this.sedePagamento = sedePagamento;
	}

}
