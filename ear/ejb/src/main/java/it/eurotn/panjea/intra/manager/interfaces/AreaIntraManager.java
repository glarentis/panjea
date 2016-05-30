/**
 *
 */
package it.eurotn.panjea.intra.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;

import java.util.List;

import javax.ejb.Local;

/**
 * @author leonardo
 */
@Local
public interface AreaIntraManager {

	/**
	 * L'area intra a seconda che sia acquisto o cessione, avvalora i dati "geografici" in modo diverso. Provincia,
	 * paese, provenienza e paese pagamento sono i dati da avvalorare.
	 * 
	 * @param areaIntra
	 *            l'area intra di cui avvalorare i dati che fanno riferimento all'origine/destinazione merci, al paese e
	 *            provincia dell'azienda e del cliente/fornitore a seconda che sia un acquisto o vendita
	 * @param documento
	 *            il documento dell'area intra con i dati relativi al cliente/fornitore e quindi i dati relativi
	 *            all'origine/destinazione di merci o pagamenti
	 * @param depositoOrigine
	 *            il deposito di origine con i dati relativi all'origine/destinazione delle merci
	 * @return AreaIntra
	 */
	AreaIntra avvaloraDatiGeograficiAreaIntra(AreaIntra areaIntra, Documento documento, Deposito depositoOrigine);

	/**
	 * Cancella l'area intra.
	 * 
	 * @param areaIntra
	 *            l'area da cancellare
	 */
	void cancellaAreaIntra(AreaIntra areaIntra);

	/**
	 * Cancella l'areaIntra del documento.
	 * 
	 * @param documento
	 *            documento
	 */
	void cancellaAreaIntra(Documento documento);

	/**
	 * Carica l'area intra legata al documento, ne crea una nuova nel caso non venga trovata.
	 * 
	 * @param documento
	 *            documento interessato
	 * @return areaIntra per il documento
	 */
	AreaIntra caricaAreaIntraByDocumento(Documento documento);

	/**
	 * Carica i documenti senza una area intra che però dovrebbero averne una<br>
	 * Vedi Documento.isAreaIntraAbilitata(String codiceNazioneAzienda) per i dettagli sulla regola di dominio.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return lista di documenti senza aree intra.
	 */
	List<Documento> caricaDocumentiSenzaIntra(ParametriRicercaAreaIntra parametri);

	/**
	 * Genera l'area intra partendo dalle informazioni del documento; le informazioni non sono sufficienti a generare un
	 * documento completo quindi non viene salvato.
	 * 
	 * @param documento
	 *            il documento da cui generare l'area intra
	 * @return AreaIntra
	 */
	AreaIntra generaAreaIntra(Documento documento);

	/**
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return areeIntra trovate
	 */
	List<AreaContabile> ricercaAreeContabiliConIntra(ParametriRicercaAreaIntra parametri);

	/**
	 * Salva l'area intra.
	 * 
	 * @param areaIntra
	 *            l'area da salvare
	 * @return l'area intra salvata
	 */
	AreaIntra salvaAreaIntra(AreaIntra areaIntra);

}
