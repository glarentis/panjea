package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParametriRicercaSomma {
	private Date dataValutaDaAssegnare;
	private BigDecimal sommaDaCercare;
	private Integer combinazioneSelezionataIndex;
	private AreaEffetti areaEffetti;
	private ArrayList<ArrayList<Effetto>> combinazioni;

	/**
	 *
	 * @param areaEffetti
	 *            effetto contenente i pagamenti con gli importi.
	 */
	public ParametriRicercaSomma(final AreaEffetti areaEffetti) {
		super();
		this.areaEffetti = areaEffetti;
		combinazioneSelezionataIndex = -1;
		combinazioni = new ArrayList<ArrayList<Effetto>>();
	}

	/**
	 * Calcola le combinazioni degli effetti.
	 *
	 * @return ritorna una lista con il numero delle combinazioni
	 */
	public List<Integer> calcola() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (sommaDaCercare == null) {
			return result;
		}

		Effetto[] effetti = new Effetto[areaEffetti.getEffetti().size()];
		effetti = areaEffetti.getEffetti().toArray(effetti);
		combinazioni = CombinationEffettiSum.cercaCombinazione(effetti, sommaDaCercare);
		for (int i = 0; i < combinazioni.size(); i++) {
			result.add(i);
		}
		return result;
	}

	/**
	 *
	 * @return lista degli effetti che compongono la combinazione selezionata.
	 */
	public List<Effetto> getCombinazioneSelezionata() {
		if (combinazioni == null || combinazioneSelezionataIndex == null || combinazioneSelezionataIndex < 0
				|| combinazioneSelezionataIndex > combinazioni.size() - 1) {
			return new ArrayList<Effetto>();
		}
		return combinazioni.get(combinazioneSelezionataIndex);
	}

	/**
	 * @return Returns the combinazioneSelezionataIndex.
	 */
	public Integer getCombinazioneSelezionataIndex() {
		return combinazioneSelezionataIndex;
	}

	/**
	 * @return Returns the combinazioni.
	 */
	public ArrayList<ArrayList<Effetto>> getCombinazioni() {
		if (combinazioni == null) {
			return new ArrayList<ArrayList<Effetto>>();
		}
		return combinazioni;
	}

	/**
	 * @return Returns the dataValutaDaAssegnare.
	 */
	public Date getDataValutaDaAssegnare() {
		return dataValutaDaAssegnare;
	}

	/**
	 * @return Returns the sommaDaCercare.
	 */
	public BigDecimal getSommaDaCercare() {
		return sommaDaCercare;
	}

	/**
	 * @param combinazioneSelezionataIndex
	 *            The combinazioneSelezionataIndex to set.
	 */
	public void setCombinazioneSelezionataIndex(Integer combinazioneSelezionataIndex) {
		this.combinazioneSelezionataIndex = combinazioneSelezionataIndex;
	}

	/**
	 * @param dataValutaDaAssegnare
	 *            The dataValutaDaAssegnare to set.
	 */
	public void setDataValutaDaAssegnare(Date dataValutaDaAssegnare) {
		this.dataValutaDaAssegnare = dataValutaDaAssegnare;
	}

	/**
	 * @param sommaDaCercare
	 *            The sommaDaCercare to set.
	 */
	public void setSommaDaCercare(BigDecimal sommaDaCercare) {
		this.sommaDaCercare = sommaDaCercare;
	}

}
