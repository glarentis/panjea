package it.eurotn.panjea.manutenzioni.rich.bd;

import java.util.List;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.domain.DepositoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.ManutenzioneSettings;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.ParametriRicercaAreeInstallazione;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ParametriRicercaArticoliMI;
import it.eurotn.panjea.manutenzioni.manager.installazioni.ParametriRicercaInstallazioni;
import it.eurotn.panjea.manutenzioni.manager.operatori.ParametriRicercaOperatori;

public interface IManutenzioniBD {

    /**
     * Cancella un {@link AreaInstallazione}.
     *
     * @param id
     *            id AreaInstallazione da cancellare
     */
    void cancellaAreaInstallazione(Integer id);

    /**
     * Cancella un {@link ArticoloMI}.
     *
     * @param id
     *            id ArticoloMI da cancellare
     */
    void cancellaArticoloMI(Integer id);

    /**
     * Cancella un {@link CausaleInstallazione}.
     *
     * @param id
     *            id CausaleInstallazione da cancellare
     */
    void cancellaCausaleInstallazione(Integer id);

    /**
     * Cancella un {@link Installazione}.
     *
     * @param id
     *            id Installazione da cancellare
     */
    void cancellaInstallazione(Integer id);

    /**
     * Cancella un {@link Operatore}.
     *
     * @param id
     *            id Operatore da cancellare
     */
    void cancellaOperatore(Integer id);

    /**
     * Cancella un {@link ProdottoCollegato}.
     *
     * @param id
     *            id ProdottoCollegato da cancellare
     */
    void cancellaProdottoCollegato(Integer id);

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
     * Cancella un {@link UbicazioneInstallazione}.
     *
     * @param id
     *            id UbicazioneInstallazione da cancellare
     */
    void cancellaUbicazioneInstallazione(Integer id);

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
     * Carica tutti i {@link ArticoloMI} presenti.
     *
     * @return {@link ArticoloMI} caricati
     */
    List<ArticoloMI> caricaArticoliMI();

    /**
     * Carica l'oggetto in base al suo id e valorizza il campo installazione (se presente)
     *
     * @param id
     *            id
     * @return oggetto caricato
     */
    ArticoloMI caricaArticoloByIdConInstallazione(Integer id);

    /**
     * Carica un {@link ArticoloMI} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link ArticoloMI} caricato
     */
    ArticoloMI caricaArticoloMIById(Integer id);

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
     * Carica un {@link Installazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link Installazione} caricato
     */
    Installazione caricaInstallazioneById(Integer id);

    /**
     * Carica tutti i {@link Installazione} presenti.
     *
     * @return {@link Installazione} caricati
     */
    List<Installazione> caricaInstallazioni();

    /**
     * Carica il settings delle manutenzioni.<br/>
     * Se non esiste ne crea uno, lo salva e lo restituisce.
     *
     * @return <code>ManutenzioneSettings</code> caricato
     */
    ManutenzioneSettings caricaManutenzioniSettings();

    /**
     * Carica un {@link Operatore} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link Operatore} caricato
     */
    Operatore caricaOperatoreById(Integer id);

    /**
     * Carica tutti i {@link Operatore} presenti.
     *
     * @return {@link Operatore} caricati
     */
    List<Operatore> caricaOperatori();

    /**
     * Carica tutti i {@link ProdottoCollegato} presenti.
     *
     * @return {@link ProdottoCollegato} caricati
     */
    List<ProdottoCollegato> caricaProdottiCollegati();

    /**
     * Carica un {@link ProdottoCollegato} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link ProdottoCollegato} caricato
     */
    ProdottoCollegato caricaProdottoCollegatoById(Integer id);

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
     * @param idAreaInstallazione
     *            id dell'area installazione
     *
     * @return {@link RigaInstallazione} caricati
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
     * Carica un {@link UbicazioneInstallazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link UbicazioneInstallazione} caricato
     */
    UbicazioneInstallazione caricaUbicazioneInstallazioneById(Integer id);

    /**
     * Carica tutti i {@link UbicazioneInstallazione} presenti.
     *
     * @return {@link UbicazioneInstallazione} caricati
     */
    List<UbicazioneInstallazione> caricaUbicazioniInstallazione();

    /**
     *
     * @param idAreaInstallazione
     *            id area installazione
     * @return id area magazzino creata
     */
    int creaAreaMagazzino(int idAreaInstallazione);

