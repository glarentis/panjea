package it.eurotn.panjea.manutenzioni.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.domain.DepositoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.panjea.manutenzioni.exception.TaiSenzaTamException;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.ParametriRicercaAreeInstallazione;
import it.eurotn.panjea.manutenzioni.manager.installazioni.ParametriRicercaInstallazioni;

@Remote
public interface InstallazioniService {

    /**
     * Cancella un {@link AreaInstallazione}.
     *
     * @param id
     *            id AreaInstallazione da cancellare
     */
    void cancellaAreaInstallazione(Integer id);

    /**
     * Cancella un {@link CausaleInstallazione}.
     *
     * @param id
     *            id CausaleInstallazione da cancellare
     */
    void cancellaCausaleInstallazione(Integer id);

    /**
     *
     * @param idInstallazione
     *            installazione da cancellare
     */
    void cancellaInstallazione(Integer idInstallazione);

    /**
     * Cancella un {@link RigaInstallazione}.
     *
     * @param id
     *            id RigaInstallazione da cancellare
     */
    void cancellaRigaInstallazione(Integer id);

    /**
     * Cancella un {@link TipoAreaInstallazione}.
     *
     * @param id
     *            id TipoAreaInstallazione da cancellare
     */
    void cancellaTipoAreaInstallazione(Integer id);

    /**
     * Carica un {@link AreaInstallazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link AreaInstallazione} caricato
     */
    AreaInstallazione caricaAreaInstallazioneById(Integer id);

    /**
     * Carica tutti i {@link AreaInstallazione} presenti.
     *
     * @return {@link AreaInstallazione} caricati
     */
    List<AreaInstallazione> caricaAreeInstallazioni();

    /**
     * Carica un {@link CausaleInstallazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link CausaleInstallazione} caricato
     */
    CausaleInstallazione caricaCausaleInstallazioneById(Integer id);

    /**
     * Carica tutti i {@link CausaleInstallazione} presenti.
     *
     * @return {@link CausaleInstallazione} caricati
     */
    List<CausaleInstallazione> caricaCausaliInstallazione();

    /**
     * Carica il deposito installazione per la sede entità indicata.
     *
     * @param sedeEntita
     *            sede entià
     * @return deposito caricato, <code>null</code> se non esiste
     */
    DepositoInstallazione caricaDeposito(SedeEntita sedeEntita);

    /**
     *
     * @param id
     *            id installazione
     * @return installazione caricata
     */
    Installazione caricaInstallazione(Integer id);

    /**
     * Carica un {@link Installazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link Installazione} caricato
     */
    Installazione caricaInstallazioneById(Integer id);

    /**
     *
     * @return lista installazioni
     */
    List<Installazione> caricaInstallazioni();

    /**
     * Carica un {@link RigaInstallazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link RigaInstallazione} caricato
     */
    RigaInstallazione caricaRigaInstallazioneById(Integer id);

    /**
     * Carica tutti i {@link RigaInstallazione} presenti.
     *
     * @return {@link RigaInstallazione} caricati
     */
    List<RigaInstallazione> caricaRigheInstallazione();

    /**
     *
     * @param idAreaInstallazione
     *            id area installazione
     * @return list di righeInstallazione. Le righe installazioni sono legate 1:1 alle installazioni
     *         . Se non ci sono nel documento viene ritornata la riga nuova con l'installazione
     *         legata.
     */
    List<RigaInstallazione> caricaRigheInstallazioneByAreaInstallazione(Integer idAreaInstallazione);

    /**
     *
     * @param idArticolo
     *            id articolo
     * @return lista di movimenti di installazione per quell'articolo
     */
    List<RigaInstallazione> caricaRigheInstallazioneByArticolo(Integer idArticolo);

    /**
     *
     * @param idInstallazione
     *            id installazione
     * @return lista di movimenti per quell'installazione
     */
    List<RigaInstallazione> caricaRigheInstallazioneByInstallazione(Integer idInstallazione);

    /**
     * Carica tutti i {@link TipoAreaInstallazione} presenti.
     *
     * @return {@link TipoAreaInstallazione} caricati
     */
    List<TipoAreaInstallazione> caricaTipiAreeInstallazione();

    /**
     * Carica un {@link TipoAreaInstallazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link TipoAreaInstallazione} caricato
     */
    TipoAreaInstallazione caricaTipoAreaInstallazioneById(Integer id);

    /**
     *
     * @param idTipoDocumento
     *            id del tipo documento
     * @return tipoAreaInstallazione per il tipo documento
     */
    TipoAreaInstallazione caricaTipoAreaInstallazioneByTipoDocumento(int idTipoDocumento);

    /**
     *
     * @param idAreaInstallazione
     *            id areaInstallazione per la quale creare l'area magazzino
     * @return idAreaMagazzino creata
     * @throws TaiSenzaTamException
     *             tai senza tam
     */
    int creaAreaMagazzino(int idAreaInstallazione) throws TaiSenzaTamException;

    /**
     *
     * @param parametri
     *            parametri di ricerca
     * @return parametri di ricerca
     */
    List<AreaInstallazione> ricercaAreeInstallazioni(ParametriRicercaAreeInstallazione parametri);

    /**
     *
     * @param idEntita
     *            idEntita delle installazioni
     * @return lista delle installazioni associate all'entità
     */
    List<Installazione> ricercaByEntita(Integer idEntita);

    /**
     *
     * @param parametri
     *            parametri ricerca
     * @return lista installazioni
     */
    List<Installazione> ricercaByParametri(ParametriRicercaInstallazioni parametri);

    /**
     *
     * @param idSedeEntita
     *            sedeEntita delle installazioni
     * @return lista delle installazioni associate alla sedeEntita
     */
    List<Installazione> ricercaBySede(Integer idSedeEntita);

    /**
     * Salva un {@link AreaInstallazione}.
     *
     * @param areaInstallazione
     *            {@link AreaInstallazione} da salvare
     * @return {@link AreaInstallazione} salvato
     */
    AreaInstallazione salvaAreaInstallazione(AreaInstallazione areaInstallazione);

    /**
     * Salva un {@link CausaleInstallazione}.
     *
     * @param causaleInstallazione
     *            {@link CausaleInstallazione} da salvare
     * @return {@link CausaleInstallazione} salvato
     */
    CausaleInstallazione salvaCausaleInstallazione(CausaleInstallazione causaleInstallazione);

    /**
     *
     * @param installazione
     *            installazione da salvare
     * @return installazione salvata
     */
    Installazione salvaInstallazione(Installazione installazione);

    /**
     * Salva un {@link RigaInstallazione}.
     *
     * @param rigaInstallazione
     *            {@link RigaInstallazione} da salvare
     * @return {@link RigaInstallazione} salvato
     */
    RigaInstallazione salvaRigaInstallazione(RigaInstallazione rigaInstallazione);

    /**
     *
     * @param rigaInstallazione
     *            riga da salvare
     * @return riga da salvare con le proprietà Lazy inizializzate
     */
    RigaInstallazione salvaRigaInstallazioneInizializza(RigaInstallazione rigaInstallazione);

    /**
     * Salva un {@link TipoAreaInstallazione}.
     *
     * @param tipoAreaInstallazione
     *            {@link TipoAreaInstallazione} da salvare
     * @return {@link TipoAreaInstallazione} salvato
     */
    TipoAreaInstallazione salvaTipoAreaInstallazione(TipoAreaInstallazione tipoAreaInstallazione);

}
