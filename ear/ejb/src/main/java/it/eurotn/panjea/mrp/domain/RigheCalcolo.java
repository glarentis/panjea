package it.eurotn.panjea.mrp.domain;

import it.eurotn.panjea.mrp.util.ArticoloDepositoConfigurazioneKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Wrappa una lista di risultatiMRPArticoloBucket. Utilizzato perchè non posso
 * avere un array di List tipizzato.
 *
 * @author giangi
 * @version 1.0, 22/gen/2014
 *
 */
public class RigheCalcolo {
	private Map<ArticoloDepositoConfigurazioneKey, List<RisultatoMRPArticoloBucket>> risultatiPerConfigurazione = new HashMap<>();

	/**
	 *
	 * @param idConfigurazioneDistinta
	 *            conf. distinta
	 * @param risultatoMRPArticoloBucket
	 *            risultato da aggiungere
	 */
	public void addRisultatiMRPArticoloBucket(Integer idConfigurazioneDistinta, Integer idComponente,
			RisultatoMRPArticoloBucket risultatoMRPArticoloBucket) {

		ArticoloDepositoConfigurazioneKey keyConfigurazione = new ArticoloDepositoConfigurazioneKey(idComponente,
				risultatoMRPArticoloBucket.getIdDeposito(), idConfigurazioneDistinta);

		List<RisultatoMRPArticoloBucket> risultatiBucketConfigurazione = risultatiPerConfigurazione
				.get(keyConfigurazione);
		if (risultatiBucketConfigurazione == null) {
			risultatiBucketConfigurazione = new ArrayList<RisultatoMRPArticoloBucket>();
		}
		risultatiBucketConfigurazione.add(risultatoMRPArticoloBucket);
		risultatiPerConfigurazione.put(keyConfigurazione, risultatiBucketConfigurazione);
	}

	public void addRisultatoMRPArticoloBucket(ArticoloDepositoConfigurazioneKey key,
			RisultatoMRPArticoloBucket risultatoMRPArticoloBucket) {
		List<RisultatoMRPArticoloBucket> righeBucket = this.risultatiPerConfigurazione.get(key);
		if (righeBucket == null) {
			righeBucket = new ArrayList<RisultatoMRPArticoloBucket>();
		}
		righeBucket.add(risultatoMRPArticoloBucket);
		this.risultatiPerConfigurazione.put(key, righeBucket);
	}

	/**
	 * @return Returns the risultatiMRPArticoloBucket.
	 */
	public List<RisultatoMRPArticoloBucket> getRisultatiMRPArticoloBucket() {
		List<RisultatoMRPArticoloBucket> result = new ArrayList<RisultatoMRPArticoloBucket>(50);
		for (List<RisultatoMRPArticoloBucket> risultatiMRPArticoloBucket : risultatiPerConfigurazione.values()) {
			result.addAll(risultatiMRPArticoloBucket);
		}
		Collections.sort(result, new Comparator<RisultatoMRPArticoloBucket>() {

			@Override
			public int compare(RisultatoMRPArticoloBucket o1, RisultatoMRPArticoloBucket o2) {
				int result = Integer.compare(o1.getTipoRiga(), o2.getTipoRiga()) * -1;
				return result;
			}
		});
		return Collections.unmodifiableList(result);
	}

	/**
	 * @param idConfigurazioneDistinta
	 *            configurazione associata alla riga
	 * @return Returns the risultatiMRPArticoloBucket.
	 */
	public List<RisultatoMRPArticoloBucket> getRisultatiMRPArticoloBucket(ArticoloDepositoConfigurazioneKey key) {
		List<RisultatoMRPArticoloBucket> risultato = risultatiPerConfigurazione.get(key);
		if (risultato == null) {
			risultato = new ArrayList<>();
		}
		return Collections.unmodifiableList(risultato);
	}

	/**
	 * @return Returns the risultatiPerConfigurazione.
	 */
	public Map<ArticoloDepositoConfigurazioneKey, List<RisultatoMRPArticoloBucket>> getRisultatiPerConfigurazione() {
		return risultatiPerConfigurazione;
	}

	/**
	 * @param idConfigurazioneDistinta
	 *            configurazione associata alla riga
	 * @param risultatiMRPArticoloBucket
	 *            The risultatiMRPArticoloBucket to set.
	 */
	public void setRisultatiMRPArticoloBucket(ArticoloDepositoConfigurazioneKey key,
			List<RisultatoMRPArticoloBucket> risultatiMRPArticoloBucket) {
		this.risultatiPerConfigurazione.put(key, risultatiMRPArticoloBucket);
	}
}