    /**
     *
     * @param parametri
     *            parametri di ricerca
     * @return parametri di ricerca
     */
    List<AreaInstallazione> ricercaAreeInstallazioni(ParametriRicercaAreeInstallazione parametri);

    /**
     *
     * @param parametriRicerca
     *            parametri di ricerca per l'articolo
     * @return lista di articoli con id,codice e descrizione avvalorati
     */
    List<ArticoloMI> ricercaArticoloMI(ParametriRicercaArticoliMI parametriRicerca);

    /**
     *
     * @param idEntita
     *            idEntita delle installazioni
     * @return lista delle installazioni associate all'entità
     */
    List<Installazione> ricercaInstallazioniByEntita(Integer idEntita);

    /**
     *
     * @param parametri
     *            parametri ricerca
     * @return lista installazioni
     */
    List<Installazione> ricercaInstallazioniByParametri(ParametriRicercaInstallazioni parametri);

    /**
     *
     * @param idSedeEntita
     *            sedeEntita delle installazioni
     * @return lista delle installazioni associate alla sedeEntita
     */
    List<Installazione> ricercaInstallazioniBySede(Integer idSedeEntita);

    /**
     * Ricerca gli operatori in base ai parametri di ricerca.
     *
     * @param parametri
     *            parametri di ricerca
     * @return operatori caricati
     */
    List<Operatore> ricercaOperatori(ParametriRicercaOperatori parametri);

    /**
     * Salva un {@link AreaInstallazione}.
     *
     * @param areaInstallazione
     *            {@link AreaInstallazione} da salvare
     * @return {@link AreaInstallazione} salvato
     */
    AreaInstallazione salvaAreaInstallazione(AreaInstallazione areaInstallazione);

    /**
     * Salva un {@link ArticoloMI}.
     *
     * @param articoloMI
     *            {@link ArticoloMI} da salvare
     * @return {@link ArticoloMI} salvato
     */
    ArticoloMI salvaArticoloMI(ArticoloMI articoloMI);

    /**
     * Salva un {@link CausaleInstallazione}.
     *
     * @param causaleInstallazione
     *            {@link CausaleInstallazione} da salvare
     * @return {@link CausaleInstallazione} salvato
     */
    CausaleInstallazione salvaCausaleInstallazione(CausaleInstallazione causaleInstallazione);

    /**
     * Salva un {@link Installazione}.
     *
     * @param installazione
     *            {@link Installazione} da salvare
     * @return {@link Installazione} salvato
     */
    Installazione salvaInstallazione(Installazione installazione);

    /**
     * Salva un {@link ManutenzioneSettings}.
     *
     * @param manutenzioneSettings
     *            settings da salvare
     * @return <code>ManutenzioneSettings</code> salvato
     */
    ManutenzioneSettings salvaManutenzioneSettings(ManutenzioneSettings manutenzioneSettings);

    /**
     * Salva un {@link Operatore}.
     *
     * @param operatore
     *            {@link Operatore} da salvare
     * @return {@link Operatore} salvato
     */
    Operatore salvaOperatore(Operatore operatore);

    /**
     * Salva un {@link ProdottoCollegato}.
     *
     * @param prodottoCollegato
     *            {@link ProdottoCollegato} da salvare
     * @return {@link ProdottoCollegato} salvato
     */
    ProdottoCollegato salvaProdottoCollegato(ProdottoCollegato prodottoCollegato);

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

    /**
     * Salva un {@link UbicazioneInstallazione}.
     *
     * @param ubicazioneInstallazione
     *            {@link UbicazioneInstallazione} da salvare
     * @return {@link UbicazioneInstallazione} salvato
     */
    UbicazioneInstallazione salvaUbicazioneInstallazione(UbicazioneInstallazione ubicazioneInstallazione);

    /**
     * Sostuisce l'operatore.<br>
     * Al nuovo operatore vengono assegnate le installazioni di quello vecchio.<br>
     * Se il nuovo operatore non ha un mezzo di trasporto verrà assegnato quello dell'operatore da sostituire e tolto da
     * quest'ultimo.
     *
     * @param idOperatoreDaSostituire
     *            operatore da sostituire
     * @param idOperatore
     *            operatore di destinazione
     * @param sostituisciTecnico
     *            sostituisce solo l'operatore come tecnico
     * @param sostituisciCaricatore
     *            sostituisce solo l'operatore come caricatore
     */
    void sostituisciOperatore(Integer idOperatoreDaSostituire, Integer idOperatore, boolean sostituisciTecnico,
            boolean sostituisciCaricatore);

}
