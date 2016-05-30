package it.eurotn.panjea.magazzino.manager.interfaces.articolo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.manager.articolo.ArticoloValorizzazioneBOM;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;

@Local
public interface DistintaBaseManager {

    /**
     * Aggiunge un articolo ad una configurazione distinta collegata al componentePadre.
     *
     * @param configurazioneDistinta
     *            {@link ConfigurazioneDistinta}
     * @param articoloDaAggiungere
     *            articolo da aggiungere
     * @param componentePadre
     *            componente al quale collegare il nuovo componente
     * @return componente componente aggiunto e collegato al componente padre
     * @throws DistintaCircolareException
     *             rilanciata se ho un riferimento circolare nella disinta
     */
    Componente aggiungiComponenteAConfigurazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Articolo articoloDaAggiungere) throws DistintaCircolareException;

    /***
     * Utilizzato solamente per importazione conf. 2 da Europa
     * 
     * @param c
     *            .
     * @param componentePadre
     *            .
     * @param articoloComponente
     *            .
     * @return .
     */
    Componente aggiungiComponenteAConfigurazionePerImportazione(ConfigurazioneDistinta c, Componente componentePadre,
            Articolo articoloComponente);

    /**
     *
     * @param configurazione
     *            configurazione della distinta
     * @param articolo
     *            componente al quale aggiungere le fasi
     * @param fasiLavorazioni
     *            fasi da aggiungere
     * @return fasi aggiunte al componente
     */
    Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione, ArticoloLite articolo,
            Set<FaseLavorazioneArticolo> fasiLavorazioni);

    /**
     *
     * @param configurazione
     *            configurazione della distinta
     * @param componente
     *            componente al quale aggiungere le fasi
     * @param fasiLavorazioni
     *            fasi da aggiungere
     * @return a aggiunte alò'articolo
     */
    Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione, Componente componente,
            Set<FaseLavorazioneArticolo> fasiLavorazioni);

    /**
     * Cancella un componente dalla configurazione distinta.
     *
     * @param componenteSelezionato
     *            componente da cancellare
     */
    void cancellaComponentiConfigurazioneDistinta(List<Componente> componenteSelezionato);

    /**
     * Cancella una configurazione distinta e tutti i suoi componenti collegati.
     *
     * @param configurazioneDistinta
     *            configurazione distinta
     */
    void cancellaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta);

    /**
     * cancella le fasiArticolo da una configurazione.
     *
     * @param configurazioneDistinta
     *            configurazioneDistinta
     * @param fasiArticoloDaCancellare
     *            fasiArticoloDaCancellare
     */
    void cancellaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta,
            List<FaseLavorazioneArticolo> fasiArticoloDaCancellare);

    /**
     *
     * @return tutti gli articoli legati ai componenti, che siano distinta o componenti.
     */
    Map<Integer, ArticoloLite> caricaArticoliComponenti();

    /**
     *
     * @return articoli con le proprietà utilizzate nella valorizzazione della distinta. Gli
     *         articoli sono solamente quelli utilizzati come componenti o disinte
     */
    List<ArticoloValorizzazioneBOM> caricaArticoliValorizzazioneBOM();

    /**
     * Carica un articoloConfigurazione con tutti i dati per la distinta caricati (fasi componenti
     * caricati in deep e distinte padre).<br>
     * Da notare che nel caso in cui l'ArticoloConfigurazioneDistinta fosse una distinta base, le
     * fasi lavorazione articolo sarebbero le fasi dall'anagrafica articolo, ma per evitare di
     * cambiare la lettura delle informazioni tra distinta base e configurazione, le fasi
     * dell'anagrafica articolo vengono impostate direttamente sul componente (clonato e quindi non
     * in sessione).
     *
     * @param configurazioneDistinta
     *            conf. per la quale caricare i dati.
     * @return articolo con tutti i dati per la distinta inizializzati.
     * @throws DistintaCircolareException
     *             rilanciata se ho un riferimento circolare nella disinta
     */
    ArticoloConfigurazioneDistinta caricaArticoloConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta)
            throws DistintaCircolareException;

    /**
     *
     * @param articolo
     *            articolo distinta
     * @return componenti che compongono la distinta.
     * @throws DistintaCircolareException
     *             rilanciata se ho un riferimento circolare nella disinta
     */
    Set<Componente> caricaComponenti(Articolo articolo) throws DistintaCircolareException;

    /**
     *
     * @param configurazioneDistinta
     *            configurazione delal disinta da caricare
     *
     * @return componenti che compongono la configurazione della distinta.
     * @throws DistintaCircolareException
     *             rilanciata se ho un riferimento circolare nella disinta
     */
    Set<Componente> caricaComponenti(ConfigurazioneDistinta configurazioneDistinta) throws DistintaCircolareException;

    /**
     *
     * @param idConfigurazione
     *            conf da caricare
     * @return configurazioneDistinta
     */
    ConfigurazioneDistinta caricaConfigurazioneDistinta(int idConfigurazione);

    /**
     *
     * @param distinta
     *            articolo distinta.
     * @return lista delle configurazioni per la distinta. La configurazione base viene messa
     *         all'inizio.
     */
    List<ConfigurazioneDistinta> caricaConfigurazioniDistinta(ArticoloLite distinta);

    /**
     * Carica la lista di {@link Componente} che rappresenta le distinte che hanno quell'articolo
     * componente.
     *
     * @param idArticolo
     *            l'id articolo del componente di cui recuperare le distinte
     * @return List<Componente> che rappresenta le distinte che hanno l'articolo componente come
     *         componenti
     */
    Set<Componente> caricaDistinteComponente(Integer idArticolo);

    /**
     *
     * @param configurazioneDistinta
     *            configurazione distinta
     * @param componente
     *            componente
     * @return fasi collegate alla configurazione
     */
    Set<FaseLavorazioneArticolo> caricaFasiLavorazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componente);

    /**
     * Carica la qta di attrezzaggio per l'articolo/configurazione e somma la qta delle fasi
     * direttamente collegate ad esso.
     *
     * @param distinta
     *            distinta
     * @param articolo
     *            comp.
     * @param configurazioneDistinta
     *            conf.
     * @return qta di attrezzaggio per componente a configurazione qtaAttrezzaggio componente +
     *         qtaAttrezzaggio fasi direttamente collegate
     */
    double caricaQtaAttrezzaggioComponenti(ArticoloLite distinta, ArticoloLite articolo,
            ConfigurazioneDistinta configurazioneDistinta);

    /**
     * Rimuove refereze circolare da una distinta.
     *
     * @param articolo
     *            articolo distinta con referenze circolari.
     */
    void rimuoviReferenzaCircolare(ArticoloLite articolo);

    /**
     * Salva il componente.
     *
     * @param componente
     *            componente da salvare
     * @return componente salvato
     */
    Componente salvaComponente(Componente componente);

    /**
     * Salva una configurazione di una distinta. Se nuova copia dalla configurazione base tutti i
     * componenti.
     *
     * @param configurazioneDistinta
     *            conf da salvare.
     * @return configurazioneDistintaSalvata
     */
    ConfigurazioneDistinta salvaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta);

    /**
     * Salva le distinte e i componenti collegati ad un articolo.
     *
     * @param articoloLite
     *            articolo
     * @param distinte
     *            componenti distinte
     * @param componenti
     *            componenti
     * @param fasiLavorazioni
     *            fasi associate alla distinta
     * @throws DistintaCircolareException
     *             rilanciata se ho un riferimento circolare nella disinta rilanciata se ho un
     *             riferimento circolare nella disinta
     */
    void salvaDistintaArticolo(ArticoloLite articoloLite, Set<Componente> distinte, Set<Componente> componenti,
            Set<FaseLavorazioneArticolo> fasiLavorazioni) throws DistintaCircolareException;

    /**
     * Salva la fase lavorazione articolo.
     *
     * @param faseLavorazioneArticolo
     *            faseLavorazioneArticolo da salvare
     * @return faseLavorazioneArticolo salvata
     */
    FaseLavorazioneArticolo salvaFaseLavorazioneArticolo(FaseLavorazioneArticolo faseLavorazioneArticolo);

    /**
     *
     * @param idConfigurazioneDistinta
     *            conf distinta
     * @param idComponentePadre
     *            comp padre
     * @param idComponenteSelezionato
     *            comp da sostituire
     * @param idArticoloSostitutivo
     *            nuovo articolo da inserire
     * @return
     * @throws DistintaCircolareException
     *             rilanciata su rif. circolare
     */
    Componente sostituisciComponenteAConfigurazione(Integer idConfigurazioneDistinta, Integer idComponentePadre,
            Integer idComponenteSelezionato, Integer idArticoloSostitutivo) throws DistintaCircolareException;

}
