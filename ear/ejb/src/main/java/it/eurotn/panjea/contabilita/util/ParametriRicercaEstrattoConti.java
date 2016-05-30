package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.contabilita.domain.SottoConto;

import java.util.List;

public class ParametriRicercaEstrattoConti extends ParametriRicercaEstrattoConto {
	private static final long serialVersionUID = 7946327921738822656L;
	private List<SottoConto> sottoConti;
	private String stampante;

	/**
	 * @return Returns the sottoConti.
	 */
	public List<SottoConto> getSottoConti() {
		return sottoConti;
	}

	/**
	 * @return Returns the stampante.
	 */
	public String getStampante() {
		return stampante;
	}

	/**
	 * @param sottoConti
	 *            The sottoConti to set.
	 */
	public void setSottoConti(List<SottoConto> sottoConti) {
		this.sottoConti = sottoConti;
	}

	/**
	 * @param stampante
	 *            The stampante to set.
	 */
	public void setStampante(String stampante) {
		this.stampante = stampante;
	}
}
