package it.eurotn.panjea.iva.manager.interfaces;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.service.exception.IvaException;
import it.eurotn.panjea.iva.util.IImponibiliIvaQueryExecutor;
import it.eurotn.panjea.iva.util.RigaIvaRicercaDTO;
import it.eurotn.panjea.iva.util.parametriricerca.ParametriRicercaRigheIva;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;

@Local
public interface AreaIvaManager {

    /**
     * Associa un'area contabile ad un area iva esistente<br/>
     * Se area contabile = null disassocia l'area contabile.
     *
     * @param areaContabile
     *            area contabile da associare
     * @param areaIva
     *            area iva esistente
     * @return area iva
     */
    AreaIva associaAreaContabile(AreaIva areaIva, AreaContabile areaContabile);

    /**
     * Carica l'area iva associata all'area contabile scelta.
     *
     * @param areaContabile
     *            l'area contabile di cui richiedere l'area iva associata
     * @return AreaIva associata all'area contabile
     */
    AreaIva caricaAreaIva(AreaContabile areaContabile);

    /**
     * Carica l'area iva con un'area iva esistente che puo' essere una area iva con valorizzato solo
     * l'id.
     *
     * @param areaIva
     *            l'area iva da caricare, l'id deve essere valorizzato
     * @return AreaIva
     */
    AreaIva caricaAreaIva(AreaIva areaIva);

    /**
     * Carica l'area iva associata al documento scelto.
     *
     * @param documento
     *            il documento di cui richiedere l'area iva associata
     * @return AreaIva associata al documento
     */
    AreaIva caricaAreaIvaByDocumento(Documento documento);

    /**
     * metodo che carica {@link RigaIva}.
     *
     * @param id
     *            id della riga iva da caricare
     * @return riga iva caricata
     * @throws IvaException
     *             eccezione generica
     */
    RigaIva caricaRigaIva(Integer id) throws IvaException;

    /**
     * Invalida parte iva e contabile e se necessario giornale iva e libro giornale.
     *
     * @param areaIva
     *            area iva
     */
    void checkInvalidaAreeCollegate(AreaIva areaIva);

    /**
     * Crea una nuova areaIva,associando l'areaContabile passata come parametro e inizializzando la
     * list di righe iva.
     *
     * @param areaContabile
     *            l'areaContabile da associare all'areaIva creata
     * @return AreaIva la nuova area iva salvata
     */
    AreaIva creaAreaIva(AreaContabile areaContabile);

    /**
     * Crea una nuova areaIva con un documento e una areaContabile.
     *
     * @param documento
     *            documento di riferimento
     * @param areaContabile
     *            area contabile esistente
     * @return AreaIva area iva creata
     */
    AreaIva creaAreaIva(Documento documento, AreaContabile areaContabile);

    /**
     * Genera AreaIva per il modulo di magazzino e la restituisce. Attenzione: se non � abilitata
     * l'area iva per il tipo documento restituisco una area iva vuota con le righe iva.
     *
     * @param areaMagazzino
     *            area magazzino
     * @param areaPartite
     *            area partite
     * @return AreaIva area iva creata
     */
    AreaIva generaAreaIvaDaMagazzino(AreaMagazzino areaMagazzino, AreaPartite areaPartite);

