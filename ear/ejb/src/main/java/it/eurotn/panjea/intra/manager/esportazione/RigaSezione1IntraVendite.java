package it.eurotn.panjea.intra.manager.esportazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
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
@DiscriminatorValue("1")
public class RigaSezione1IntraVendite extends RigaSezione1Intra {
	private static final long serialVersionUID = -201001381153326690L;

	/**
	 *
	 * Costruttore.
	 *
	 * @param prototypeObject
	 *            rigaSezione1 legata ad una dichiarazione intra di Vendita (cessione)
	 */
	public RigaSezione1IntraVendite(final RigaSezione1Intra prototypeObject) {
		if (prototypeObject.getDichiarazione().getTipoDichiarazione() != TipoDichiarazione.VENDITE) {
			throw new IllegalArgumentException("Solamente righe cessioni");
		}
		PanjeaEJBUtil.copyProperties(this, prototypeObject);
	}
}
