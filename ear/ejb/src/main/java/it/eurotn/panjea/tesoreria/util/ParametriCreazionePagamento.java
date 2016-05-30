package it.eurotn.panjea.tesoreria.util;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Raccoglie i parametri per il pagamento di una rata. <b>NB</b>Implementa {@link IDefProperty} solamente per poter
 * essere gestita dal formBacked
 * 
 * @author giangi
 * @version 1.0, 28/mag/2010
 */
public class ParametriCreazionePagamento extends ParametriCreazioneAreaChiusure implements IDefProperty {
	private static final long serialVersionUID = -1133534058473596122L;

	private EntitaLite entita;

	private Pagamento pagamento;

	{
		pagamento = new Pagamento();
	}

	/**
	 * Costruttore.
	 */
	public ParametriCreazionePagamento() {
	}

	/**
	 * Costruttore di default.
	 * 
	 * @param rata
	 *            rata da pagare
	 */
	public ParametriCreazionePagamento(final Rata rata) {
		setDataDocumento(Calendar.getInstance().getTime());
		setTipoPartita(rata.getAreaRate().getTipoAreaPartita().getTipoPartita());
		setEntita(rata.getAreaRate().getDocumento().getEntita());

		pagamento = new Pagamento();
		pagamento.setDataCreazione(getDataDocumento());
		pagamento.setDataPagamento(getDataDocumento());
		pagamento.setImporto(rata.getResiduo().clone());
		pagamento.setRata(rata);

		setTipoAreaPartita(rata.getAreaRate().getCodicePagamento().getTipoAreaPartitaPredefinitaPerPagamenti());

		Importo importo = rata.getImporto().clone();
		importo.setImportoInValuta(BigDecimal.ZERO);
		importo.calcolaImportoValutaAzienda(2);
		pagamento.setImportoForzato(importo);
	}

	@Override
	public String getDomainClassName() {
		return this.getClass().getName();
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	@Override
	public Integer getId() {
		return null;
	}

	/**
	 * @return pagamento creato.
	 */
	public Pagamento getPagamento() {
		return pagamento;
	}

	@Override
	public Integer getVersion() {
		return null;
	}

	@Override
	public boolean isNew() {
		return getId() == null;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}
}
