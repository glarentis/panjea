package it.eurotn.panjea.magazzino.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.InventarioArticoloDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface GestioneInventarioArticoloService {

	/**
	 * Setta il valore della giacenza reale con quello della giacenza calcolata per l'inventario articolo del deposito e
	 * data richiesti. Se esiste già della giacenza reale viene aggiunto il valore per la differenza.
	 * 
	 * @param data
	 *            data
	 * @param deposito
	 *            deposito
	 */
	void avvaloraGiacenzaRealeInventarioArticolo(Date data, DepositoLite deposito);

	/**
	 * Cancella un inventario in preparazione.
	 * 
	 * @param data
	 *            data
	 * @param deposito
	 *            deposito
	 */
	void cancellaInventarioArticolo(Date data, DepositoLite deposito);

	/**
	 * Carica tutti i depositi che posso essere utilizzati per la preparazione de un inventario. Vengono esclusi tutti
	 * quelli che hanno già un inventario in preparazione.
	 * 
	 * @return depositi
	 */
	List<Deposito> caricaDepositiPerInventari();

	/**
	 * Carica tutti gli inventari articoli presenti raggruppati per data e deposito.
	 * 
	 * @return inventari caricati
	 */
	List<InventarioArticoloDTO> caricaInventariiArticoli();

	/**
	 * Carica l'inventario articolo per data e deposito specificati.
	 * 
	 * @param date
	 *            data
	 * @param depositoLite
	 *            deposito
	 * @param caricaGiacenzeAZero
	 *            se <code>true</code> carica anche gli articoli con giacenza a 0
	 * @return inventari
	 */
	List<InventarioArticolo> caricaInventarioArticolo(Date date, DepositoLite depositoLite, boolean caricaGiacenzeAZero);

	/**
	 * Carica l'inventario articolo.
	 * 
	 * @param inventarioArticolo
	 *            inventario da caricare
	 * @return inventario caricato
	 */
	InventarioArticolo caricaInventarioArticolo(InventarioArticolo inventarioArticolo);

	/**
	 * Carica l'inventario articolo per la stampa.
	 * 
	 * @param parameters
	 *            parametri per il caricamento
	 * @return inventari caricati
	 */
	List<InventarioArticolo> caricaInventarioArticolo(Map<Object, Object> parameters);

	/**
	 * Crea gli inventari articolo.
	 * 
	 * @param data
	 *            data di riferimento
	 * @param depositi
	 *            depositi per i quali creare gli inventari articolo
	 */
	void creaInventariArticolo(java.util.Date data, List<DepositoLite> depositi);

	/**
	 * Genera l'inventario e gli eventuali movimenti di rettifica.
	 * 
	 * @param dataInventario
	 *            data di creazione dell'inventario
	 * @param dataInventarioArticolo
	 *            data inventario articolo
	 * @param deposito
	 *            deposito di riferimento
	 * @return lista di aree magazzino create ( inventario + eventuale rettifica positiva e/o negativa )
	 * @throws TipoDocumentoBaseException
	 *             sollevata se non sono configurati tutti i tipi documento necessari per la generazione dell'inventario
	 */
	List<AreaMagazzinoRicerca> generaInventario(Date dataInventario, Date dataInventarioArticolo, DepositoLite deposito)
			throws TipoDocumentoBaseException;

	/**
	 * Importa le giacenze degli articoli contenuti nel file per il deposito specificato.
	 * 
	 * @param fileImportData
	 *            file
	 * @param idDeposito
	 *            deposito di riferimento
	 * @return lista di codici articoli presenti nel file di importazione ma non in panjea
	 */
	List<String> importaArticoliInventario(byte[] fileImportData, Integer idDeposito);

	/**
	 * Salva un inventario articolo.
	 * 
	 * @param inventarioArticolo
	 *            inventario da salvare
	 * @return inventario salvato
	 */
	InventarioArticolo salvaInventarioArticolo(InventarioArticolo inventarioArticolo);
}
