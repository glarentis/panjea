package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.util.RegistroLiquidazioneDTO;

/**
 * Utilizzata per la creazione della tabella Liqidazioni Contiene il periodo ed un eventuale documento di liquidazione
 * per quel periodo.
 * 
 * @author giangi
 * 
 */
public class LiquidazionePM {
	public enum StatoGiornale {
		NON_STAMPATO, VALIDO
	}

	private RegistroLiquidazioneDTO registroLiquidazioneDTO;
	private String periodo; // Stringa per il periodo..mese oppure trimestre

	/**
	 * Costruttore.
	 * 
	 */
	public LiquidazionePM() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param periodo
	 *            periodo
	 * @param registroLiquidazioneDTO
	 *            registro liquidazione
	 */
	public LiquidazionePM(final String periodo, final RegistroLiquidazioneDTO registroLiquidazioneDTO) {
		super();
		if (registroLiquidazioneDTO != null) {
			this.registroLiquidazioneDTO = registroLiquidazioneDTO;
		} else {
			this.registroLiquidazioneDTO = new RegistroLiquidazioneDTO();
		}
		this.periodo = periodo;
	}

	/**
	 * @return the periodo
	 */
	public String getPeriodo() {
		return periodo;
	}

	/**
	 * @return the registroLiquidazioneDTO
	 */
	public RegistroLiquidazioneDTO getRegistroLiquidazioneDTO() {
		return registroLiquidazioneDTO;

	}

	/**
	 * @return stato giornale
	 */
	public StatoGiornale getStatoGiornale() {
		StatoGiornale stato = null;
		if (this.getRegistroLiquidazioneDTO().getGiornaleIva().isNew()) {
			stato = StatoGiornale.NON_STAMPATO;
		} else {
			stato = StatoGiornale.VALIDO;
		}
		return stato;
	}
}
