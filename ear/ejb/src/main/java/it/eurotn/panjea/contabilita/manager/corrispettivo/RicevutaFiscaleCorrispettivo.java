/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.corrispettivo;

import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipologiaCorrispettivo;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;

/**
 * Classe che rappresenta la tipologia corrispettivo ricevuta fiscale.
 * 
 * @author Leonardo
 */
public class RicevutaFiscaleCorrispettivo extends AbstractTipologiaCorrispettivo {

	/**
	 * Costruttore.
	 * 
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param codiceAzienda
	 *            codice azienda
	 */
	public RicevutaFiscaleCorrispettivo(final PanjeaDAO panjeaDAO, final String codiceAzienda) {
		super(TipologiaCorrispettivo.RICEVUTA_FISCALE, panjeaDAO, codiceAzienda);
	}

	@Override
	protected void setTotTipoCorrispettivo(TotaliCodiceIvaDTO totaliCodiceIvaDTO, BigDecimal tot) {
		totaliCodiceIvaDTO.setTotRicevutaFiscale(tot);
	}

}
