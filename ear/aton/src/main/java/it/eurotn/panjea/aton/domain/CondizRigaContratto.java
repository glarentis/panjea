package it.eurotn.panjea.aton.domain;

import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;

import java.math.BigDecimal;
import java.util.Date;

public class CondizRigaContratto extends CondizRiga {

	private RigaContratto rigaContratto;
	private DatiEsportazioneContratti datiEsportazioneContratti;
	private ParametriCalcoloPrezzi parametriCalcoloPrezzi;

	/**
	 * Costruttore.
	 * 
	 * @param rigaContratto
	 *            riga del contratto da esportare
	 * @param datiEsportazioneContratti
	 *            dati delal riga del contratto da esportare
	 * @param parametriCalcoloPrezzi
	 *            parametri calcolo con i dati della riga riguardo ai prezzi
	 */
	public CondizRigaContratto(final RigaContratto rigaContratto, final ParametriCalcoloPrezzi parametriCalcoloPrezzi,
			final DatiEsportazioneContratti datiEsportazioneContratti) {
		super();
		this.rigaContratto = rigaContratto;
		this.parametriCalcoloPrezzi = parametriCalcoloPrezzi;
		this.datiEsportazioneContratti = datiEsportazioneContratti;
	}

	@Override
	public String getChiave() {
		return datiEsportazioneContratti.getCodiceEsportazione();
	}

	@Override
	public String getCodiceRicerca() {
		return datiEsportazioneContratti.getTipoRecord();
	}

	@Override
	public String getCodiceSconto() {
		String codiceSconto = "    ";
		double quantitaSogliaPrezzo = rigaContratto.getStrategiaPrezzo().getQuantitaSogliaPrezzo();
		RisultatoPrezzo<Sconto> risultatoSconto = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti()
				.getRisultatoPrezzo(quantitaSogliaPrezzo);
		if (risultatoSconto != null) {
			Sconto sconto = risultatoSconto.getValue();
			if (sconto != null) {
				codiceSconto = sconto.getCodice();
			}
		}
		return codiceSconto;
	}

	@Override
	public Date getDataFine() {
		return rigaContratto.getContratto().getDataFine();
	}

	@Override
	public Date getDataInizio() {
		return rigaContratto.getContratto().getDataInizio();
	}

	@Override
	public BigDecimal getPrezzo() {
		BigDecimal prezzo = BigDecimal.ZERO;
		double quantitaSogliaPrezzo = rigaContratto.getStrategiaPrezzo().getQuantitaSogliaPrezzo();
		PoliticaPrezzo politicaPrezzo = parametriCalcoloPrezzi.getPoliticaPrezzo();
		if (politicaPrezzo != null) {
			RisultatoPrezzo<BigDecimal> risultatoPrezzo = politicaPrezzo.getPrezzi().getRisultatoPrezzo(
					quantitaSogliaPrezzo);
			if (risultatoPrezzo != null) {
				prezzo = risultatoPrezzo.getValue();
			}
		}
		return prezzo;
	}

	@Override
	public BigDecimal getScontoPercentuale() {
		BigDecimal scontoPercentuale = BigDecimal.ZERO;
		double quantitaSogliaPrezzo = rigaContratto.getStrategiaPrezzo().getQuantitaSogliaPrezzo();
		RisultatoPrezzo<Sconto> risultatoSconto = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti()
				.getRisultatoPrezzo(quantitaSogliaPrezzo);
		if (risultatoSconto != null) {
			Sconto sconto = risultatoSconto.getValue();
			if (sconto != null) {
				scontoPercentuale = sconto.getSconto1().abs();
			}
		}
		return scontoPercentuale;
	}

}
