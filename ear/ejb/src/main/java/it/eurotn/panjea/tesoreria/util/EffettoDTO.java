package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.io.Serializable;
import java.util.Date;

public class EffettoDTO implements Serializable {

	private static final long serialVersionUID = -1884223998806901418L;

	private Effetto effetto;

	private RapportoBancarioAzienda rapportoBancarioAzienda;

	private RapportoBancarioSedeEntita rapportoBancarioSedeEntita;

	private Date dataScadenza;

	/**
	 * Costruttore.
	 * 
	 * @param effetto
	 *            effetto
	 * @param rapportoBancarioAzienda
	 *            rapporto bancario dell'azienda
	 * @param rapportoBancarioSedeEntita
	 *            rapporto bancario della sede entità
	 */
	public EffettoDTO(final Effetto effetto, final RapportoBancarioAzienda rapportoBancarioAzienda,
			final RapportoBancarioSedeEntita rapportoBancarioSedeEntita) {
		super();
		this.effetto = effetto;
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
		this.rapportoBancarioSedeEntita = rapportoBancarioSedeEntita;
		this.dataScadenza = effetto.getDataScadenza();
	}

	/**
	 * @return Returns the dataScadenza.
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return Returns the effetto.
	 */
	public Effetto getEffetto() {
		return effetto;
	}

	/**
	 * @return Returns the rapportoBancarioAzienda.
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return Returns the rapportoBancarioSedeEntita.
	 */
	public RapportoBancarioSedeEntita getRapportoBancarioSedeEntita() {
		return rapportoBancarioSedeEntita;
	}

}
