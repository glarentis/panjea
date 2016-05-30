package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;

import java.util.Calendar;

import org.springframework.richclient.util.RcpSupport;

public class ParametriContabilizzazioneWrapper {

	private Integer anno;

	private TipoGenerazione tipoGenerazione;

	private Integer annoContabile;

	private AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriContabilizzazioneWrapper() {
		super();
		Calendar calendar = Calendar.getInstance();
		this.anno = calendar.get(Calendar.YEAR);
		this.tipoGenerazione = TipoGenerazione.TUTTI;
		this.annoContabile = aziendaCorrente.getAnnoContabile();
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return the annoContabile
	 */
	public Integer getAnnoContabile() {
		return annoContabile;
	}

	/**
	 * @return the tipoGenerazione
	 */
	public TipoGenerazione getTipoGenerazione() {
		return tipoGenerazione;
	}

	/**
	 * @param anno
	 *            the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param annoContabile
	 *            the annoContabile to set
	 */
	public void setAnnoContabile(Integer annoContabile) {
		this.annoContabile = annoContabile;
	}

	/**
	 * @param tipoGenerazione
	 *            the tipoGenerazione to set
	 */
	public void setTipoGenerazione(TipoGenerazione tipoGenerazione) {
		this.tipoGenerazione = tipoGenerazione;
	}
}
