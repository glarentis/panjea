package it.eurotn.panjea.preventivi.rich.bd;

import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;

import java.util.Set;

public interface IRigheBD<E extends IAreaDocumentoTestata> {

	/**
	 * 
	 * @param righePreventivoDaCambiare
	 *            righe da collegare.
	 */
	void collegaTestata(Set<Integer> righePreventivoDaCambiare);

	/**
	 * 
	 * @param areaDocumento
	 *            areaDocumento
	 * @param note
	 *            note
	 * @return <code>true</code> se la riga viene generata e salvata correttamente
	 */
	boolean creaRigaNoteAutomatica(E areaDocumento, String note);

	/**
	 * 
	 * @param righeDaSpostare
	 *            righeDaSpostare
	 * @param rowDest
	 *            riga sotto cui le righe vanno posizionate
	 */
	void spostaRighe(Set<Integer> righeDaSpostare, Integer rowDest);
}
