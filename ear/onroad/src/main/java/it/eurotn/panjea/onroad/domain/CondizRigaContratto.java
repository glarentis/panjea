package it.eurotn.panjea.onroad.domain;

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
	 *            dati della riga del contratto da esportare
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
	public BigDecimal getSconto1() {
		BigDecimal sconto1 = BigDecimal.ZERO;
		double quantitaSogliaPrezzo = rigaContratto.getStrategiaPrezzo().getQuantitaSogliaPrezzo();
		RisultatoPrezzo<Sconto> risultatoSconto = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti()
				.getRisultatoPrezzo(quantitaSogliaPrezzo);
		if (risultatoSconto != null) {
			Sconto sconto = risultatoSconto.getValue();
			if (sconto != null) {
				sconto1 = sconto.getSconto1().abs();
			}
		}
		return sconto1;
	}

	@Override
	public BigDecimal getSconto2() {
		BigDecimal sconto2 = BigDecimal.ZERO;
		double quantitaSogliaPrezzo = rigaContratto.getStrategiaPrezzo().getQuantitaSogliaPrezzo();
		RisultatoPrezzo<Sconto> risultatoSconto = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti()
				.getRisultatoPrezzo(quantitaSogliaPrezzo);
		if (risultatoSconto != null) {
			Sconto sconto = risultatoSconto.getValue();
			if (sconto != null) {
				sconto2 = sconto.getSconto2().abs();
			}
		}
		return sconto2;
	}

	@Override
	public BigDecimal getSconto3() {
		BigDecimal sconto3 = BigDecimal.ZERO;
		double quantitaSogliaPrezzo = rigaContratto.getStrategiaPrezzo().getQuantitaSogliaPrezzo();
		RisultatoPrezzo<Sconto> risultatoSconto = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti()
				.getRisultatoPrezzo(quantitaSogliaPrezzo);
		if (risultatoSconto != null) {
			Sconto sconto = risultatoSconto.getValue();
			if (sconto != null) {
				sconto3 = sconto.getSconto3().abs();
			}
		}
		return sconto3;
	}

	@Override
	public BigDecimal getSconto4() {
		BigDecimal sconto4 = BigDecimal.ZERO;
		double quantitaSogliaPrezzo = rigaContratto.getStrategiaPrezzo().getQuantitaSogliaPrezzo();
		RisultatoPrezzo<Sconto> risultatoSconto = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti()
				.getRisultatoPrezzo(quantitaSogliaPrezzo);
		if (risultatoSconto != null) {
			Sconto sconto = risultatoSconto.getValue();
			if (sconto != null) {
				sconto4 = sconto.getSconto4().abs();
			}
		}
		return sconto4;
	}

}
