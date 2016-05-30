package it.eurotn.panjea.intra.manager.esportazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.RigaSezione4Intra;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.DiscriminatorValue;

/**
 * Utilizzata solamente in fase di esportazione per selezionare il record corretto all'interno dello streaming di
 * BeanIO.
 *
 * @author giangi
 * @version 1.0, 12/apr/2013
 *
 */
@DiscriminatorValue("4")
public class RigaSezione4IntraVendite extends RigaSezione4Intra {
	private static final long serialVersionUID = -201001381153326690L;

	/**
	 *
	 * Costruttore.
	 *
	 * @param prototypeObject
	 *            RigaSezione2 legata ad una dichiarazione intra di Vendita (cessione)
	 */
	public RigaSezione4IntraVendite(final RigaSezione4Intra prototypeObject) {
		if (prototypeObject.getDichiarazione().getTipoDichiarazione() != TipoDichiarazione.VENDITE) {
			throw new IllegalArgumentException("Solamente righe cessioni");
		}
		PanjeaEJBUtil.copyProperties(this, prototypeObject);
	}
}
