/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.corrispettivo;

import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipologiaCorrispettivo;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;

/**
 * Classe che rappresenta la tipologia corrispettivo aliquota nota.
 * 
 * @author Leonardo
 */
public class AliquotaNotaCorrispettivo extends AbstractTipologiaCorrispettivo {

	/**
	 * Costruttore.
	 * 
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param codiceAzienda
	 *            codice azienda
	 */
	public AliquotaNotaCorrispettivo(final PanjeaDAO panjeaDAO, final String codiceAzienda) {
		super(TipologiaCorrispettivo.ALIQUOTA_NOTA, panjeaDAO, codiceAzienda);
	}

	@Override
	protected void setTotTipoCorrispettivo(TotaliCodiceIvaDTO totaliCodiceIvaDTO, BigDecimal tot) {
		totaliCodiceIvaDTO.setTotAliquotaNota(tot);
	}

}
