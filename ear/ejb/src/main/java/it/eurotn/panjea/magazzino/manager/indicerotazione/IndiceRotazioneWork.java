package it.eurotn.panjea.magazzino.manager.indicerotazione;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoMovimentazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import commonj.work.Work;

public class IndiceRotazioneWork implements Work {

	private ArticoloLite articolo;
	private DepositoLite deposito;
	private Periodo periodo;
	private IndiceGiacenzaArticolo indice;
	private MagazzinoMovimentazioneManager magazzinoMovimentazioneManager;
	private double giacenza;
	private ArticoloManager articoloManager;

	/**
	 *
	 * @param articolo
	 *            articolo
	 * @param deposito
	 *            deposito
	 * @param periodo
	 *            periodo
	 * @param giacenza
	 *            giacenza
	 * @param magazzinoMovimentazioneManager
	 *            manager movimentazione
	 */
	public IndiceRotazioneWork(
			final ArticoloLite articolo,
			final DepositoLite deposito,
			final Periodo periodo,
			final double giacenza,
			final MagazzinoMovimentazioneManager magazzinoMovimentazioneManager,
			final ArticoloManager articoloManager) {
		super();
		this.articolo = articolo;
		this.deposito = deposito;
		this.periodo = periodo;
		this.giacenza = giacenza;
		this.magazzinoMovimentazioneManager = magazzinoMovimentazioneManager;
		this.articoloManager = articoloManager;
	}

	/**
	 * @return Returns the indice.
	 */
	public IndiceGiacenzaArticolo getIndice() {
		return indice;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {

	}

	@Override
	public void run() {
		List<Object[]> movimentazione = magazzinoMovimentazioneManager
				.caricaMovimentazionePerIndiciRotazione(articolo, deposito,
						periodo);

		Date data = DateUtils.addDays(periodo.getDataIniziale(), -1);
		int giorni = 0;
		double scortaGiorni = 0;
		double scorta = giacenza;
		double scarichi = 0;

		for (Object[] movimento : movimentazione) {
			BigInteger tipo = (BigInteger) movimento[0];
			Date dataMovimento = (Date) movimento[1];
			double qtaCarico = (double) movimento[2];
			double qtaScarico = (double) movimento[3];

			giorni = PanjeaEJBUtil.calculateDifference(data, dataMovimento);
			scortaGiorni += (giorni * scorta);

			scarichi += qtaScarico;

			if (BigInteger.ZERO.equals(tipo)) {
				scorta = qtaCarico;
			} else {
				scorta = scorta + qtaCarico - qtaScarico;
			}

			data = dataMovimento;
		}
		// giorni rimanenti
		int giorniFinePeriodo = PanjeaEJBUtil.calculateDifference(data,
				periodo.getDataFinale());
		scortaGiorni += (giorniFinePeriodo * scorta);
		giorni += giorniFinePeriodo;
		double scortaMedia = scortaGiorni
				/ (PanjeaEJBUtil.calculateDifference(periodo.getDataIniziale(),
						periodo.getDataFinale()) + 1);
		double indiceRotazione = 0;
		if (scortaMedia > 0) {
			indiceRotazione = scarichi / scortaMedia;
		}
		articolo = articoloManager.caricaArticoloLite(articolo.getId());
		indice = new IndiceGiacenzaArticolo(scortaMedia, indiceRotazione,
				articolo, deposito);
	}
}
