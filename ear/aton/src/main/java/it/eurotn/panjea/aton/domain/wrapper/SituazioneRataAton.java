package it.eurotn.panjea.aton.domain.wrapper;

import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;

public class SituazioneRataAton extends SituazioneRata {
	private static final long serialVersionUID = -8605789707543919865L;

	/**
	 * Costruttore.
	 * 
	 * @param situazioneRata
	 *            situazioneRata.
	 */
	public SituazioneRataAton(final SituazioneRata situazioneRata) {
		PanjeaEJBUtil.copyProperties(this, situazioneRata);
	}

	/**
	 * @return 0=Dare 1=Avere
	 */
	public int getDare() {
		return getRata().getResiduo().getImportoInValuta().compareTo(BigDecimal.ZERO) >= 0 ? 0 : 1;
	}

	/**
	 * @return abs del totale della rata.
	 */
	public BigDecimal getImporto() {
		return getRata().getImporto().getImportoInValuta();
	}

	/**
	 * @return abs residuo rata
	 */
	public BigDecimal getResiduo() {
		return getRata().getResiduo().getImportoInValuta();
	}

	/**
	 * Non utilizzato. Solo
	 * 
	 * @param dare
	 *            dare
	 */
	public void setDare(int dare) {

	}

	/**
	 * Non utilizzato.
	 * 
	 * @param importo
	 *            importo
	 */
	public void setImporto(BigDecimal importo) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param residuo
	 *            residuo
	 */
	public void setResiduo(BigDecimal residuo) {
		throw new UnsupportedOperationException();
	}
}
