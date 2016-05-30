package it.eurotn.panjea.mrp.domain;

import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.util.ArticoloDepositoConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;

public class RigheOrdinatoCliente {

	private static Logger logger = Logger.getLogger(RigheOrdinatoCliente.class);

	private Set<Integer> configurazioniDistintaUtilizzate;

	private Set<Integer> articoliOrdinati;

	private Map<ArticoloDepositoKey, RigheCalcolo[]> righeOrdinateArticoloDeposito;

	private Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> righeOrdinateArticoloDepositoConfigurazione;

	{
		righeOrdinateArticoloDepositoConfigurazione = new HashMap<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]>();
		righeOrdinateArticoloDeposito = new HashMap<ArticoloDepositoKey, RigheCalcolo[]>();
	}

	/**
	 * Aggiunge una riga ordine con i risultati.
	 *
	 * @param righeOrdiniCliente
	 *            righeOrdiniCliente
	 * @param startDate
	 *            startDate
	 * @param numTime
	 *            numTime
	 */
	public void aggiungiRigheOrdine(List<RisultatoMRPArticoloBucket> righeOrdiniCliente, Date startDate, int numTime) {
		configurazioniDistintaUtilizzate = new HashSet<Integer>();
		articoliOrdinati = new HashSet<>();
		for (RisultatoMRPArticoloBucket risultatoMRPArticoloBucket : righeOrdiniCliente) {

			int bucket = risultatoMRPArticoloBucket.getBucket(startDate) + MrpCalcoloManager.BUCKET_ZERO;

			ArticoloDepositoKey articoloDepositoKey = new ArticoloDepositoKey(
					risultatoMRPArticoloBucket.getIdArticolo(), risultatoMRPArticoloBucket.getIdDeposito());

			ArticoloDepositoConfigurazioneKey configurazioneKey = new ArticoloDepositoConfigurazioneKey(
					risultatoMRPArticoloBucket.getIdArticolo(), risultatoMRPArticoloBucket.getIdDeposito(),
					risultatoMRPArticoloBucket.getIdConfigurazioneDistinta());

			// Gestisco le righe calcolo per la configurazione
			RigheCalcolo[] righeCalcoloConfigurazione = righeOrdinateArticoloDepositoConfigurazione
					.get(configurazioneKey);
			righeCalcoloConfigurazione = ObjectUtils.defaultIfNull(righeCalcoloConfigurazione,
					new RigheCalcolo[numTime]);
			RigheCalcolo rigaCalcoloConfigurazioneGiorno = ObjectUtils.defaultIfNull(
					righeCalcoloConfigurazione[bucket], new RigheCalcolo());

			rigaCalcoloConfigurazioneGiorno.addRisultatiMRPArticoloBucket(
					risultatoMRPArticoloBucket.getIdConfigurazioneDistinta(),
					risultatoMRPArticoloBucket.getIdArticolo(), risultatoMRPArticoloBucket);

			righeCalcoloConfigurazione[bucket] = rigaCalcoloConfigurazioneGiorno;
			righeOrdinateArticoloDepositoConfigurazione.put(configurazioneKey, righeCalcoloConfigurazione);

			// Gestisco le righe calcolo per l'articolo
			RigheCalcolo[] righeCalcoloArticoloDeposito = righeOrdinateArticoloDeposito.get(articoloDepositoKey);
			righeCalcoloArticoloDeposito = ObjectUtils.defaultIfNull(righeCalcoloArticoloDeposito,
					new RigheCalcolo[numTime]);
			RigheCalcolo rigaCalcoloArticoloDepositoGiorno = ObjectUtils.defaultIfNull(
					righeCalcoloArticoloDeposito[bucket], new RigheCalcolo());

			rigaCalcoloArticoloDepositoGiorno.addRisultatiMRPArticoloBucket(
					risultatoMRPArticoloBucket.getIdConfigurazioneDistinta(),
					risultatoMRPArticoloBucket.getIdArticolo(), risultatoMRPArticoloBucket);

			righeCalcoloArticoloDeposito[bucket] = rigaCalcoloArticoloDepositoGiorno;
			righeOrdinateArticoloDeposito.put(articoloDepositoKey, righeCalcoloArticoloDeposito);

			if (risultatoMRPArticoloBucket.getIdConfigurazioneDistinta() != null) {
				// ArticoloDepositoKey keyArticoloDepositoKey = new ArticoloDepositoKey(
				// risultatoMRPArticoloBucket.getIdArticolo(), risultatoMRPArticoloBucket.getIdDeposito());

				// Set<Integer> confUtilizzate = ObjectUtils.defaultIfNull(
				// configurazioniDistintaUtilizzate.get(keyArticoloDepositoKey), new HashSet<Integer>());
				// confUtilizzate.add(risultatoMRPArticoloBucket.getIdConfigurazioneDistinta());
				// configurazioniDistintaUtilizzate.put(keyArticoloDepositoKey, confUtilizzate);
				configurazioniDistintaUtilizzate.add(risultatoMRPArticoloBucket.getIdConfigurazioneDistinta());

			} else {
				articoliOrdinati.add(risultatoMRPArticoloBucket.getIdArticolo());
			}
		}
		configurazioniDistintaUtilizzate.add(null);
	}

	/**
	 * @return Returns the articoliUtilizzati.
	 */
	public Set<Integer> getArticoliOrdinati() {
		return articoliOrdinati;
	}

	/**
	 * @return Returns the configurazioniDistintaUtilizzate.
	 */
	public Set<Integer> getConfigurazioniDistintaUtilizzate() {
		return configurazioniDistintaUtilizzate;
	}

	/**
	 * @return Returns the righeOrdinateArticoloDeposito.
	 */
	public Map<ArticoloDepositoKey, RigheCalcolo[]> getRigheOrdinateArticoloDeposito() {
		return righeOrdinateArticoloDeposito;
	}

	/**
	 * @return Returns the righeOrdinateArticoloDepositoConfigurazione.
	 */
	public Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> getRigheOrdinateArticoloDepositoConfigurazione() {
		return righeOrdinateArticoloDepositoConfigurazione;
	}

	/**
	 * @param righeOrdinateArticoloDeposito
	 *            The righeOrdinateArticoloDeposito to set.
	 */
	public void setRigheOrdinateArticoloDeposito(Map<ArticoloDepositoKey, RigheCalcolo[]> righeOrdinateArticoloDeposito) {
		this.righeOrdinateArticoloDeposito = righeOrdinateArticoloDeposito;
	}

	/**
	 * @param righeOrdinateArticoloDepositoConfigurazione
	 *            The righeOrdinateArticoloDepositoConfigurazione to set.
	 */
	public void setRigheOrdinateArticoloDepositoConfigurazione(
			Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> righeOrdinateArticoloDepositoConfigurazione) {
		this.righeOrdinateArticoloDepositoConfigurazione = righeOrdinateArticoloDepositoConfigurazione;
	}

}
