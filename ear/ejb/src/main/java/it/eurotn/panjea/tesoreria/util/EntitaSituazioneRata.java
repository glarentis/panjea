package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EntitaSituazioneRata implements Serializable {

	private static final long serialVersionUID = -3797244113085065775L;

	private EntitaDocumento entita;

	private List<SituazioneRata> situazioneRate;

	private String zona;

	private TipoPagamento tipoPagamento;

	private BigDecimal totale;
	private BigDecimal totalePagato;

	{
		situazioneRate = new ArrayList<SituazioneRata>();

		totale = BigDecimal.ZERO;
		totalePagato = BigDecimal.ZERO;
	}

	/**
	 * @return Returns the entita.
	 */
	public EntitaDocumento getEntita() {
		return entita;
	}

	/**
	 * @return Returns the situazioneRate.
	 */
	public List<SituazioneRata> getSituazioneRate() {
		return situazioneRate;
	}

	/**
	 * @return Returns the tipoPagamento.
	 */
	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	/**
	 * @return Returns the totaleResiduo.
	 */
	public BigDecimal getTotale() {
		return totale;
	}

	/**
	 * @return Returns the totalePagato.
	 */
	public BigDecimal getTotalePagato() {
		return totalePagato;
	}

	/**
	 * @return Returns the totaleProgressivo.
	 */
	public BigDecimal getTotaleProgressivo() {
		return totale.subtract(totalePagato);
	}

	/**
	 * @return Returns the zona.
	 */
	public String getZona() {
		return zona;
	}

	private void initTotali() {
		setTotale(BigDecimal.ZERO);
		setTotalePagato(BigDecimal.ZERO);
		for (SituazioneRata rata : situazioneRate) {
			// totali
			BigDecimal residuo = rata.getRata().getResiduo().getImportoInValutaAzienda();
			BigDecimal totaleR = (residuo.signum() >= 0) ? rata.getDocumento().getTotale().getImportoInValutaAzienda()
					: rata.getDocumento().getTotale().getImportoInValutaAzienda().negate();
			setTotale(getTotale().add(totaleR));
			setTotalePagato(getTotalePagato().add(rata.getRata().getTotalePagato().getImportoInValutaAzienda()));
		}
	}

	/**
	 * @param entita
	 *            The entita to set.
	 */
	public void setEntita(EntitaDocumento entita) {
		this.entita = entita;
	}

	/**
	 * @param situazioneRate
	 *            The situazioneRate to set.
	 */
	public void setSituazioneRate(List<SituazioneRata> situazioneRate) {
		this.situazioneRate = situazioneRate;
		initTotali();
	}

	/**
	 * @param tipoPagamento
	 *            The tipoPagamento to set.
	 */
	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	/**
	 * @param totale
	 *            The totale to set.
	 */
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}

	/**
	 * @param totalePagato
	 *            The totalePagato to set.
	 */
	public void setTotalePagato(BigDecimal totalePagato) {
		this.totalePagato = totalePagato;
	}

	/**
	 * @param zona
	 *            The zona to set.
	 */
	public void setZona(String zona) {
		this.zona = zona;
	}

}
