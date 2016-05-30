package it.eurotn.panjea.partite.manager.interfaces;

import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;

import java.util.List;

import javax.ejb.Local;

@Local
public interface StrutturaPartitaManager {

	/**
	 * Cancella la categoria rata.
	 * 
	 * @param categoriaRata
	 *            la categoria rata da cancellare
	 */
	void cancellaCategoriaRata(CategoriaRata categoriaRata);

	/**
	 * Cancella la riga struttura partita
	 * 
	 * @param rigaStrutturaPartite
	 *            la riga da cancellare
	 */
	void cancellaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite);

	/**
	 * Cancella TUTTA la struttura partita compreso righe e formule.
	 * 
	 * @param strutturaPartite
	 *            la struttura partita da cancellare
	 */
	void cancellaStrutturaPartita(StrutturaPartita strutturaPartite);

	/**
	 * Carica una categoria rata con ID.
	 * 
	 * @param idCategoriaRata
	 *            la categoria rata
	 * @return CategoriaRata
	 */
	CategoriaRata caricaCategoriaRata(Integer idCategoriaRata);

	/**
	 * Carica tutte le categorie per elenchi.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return List<CategoriaRata>
	 */
	List<CategoriaRata> caricaCategorieRata(String fieldSearch, String valueSearch);

	/**
	 * Carica la struttura partita completa di righe e formule.
	 * 
	 * @param idStruttura
	 *            l'id della struttura da caricare
	 * @return StrutturaPartita
	 */
	StrutturaPartita caricaStrutturaPartita(Integer idStruttura);

	/**
	 * Carica tutte le strutture partite con unico discriminate il codice azienda.
	 * 
	 * @return List<StrutturaPartita>
	 */
	List<StrutturaPartitaLite> caricaStrutturePartita();

	/**
	 * Restituisce le righe della <code>StrutturaPartita</code> che serviranno al <code>CodicePagamento</code> per
	 * calcolare le scadenze relative.
	 * 
	 * @param strutturaPartita
	 *            la struttura a cui legare le righe
	 * @param numeroRate
	 *            il numero di rate da generare
	 * @param intervallo
	 *            l'intervallo di giorni tra una rata e l'altra
	 * @return List<RigaStrutturaPartite>
	 */
	List<RigaStrutturaPartite> creaRigheStrutturaPartite(StrutturaPartita strutturaPartita, int numeroRate,
			int intervallo);

	/**
	 * Metodo per salvare una categoria rata generata dal codice di pagamento (Es. F24, ....)
	 * 
	 * @param categoriaRata
	 *            la categoria da salvare
	 * @return CategoriaRata
	 */
	CategoriaRata salvaCategoriaRata(CategoriaRata categoriaRata);

	/**
	 * Salva la rigaStruttura.
	 * 
	 * @param rigaStrutturaPartite
	 *            la riga struttura da salvare
	 * @return RigaStrutturaPartite
	 */
	RigaStrutturaPartite salvaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite);

	/**
	 * Salva tutta la StrutturaPartite, con le righe e formule.
	 * 
	 * @param strutturaPartite
	 *            la struttura da salvare
	 * @return strutturaPartite salvata
	 */
	StrutturaPartita salvaStrutturaPartita(StrutturaPartita strutturaPartite);

}
