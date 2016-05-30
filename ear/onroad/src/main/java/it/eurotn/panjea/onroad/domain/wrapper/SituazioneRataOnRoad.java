package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;

public class SituazioneRataOnRoad extends SituazioneRata {

	private static final long serialVersionUID = -8605789707543919865L;

	/**
	 * Costruttore.
	 * 
	 * @param situazioneRata
	 *            situazioneRata.
	 */
	public SituazioneRataOnRoad(final SituazioneRata situazioneRata) {
		PanjeaEJBUtil.copyProperties(this, situazioneRata);
	}

	/**
	 * @return codice destinatario o se non presente codice entità
	 */
	public String getCodiceDestinatario() {
		String codiceSedeEntita = getDocumento().getSedeEntita().getCodice();
		String codiceEntita = getEntita().getCodice() + "";
		if (codiceSedeEntita != null && !codiceSedeEntita.isEmpty() && codiceSedeEntita.compareTo("1") != 0) {
			return codiceSedeEntita;
		}
		return codiceEntita;
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
	 * @return FT (fattura generica) o ACR (nota di credito generica)
	 */
	public String getTipoDocumento() {
		if (getDocumento().getTipoDocumento().isRigheIvaEnable()
				&& (getDocumento().getTipoDocumento().isNotaCreditoEnable() || getDocumento().getTipoDocumento()
						.isNotaAddebitoEnable())) {
			return "ACR";
		} else {
			return "FT";
		}
	}

	public void setCodiceDestinatario(String codiceDestinatario) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Non utilizzato.
	 * 
	 * @param dare
	 *            .
	 */
	public void setDare(int dare) {

	}

	/**
	 * Non utilizzato.
	 * 
	 * @param importo
	 *            .
	 */
	public void setImporto(BigDecimal importo) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param residuo
	 *            .
	 */
	public void setResiduo(BigDecimal residuo) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		throw new UnsupportedOperationException();
	}
}
