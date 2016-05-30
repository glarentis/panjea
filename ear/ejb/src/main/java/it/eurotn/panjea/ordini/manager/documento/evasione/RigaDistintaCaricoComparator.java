package it.eurotn.panjea.ordini.manager.documento.evasione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;

import java.util.Comparator;

/**
 * Compara le righe distinta di carico attravero il suo crc che comprende varie proprietà della riga.
 * 
 * @author giangi
 * @version 1.0, 21/feb/2011
 * 
 */
public class RigaDistintaCaricoComparator implements Comparator<RigaDistintaCarico> {

	@Override
	public int compare(RigaDistintaCarico riga1, RigaDistintaCarico riga2) {

		if (riga1.getChecksum() > riga2.getChecksum()) {
			return 1;
		} else if (riga1.getChecksum() < riga2.getChecksum()) {
			return -1;
		} else {
			return 0;
		}
	}

}