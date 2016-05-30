/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.corrispettivo;

import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipologiaCorrispettivo;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;

/**
 * Classe che rappresenta la tipologia corrispettivo da fattura.
 * 
 * @author Leonardo
 */
public class DaFatturaCorrispettivo extends AbstractTipologiaCorrispettivo {

	/**
	 * Costruttore.
	 * 
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param codiceAzienda
	 *            codice azienda
	 */
	public DaFatturaCorrispettivo(final PanjeaDAO panjeaDAO, final String codiceAzienda) {
		super(TipologiaCorrispettivo.DA_FATTURA, panjeaDAO, codiceAzienda);
	}

	@Override
	protected void setTotTipoCorrispettivo(TotaliCodiceIvaDTO totaliCodiceIvaDTO, BigDecimal tot) {
		totaliCodiceIvaDTO.setTotDaFattura(tot);
	}

}
