package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.domain.AttributoCategoria;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

import java.util.List;

import javax.ejb.Local;

@Local
public interface CategorieManager {

	/**
	 * 
	 * @param idCategoria
	 *            id categoria da cancellare
	 */
	void cancellaCategoria(Integer idCategoria);

	/**
	 * Carica la categoria richiesta.
	 * 
	 * @param categoria
	 *            categoria da caricare. Obbligatorio solamente il campo ID.
	 * @param initializeLazy
	 *            inizializza le varie liste collegate (tipiattributi,tipiattributiereditati,descrizionilingua)
	 * @return categoria caricata.
	 */
	Categoria caricaCategoria(Categoria categoria, boolean initializeLazy);

	/**
	 * Carica tutte le categorie padri dell'azienda.<br>
	 * . Inizializza le categorie figlie di {@link CategoriaLite}
	 * 
	 * @return Lista delle categorie con lingua e categorie figli impostate
	 */
	List<CategoriaLite> caricaCategorie();

	/**
	 * Carica una lista di categorie contenenti solamente il codice e la descrizione.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return list di categorie con solamente codice e descrizione
	 */
	List<Categoria> caricaCategorieCodiceDescrizione(String fieldSearch, String valueSearch);

	/**
	 * Data una categoria carica tutte le categorie figlie e di tutti i rami collegati e li "spiana" in una lista.<br/>
	 * 
	 * @param idCategoria
	 *            id della categoria padre.
	 * @return lista dei figli e dei figli delle sottocategorie per la categoria richiesta
	 */
	List<CategoriaLite> caricaCategorieFiglie(Integer idCategoria);

	/**
	 * Crea una nuova categoria inizializzado tutti gli attributi ereditati da quella padre. <br/>
	 * La nuova categoria verrà legata a quella padre se questa non è nulla. <b>NB</b>.La categoria non è salvata, ma
	 * viene solamente creata.
	 * 
	 * @param idCategoriaPadre
	 *            categoria da settare come padre della nuova categoria.
	 * @return categoria creata.
	 */
	Categoria creaCategoria(Integer idCategoriaPadre);

	/**
	 * Salva la categoria passata.
	 * 
	 * @param categoria
	 *            le collection non vengono salvate assieme alla categoria.
	 * @return categoria salvata con le collection inizializzate
	 */
	Categoria salvaCategoria(Categoria categoria);

	/**
	 * Sincronizza gli attributi degli articoli di una categoria.<br>
	 * Se da una categoria tolgo/aggiungo un tipoAttributo devo toglierlo/aggiungerlo anche dai suoi articoli. <br>
	 * Se sposto degli articoli fra categorie devo sincronizzare gli attributi con i nuovi tipoAttributo della
	 * categoria.
	 * 
	 * @param attributiOld
	 *            lista dei vecchi attributi della categoria
	 * @param categoria
	 *            categoria interessata alla sincronizzazione dei suoi articoli.
	 */
	void sincronizzaAttributiArticoli(Categoria categoria, List<AttributoCategoria> attributiOld);

}