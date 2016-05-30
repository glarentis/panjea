package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.updateultimocosto;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.RigaListino;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RigaListinoAggioramento {
	private BigDecimal prezzoDaAggiornare;
	private Boolean aggiorna;
	private RigaListino rigaListino;
	private int numeroDecimali;

	/**
	 * Costruttore.
	 */
	public RigaListinoAggioramento() {
		aggiorna = false;
	}

	/**
	 * @return the aggiorna
	 */
	public Boolean getAggiorna() {
		return aggiorna;
	}

	/**
	 * @return the codiceListino
	 */
	public String getCodiceListino() {
		return rigaListino.getVersioneListino().getListino().getCodice() + " - "
				+ rigaListino.getVersioneListino().getListino().getDescrizione();
	}

	/**
	 * @return the importoVariazione
	 */
	public BigDecimal getImportoVariazione() {
		if (prezzoDaAggiornare != null && rigaListino != null && rigaListino.getPrezzo() != null) {
			return prezzoDaAggiornare.subtract(rigaListino.getPrezzo());
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * @return the numeroDecimali
	 */
	public int getNumeroDecimali() {
		return numeroDecimali;
	}

	/**
	 * @return the percVariazione
	 */
	public BigDecimal getPercVariazione() {
		BigDecimal result = BigDecimal.ZERO;
		try {
			result = getImportoVariazione().multiply(Importo.HUNDRED).divide(rigaListino.getPrezzo(),
					RoundingMode.HALF_UP);
		} catch (Exception e) {
			return result;
		}
		return result;
	}

	/**
	 * @return the prezzoDaAggiornare
	 */
	public BigDecimal getPrezzoDaAggiornare() {
		return prezzoDaAggiornare;
	}

	/**
	 * 
	 * @return PrezzoListino
	 */
	public BigDecimal getPrezzoListino() {
		return rigaListino.getPrezzo();
	}

	/**
	 * @return the rigaListino
	 */
	public RigaListino getRigaListino() {
		return rigaListino;
	}

	/**
	 * @param aggiorna
	 *            the aggiorna to set
	 */
	public void setAggiorna(Boolean aggiorna) {
		this.aggiorna = aggiorna;
	}

	/**
	 * @param importoVariazione
	 *            the importoVariazione to set
	 */
	public void setImportoVariazione(BigDecimal importoVariazione) {
		prezzoDaAggiornare = rigaListino.getPrezzo().add(importoVariazione);
	}

	/**
	 * @param numeroDecimali
	 *            the numeroDecimali to set
	 */
	public void setNumeroDecimali(int numeroDecimali) {
		this.numeroDecimali = numeroDecimali;
	}

	/**
	 * @param percVariazione
	 *            the percVariazione to set
	 */
	public void setPercVariazione(BigDecimal percVariazione) {
		prezzoDaAggiornare = rigaListino.getPrezzo().add(
				percVariazione.divide(Importo.HUNDRED).multiply(rigaListino.getPrezzo()));
		prezzoDaAggiornare = prezzoDaAggiornare.setScale(rigaListino.getNumeroDecimaliPrezzo(),
				BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param prezzoDaAggiornare
	 *            the prezzoDaAggiornare to set
	 */
	public void setPrezzoDaAggiornare(BigDecimal prezzoDaAggiornare) {
		this.prezzoDaAggiornare = prezzoDaAggiornare;
	}

	/**
	 * 
	 * @param prezzoListino
	 *            prezzo listino
	 */
	public void setPrezzoListino(BigDecimal prezzoListino) {
		rigaListino.setPrezzo(prezzoListino);
	}

	/**
	 * @param rigaListino
	 *            the rigaListino to set
	 */
	public void setRigaListino(RigaListino rigaListino) {
		this.rigaListino = rigaListino;
	}
}