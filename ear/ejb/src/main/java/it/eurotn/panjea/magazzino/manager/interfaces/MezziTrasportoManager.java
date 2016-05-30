package it.eurotn.panjea.magazzino.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;

@Local
public interface MezziTrasportoManager {

    /**
     * Cancellazione di un record dall'anagrafica mezzo di trasporto.
     *
     * @param mezzoTrasporto
     *            {@link MezzoTrasporto} da cancellare
     */
    void cancellaMezzoTrasporto(MezzoTrasporto mezzoTrasporto);

    /**
     * Cancellazione di un record dall'anagrafica tipo mezzo di trasporto.
     *
     * @param tipoMezzoTrasporto
     *            {@link TipoMezzoTrasporto} da cancellare
     */
    void cancellaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto);

    /**
     * Carica i {@link MezzoTrasporto} presenti nell'anagrafica.<br/>
     *
     * @param targa
     *            targa da filtrare
     * @param abilitato
     *            true se voglio caricare solamente quelli abilitati
     * @param entita
     *            null se non si vuole filtrare l'entità
     * @param senzaCaricatore
     *            true per caricare i mezzi di trasporto senza un caricatore collegato
     * @return lista dei {@link MezzoTrasporto}
     */
    List<MezzoTrasporto> caricaMezziTrasporto(String targa, boolean abilitato, EntitaLite entita,
            boolean senzaCaricatore);

    /**
     * Carica i {@link TipoMezzoTrasporto} presenti nell'anagrafica.<br/>
     *
     * @return lista dei {@link TipoMezzoTrasporto}
     */
    List<TipoMezzoTrasporto> caricaTipiMezzoTrasporto();

    /**
     * Crea un nuovo deposito e lo associa al mezzo di trasporto.
     *
     * @param mezzoTrasporto
     *            mezzo di trasporto
     * @param codiceDeposito
     *            codice da assegnare al deposito
     * @param descrizioneDeposito
     *            descrizione da assegnare al deposito
     * @return mezzo di trasporto con il deposito associato
     */
    MezzoTrasporto creaNuovoDepositoMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto, String codiceDeposito,
            String descrizioneDeposito);

    /**
     * Rimuove il deposito del mezzo di trasporto e lo cancella. Se la cancellazione non dovesse
     * andare a buon fine perchè il deposito è legato a documenti questo viene disabilitato.
     *
     * @param mezzoTrasporto
     *            mezzo di trasporto
     * @return mezzo di trasporto senza il deposito
     */
    MezzoTrasporto rimuoviDepositoDaMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto);

    /**
     * Salva un record {@link MezzoTrasporto}.
     *
     * @param mezzoTrasporto
     *            oggetto con i nuovi dati
     * @return {@link MezzoTrasporto} salvato
     */
    MezzoTrasporto salvaMezzoTrasporto(MezzoTrasporto mezzoTrasporto);

    /**
     * Salva un record {@link TipoMezzoTrasporto}.
     *
     * @param tipoMezzoTrasporto
     *            oggetto con i nuovi dati
     * @return {@link TipoMezzoTrasporto} salvato
     */
    TipoMezzoTrasporto salvaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto);

}