    /**
     * Calcola restituisce una {@link Collection} di {@link RigaIva} per gli argomenti areaMagazzino
     * e areaIva.<br>
     * <b>NB:</B>Se areaIva e' null non salva le righe <br>
     * il calcolo avviene mediante la query RigaArticolo.caricaImportiIva che esegue l'aggregazione
     * di totaleRiga.importoInValutaAzienda<br>
     * raggruppando per {@link CodiceIva}.<br/>
     * Se l'area magazzino ha il flag di addebito delle spese di incasso e genera rate<br/>
     * (potrebbe avere un {@link TipoAreaPartita} con {@link TipoAreaPartita#getTipoOperazione()}
     * =nessuna) <br/>
     * inserisco le spese nell'imponibile piu' alto.
     *
     * @param executor
     *            {@link IImponibiliIvaQueryExecutor}
     * @param documento
     *            documento
     * @param addebitoSpeseIncasso
     *            indica se addebitare le spese di incasso
     * @param areaIva
     *            areaIva da associare alle righe. Se null non salvo le righe Iva
     * @param areaPartite
     *            Se ho un area partite con delle spese di incasso metto le spese sul codice iva con
     *            importo maggiore
     * @return List<RigaIva>
     */
    List<RigaIva> generaRigheIva(IImponibiliIvaQueryExecutor executor, Documento documento,
            boolean addebitoSpeseIncasso, AreaIva areaIva, AreaPartite areaPartite);

    /**
     * Genera una lista di righe iva calcolate dall'areaMagazzino.
     *
     * @param areaMagazzino
     *            areaMagazzino
     * @param areaPartite
     *            areaPartite legata al documento
     * @return righeIva calcolate
     */
    List<RigaIva> generaRigheIvaRiepilogo(AreaMagazzino areaMagazzino, AreaPartite areaPartite);

    /**
     * Esegue la invalidazione delle righe iva.
     *
     * @param areaIva
     *            l'area da invalidare
     * @return AreaIva l'area invalidata
     */
    AreaIva invalidaAreaIva(AreaIva areaIva);

    /**
     * Esegue la ricerca delle righe iva in base ai parametri.
     *
     * @param parametriRicercaRigheIva
     *            parametri ri ricerca
     * @return righe iva caricate
     */
    List<RigaIvaRicercaDTO> ricercaRigheIva(ParametriRicercaRigheIva parametriRicercaRigheIva);

    /**
     * Salva una area Iva senza eseguire controlli.
     *
     * @param areaIva
     *            l'area da salvare
     * @return AreaIva aggiornata
     */
    AreaIva salvaAreaIva(AreaIva areaIva);

    /**
     * metodo che salva {@link RigaIva}.<br>
     * l'attributo tipoAreaContabile serve per controllare la presenza dell'attributo
     * codiceIvaCollegato se la gestioneIva è GestioneIva.INTRA o GestioneIva.ART17.
     *
     * @param rigaIva
     *            la riga da salvare
     * @param tipoAreaContabile
     *            il tipoAreacontabile
     * @return RigaIva la riga iva salvata
     * @throws CodiceIvaCollegatoAssenteException
     *             se manca il codice iva collegato
     */
    RigaIva salvaRigaIva(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException;

    /**
     * metodo che salva {@link RigaIva}.<br>
     * l'attributo tipoAreaContabile serve per controllare la presenza dell'attributo
     * codiceIvaCollegato se la gestioneIva è GestioneIva.INTRA o GestioneIva.ART17.
     *
     * @param rigaIva
     *            la riga da salvare
     * @param tipoAreaContabile
     *            il tipoAreacontabile
     * @return RigaIva la riga iva salvata
     * @throws CodiceIvaCollegatoAssenteException
     *             se manca il codice iva collegato
     */
    RigaIva salvaRigaIvaNoCkeck(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException;

    /**
     * Da una areaIva e una area contabile esegue l'aggiornamento delle proprieta' registroIva e
     * registroIvaCollegato di areaIva.
     *
     * @param areaIva
     *            l'area a cui associare registro e registro collegato
     * @param areaContabile
     *            area da cui recuperare i registri del tipo area contabile
     * @return AreaIva l'area aggiornata
     */
    AreaIva updateRegistroAreaIva(AreaIva areaIva, AreaContabile areaContabile);

    /**
     * Esegue la validazione delle righe iva controllandone la quadratura e modificandone gli
     * attributi di validazione.
     *
     * @param areaIva
     *            l'area da validare
     * @return AreaIva aggiornata
     */
    AreaIva validaAreaIva(AreaIva areaIva);
}
