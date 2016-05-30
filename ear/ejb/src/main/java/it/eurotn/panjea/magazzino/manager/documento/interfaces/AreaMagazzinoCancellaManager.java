/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.List;

import javax.ejb.Local;

/* @author Leonardo
 *
 */
@Local
public interface AreaMagazzinoCancellaManager {

	/**
	 * . Cancella l'{@link AreaMagazzino} scelta cancellando eventuali aree collegate.
	 * 
	 * @param areaMagazzino
	 *            area da cancellare
	 * @throws TipoDocumentoBaseException
	 *             sollevatase non esite un tipoDocumentoBasePartite
	 * @throws DocumentiCollegatiPresentiException
	 *             sollevata se esistono documenti collegati
	 * @throws AreeCollegatePresentiException
	 *             sollevata se esistono altre aree per il documento
	 */
	void cancellaAreaMagazzino(AreaMagazzino areaMagazzino) throws DocumentiCollegatiPresentiException,
			TipoDocumentoBaseException, AreeCollegatePresentiException;

	/**
	 * 
	 * @param areaMagazzino
	 *            area da cancellare
	 * @param deleteAreeCollegate
	 *            <code>true</code> se le eventuali aree collegate devono essere cancellate
	 * @throws AreeCollegatePresentiException
	 *             sollevata se esistono altre aree per il documento
	 * @throws TipoDocumentoBaseException
	 *             sollevatase non esite un tipoDocumentoBasePartite
	 * @throws DocumentiCollegatiPresentiException
	 *             sollevata se esistono documenti collegati
	 */
	void cancellaAreaMagazzino(AreaMagazzino areaMagazzino, boolean deleteAreeCollegate)
			throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException;

	/**
	 * 
	 * @param areaMagazzino
	 *            area da cancellare
	 * @param deleteAreeCollegate
	 *            <code>true</code> se le eventuali aree collegate devono essere cancellate
	 * @param forceDeleteAreeCollegate
	 *            foza la cancellazione delle aree collegate
	 * @throws AreeCollegatePresentiException
	 *             sollevata se esistono altre aree per il documento
	 * @throws TipoDocumentoBaseException
	 *             sollevatase non esite un tipoDocumentoBasePartite
	 * @throws DocumentiCollegatiPresentiException
	 *             sollevata se esistono documenti collegati
	 */
	void cancellaAreaMagazzino(AreaMagazzino areaMagazzino, boolean deleteAreeCollegate,
			boolean forceDeleteAreeCollegate) throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException,
			AreeCollegatePresentiException;

	/**
	 * Cancella la {@link RigaMagazzino}.
	 * 
	 * @param rigaMagazzino
	 *            riga da cancellare
	 * @return area magazzino
	 */
	AreaMagazzino cancellaRigaMagazzino(RigaMagazzino rigaMagazzino);

	/**
	 * Cancella le {@link RigaMagazzino} per l'{@link AreaMagazzino} passata come argomento.
	 * 
	 * @param areaMagazzino
	 *            area di riferimento
	 */
	void cancellaRigheMagazzino(AreaMagazzino areaMagazzino);

	/**
	 * Cancella la lista di rigaMagazzino ricevuta, ogni riga verrà cancellata in accordo con il tipo;<br>
	 * vengono inoltre ordinate le righe in modo da cancellare per ultime le righe testata (in modo da non avere righe
	 * collegate che blocchino la cancellazione).
	 * 
	 * @param righeMagazzino
	 *            le righe da eliminare
	 * @return AreaMagazzino
	 */
	AreaMagazzino cancellaRigheMagazzino(List<RigaMagazzino> righeMagazzino);

}
