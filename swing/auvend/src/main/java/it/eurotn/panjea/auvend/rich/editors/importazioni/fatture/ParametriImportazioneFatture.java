package it.eurotn.panjea.auvend.rich.editors.importazioni.fatture;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;

public class ParametriImportazioneFatture implements IDefProperty {

	private LetturaFlussoAuVend letturaFlussoAuVend;

	private StatisticaImportazione statisticaImportazione;

	private Boolean selezionato;

	/**
	 * Costruttore.
	 */
	public ParametriImportazioneFatture() {
		selezionato = false;
	}

	/**
	 * Costruttore.
	 * 
	 * @param letturaFlussoAuVend
	 *            letturaFlussoAuVend
	 */
	public ParametriImportazioneFatture(final LetturaFlussoAuVend letturaFlussoAuVend) {
		super();
		this.letturaFlussoAuVend = letturaFlussoAuVend;
		this.statisticaImportazione = new StatisticaImportazione();
		this.selezionato = false;
	}

	@Override
	public String getDomainClassName() {
		return Deposito.class.getName();
	}

	@Override
	public Integer getId() {
		return getLetturaFlussoAuVend().getDeposito().getId();
	}

	/**
	 * @return the letturaFlussoAuVend
	 */
	public LetturaFlussoAuVend getLetturaFlussoAuVend() {
		return letturaFlussoAuVend;
	}

	/**
	 * @return the selezionato
	 */
	public Boolean getSelezionato() {
		return selezionato;
	}

	/**
	 * @return the statisticaImportazione
	 */
	public StatisticaImportazione getStatisticaImportazione() {
		return statisticaImportazione;
	}

	@Override
	public Integer getVersion() {
		return getLetturaFlussoAuVend().getDeposito().getVersion();
	}

	@Override
	public boolean isNew() {
		return getId() == null;
	}

	/**
	 * @param letturaFlussoAuVend
	 *            the letturaFlussoAuVend to set
	 */
	public void setLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) {
		this.letturaFlussoAuVend = letturaFlussoAuVend;
	}

	/**
	 * @param selezionato
	 *            the selezionato to set
	 */
	public void setSelezionato(Boolean selezionato) {
		this.selezionato = selezionato;
	}

	/**
	 * @param statisticaImportazione
	 *            the statisticaImportazione to set
	 */
	public void setStatisticaImportazione(StatisticaImportazione statisticaImportazione) {
		this.statisticaImportazione = statisticaImportazione;
	}

}