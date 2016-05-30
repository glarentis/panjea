package it.eurotn.panjea.stampe.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;

import java.util.List;

import javax.ejb.Local;

@Local
public interface LayoutStampeManager {

	/**
	 * Aggiunge al tipo area documento il layout stampa associato al report indicato.
	 *
	 * @param tipoAreaDocumento
	 *            tipo area documento
	 * @param reportName
	 *            report da associare al layout
	 * @param entita
	 *            entità da associare al layout. Se nullo il layout sarà valido per tutte le entità
	 * @param sedeEntita
	 *            sede dell'entità
	 * @return layout creato
	 */
	LayoutStampa aggiungiLayoutStampa(ITipoAreaDocumento tipoAreaDocumento, String reportName, EntitaLite entita,
			SedeEntita sedeEntita);

	/**
	 * Cancella i {@link LayoutStampa} che si riferiscono al tipo area specificato.
	 *
	 * @param tipoArea
	 *            tipo area
	 */
	void cancellaLayoutStampa(ITipoAreaDocumento tipoArea);

	/**
	 * Cancella un {@link LayoutStampa}.
	 *
	 * @param layoutStampa
	 *            layout da cancellare
	 */
	void cancellaLayoutStampa(LayoutStampa layoutStampa);

	/**
	 * Carica tutti i layout di stampa batch per il tipo area specificato. Se viene specificata anche l'entità e sede
	 * verranno aggiunti anche i layout bach personalizzati per l'entità e sede.
	 *
	 * @param tipoAreaDocumento
	 *            tipo area specificato
	 * @param entita
	 *            entita
	 * @param sedeEntita
	 *            sede entità
	 * @return layouts caricati
	 */
	List<LayoutStampaDocumento> caricaLayoutStampaBatch(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
			SedeEntita sedeEntita);

	/**
	 * @return lista di layoutManager associati a dei documenti
	 */
	List<LayoutStampaDocumento> caricaLayoutStampaPerDocumenti();

	/**
	 * Carica tutti i {@link LayoutStampa} definiti.<br>
	 *
	 * @return layout caricati
	 */
	List<LayoutStampa> caricaLayoutStampe();

	/**
	 * Carica tutti i {@link LayoutStampa} personalizzati per l'entità.<br>
	 *
	 * @param idEntita
	 *            id entità
	 * @return layout caricati
	 */
	List<LayoutStampaDocumento> caricaLayoutStampe(Integer idEntita);

	/**
	 * Carica tutti i layout stampe per il tipo area e entita selezionato.
	 *
	 * @param tipoAreaDocumento
	 *            tipo area
	 * @param entita
	 *            entita
	 * @param sedeEntita
	 *            sede entita
	 * @return layout caricati
	 */
	List<LayoutStampaDocumento> caricaLayoutStampe(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
			SedeEntita sedeEntita);

	/**
	 * @param reportName
	 *            nome del report
	 * @return layout per il report
	 */
	LayoutStampa caricaLayoutStampe(String reportName);

	/**
	 * Carica tutti i tipi area legati alla classeTipoDocumento.
	 *
	 * @param classeTipoDocumento
	 *            classe
	 * @return tipi aree caricati
	 */
	List<ITipoAreaDocumento> caricaTipoAree(String classeTipoDocumento);

	/**
	 *
	 * @param tipoAreaDocumenrtoDestinazione
	 *            tipo area documento per la quale clonare i layout.
	 * @param tipoAreaDocumentoOrigine
	 *            origine dei layout
	 */
	void cloneLayoutStampa(ITipoAreaDocumento tipoAreaDocumenrtoDestinazione,
			ITipoAreaDocumento tipoAreaDocumentoOrigine);

	/**
	 * Salva un {@link LayoutStampa}.
	 *
	 * @param layoutStampa
	 *            layout da salvare
	 * @return layout salvato
	 */
	LayoutStampa salvaLayoutStampa(LayoutStampa layoutStampa);

	/**
	 * Setta il layout di stampa come predefinito. Restituisce tutti i layout configurati per il tipo area di
	 * riferimento. (Oltre al layout impostato come predefinito anche il predefinito precedente viene modificato)
	 *
	 * @param layoutStampa
	 *            layout da impostare come predefinito
	 * @return layouts
	 */
	List<LayoutStampaDocumento> setLayoutAsDefault(LayoutStampaDocumento layoutStampa);

	/**
	 * Setta il layout di stampa come predefinito per l'invio delle email. Restituisce tutti i layout configurati per il
	 * tipo area di riferimento. (Oltre al layout impostato come predefinito anche il predefinito precedente viene
	 * modificato)
	 *
	 * @param layoutStampa
	 *            layout da impostare come predefinito per l'invio email
	 * @return layouts
	 */
	List<LayoutStampaDocumento> setLayoutForInvioMail(LayoutStampaDocumento layoutStampa);

	/**
	 * Setta il layout di stampa come ad uso interno. Restituisce tutti i layout configurati per il tipo area di
	 * riferimento. (Oltre al layout impostato come predefinito anche il predefinito precedente viene modificato)
	 *
	 * @param layoutStampa
	 *            layout da impostare ad uso interno
	 * @param usoInterno
	 *            <code>true</code> per settarlo come uso interno, <code>false</code> altrimenti
	 * @return layouts
	 */
	List<LayoutStampaDocumento> setLayoutForUsoInterno(LayoutStampaDocumento layoutStampa, boolean usoInterno);
}
