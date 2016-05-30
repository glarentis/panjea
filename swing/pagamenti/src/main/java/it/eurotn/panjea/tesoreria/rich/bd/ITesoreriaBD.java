package it.eurotn.panjea.tesoreria.rich.bd;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaBonifico;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.domain.TesoreriaSettings;
import it.eurotn.panjea.tesoreria.service.exception.CodiceSIAAssenteException;
import it.eurotn.panjea.tesoreria.service.exception.DataRichiestaDopoIncassoException;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;
import it.eurotn.panjea.tesoreria.service.exception.TipoDocumentoChiusuraAssenteException;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.panjea.tesoreria.util.EffettoDTO;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.panjea.tesoreria.util.SituazioneRigaAnticipo;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;

public interface ITesoreriaBD {

    /**
     * Assegna il {@link RapportoBancarioAzienda} a tutte le aree assegno.
     * 
     * @param rapportoBancarioAzienda
     *            {@link RapportoBancarioAzienda} da assegnare
     * @param areeAssegno
     *            aree assegno
     */
    void assegnaRapportoBancarioAssegni(RapportoBancarioAzienda rapportoBancarioAzienda, List<AreaAssegno> areeAssegno);

    /**
     * Cancella un'area acconto.
     * 
     * @param areaAcconto
     *            areaAcconto da cancellare
     */
    void cancellaAcconto(AreaAcconto areaAcconto);

    /**
     * Cancellazione di un'area Tesoreria<br>
     * L'implementazione nel bean chiamera' la giusta implementazione del metodo.
     * 
     * @param areaTesoreria
     *            da cancellare
     */
    @AsyncMethodInvocation
    void cancellaAreaTesoreria(AreaTesoreria areaTesoreria);

    /**
     * Cancella una lista di areeTesoreria.
     * 
     * @param listAree
     *            aree da cancellare
     */
    @AsyncMethodInvocation
    void cancellaAreeTesorerie(List<AreaTesoreria> listAree);

    /**
     * Cancella uno specifico effetto.
     * 
     * @param effetto
     *            da cancellare
     */
    void cancellaEffetto(Effetto effetto);

    /**
     * Cancellazione del Pagamento.
     * 
     * @param pagamento
     *            da cancellare
     */
    void cancellaPagamento(Pagamento pagamento);

    /**
     * Cancella il pagamento se e solo se l'area tesoreria collegata è una AreaAcconto.
     * 
     * @param pagamento
     *            il pagamento da cancellare
     */
    void cancellaPagamentoAccontoLiquidazione(Pagamento pagamento);

    /**
     * esegue la cancellazione di {@link Sollecito}.
     * 
     * @param sollecito
     *            sollecito da cancellare
     */
    void cancellaSollecito(Sollecito sollecito);

    /**
     * esegue la cancellazione di {@link TemplateSolleciti}.
     * 
     * @param templateSollecito
     *            template da cancellare
     */
    void cancellaTemplateSolleciti(TemplateSolleciti templateSollecito);

    /**
     * Carica l' {@link AreaAcconto} in base ai {@link ParametriRicercaAcconti}.
     * 
     * @param parametriRicercaAcconti
     *            parametri di ricerca
     * @return acconti caricati
     */
    @AsyncMethodInvocation
    List<AreaAcconto> caricaAcconti(ParametriRicercaAcconti parametriRicercaAcconti);

    /**
     * Carica l'area contabile associata all'area tesoreria.
     * 
     * @param areaTesoreria
     *            area tesoreria
     * @return area contabile
     */
    AreaContabileLite caricaAreaContabileLite(AreaTesoreria areaTesoreria);

    /**
     * Caricamento di un'area tesoreria<br>
     * Il manager Tesoreria sa su quale manager corretto chiamare il metodo.
     * 
     * @param areaTesoreria
     *            di tipo generico
     * @return l'area tesoreia specifica
     */
    @AsyncMethodInvocation
    AreaTesoreria caricaAreaTesoreria(AreaTesoreria areaTesoreria);

    /**
     * Carica l'{@link AreaTesoreria} del documento.
     * 
     * @param documento
     *            Documento
     * @return area tesoreria caricata
     */
    @AsyncMethodInvocation
    AreaTesoreria caricaAreaTesoreria(Documento documento);

    /**
     * carica un area tesoreria legata ad un pagamento.
     * 
     * @param pagamento
     *            pagamento dell'area
     * @return areaTesoreria
     */
    @AsyncMethodInvocation
    AreaTesoreria caricaAreaTesoreria(Pagamento pagamento);

    /**
     * Carica l'area tesoreria in base alle stato dell'effetto.<br>
     * INSOLUTO: carica l'area insoluto<br>
     * CHIUSO: carica l'area accredito<br>
     * PRESENTATO: carica l'area distinta bancaria
     * 
     * @param effetto
     *            effetto di riferimento
     * @return area caricata
     */
    @AsyncMethodInvocation
    AreaTesoreria caricaAreaTesoreriaByStatoEffetto(Effetto effetto);

    /**
     * Carica le aree collegate all'areaTesoreria.
     * 
     * @param areaTesoreria
     *            area tesoreria di riferimento.
     * @return la lista di aree collegate
     */
    @AsyncMethodInvocation
    List<AreaTesoreria> caricaAreeCollegate(AreaTesoreria areaTesoreria);

    /**
     * Carica tutte le aree del documento.
     * 
     * @param idDocumento
     *            documento
     * @return aree del documento caricate
     */
    @AsyncMethodInvocation
    List<Object> caricaAreeDocumento(Integer idDocumento);

    /**
     * Carica gli assegni (AreaAssegno, AreaAccreditoAssegno) secondo i ParametriRicercaAssegni passati.
     * 
     * @param parametriRicercaAssegni
     *            i parametri per limitare la ricerca degli assegni
     * @return List<AssegnoDTO>
     */
    @AsyncMethodInvocation
    List<AssegnoDTO> caricaAssegni(ParametriRicercaAssegni parametriRicercaAssegni);

    /**
     * Carica gli effetti delle distinte effetti presentate.<br>
     * La mappa contiene il parametro con la lista degli id delle eree tesorerie da stampare. Se l'area tesoreria non è
     * definita nei tipi documento base di tesoreria verrà considerata.
     *
     * @param parametri
     *            mappa dei parametri
     * @return effetti caricati
     * @throws RapportoBancarioPerFlussoAssenteException
     *             .
     */
    List<EffettoDTO> caricaEffettiDistintePerStampa(Map<Object, Object> parametri)
            throws RapportoBancarioPerFlussoAssenteException;

    /**
     * Carica l'immagine e la associa all'assegno corrente.
     * 
     * @param areaAssegno
     *            l'area assegno a cui legare l'immagine assegno
     * @return AreaAssegno con ImmagineAssegno legata
     */
    @AsyncMethodInvocation
    AreaAssegno caricaImmagineAssegno(AreaAssegno areaAssegno);

    /**
     * Carica il totale dell'importo anticipato per il rapporto bancario e data valuta specificati.
     * 
     * @param areaEffetti
     *            areaEffetti
     * @param rapportoBancarioAzienda
     *            rapporto bancario
     * @param dataValuta
     *            data valuta
     * 
     * @return totale importo anticipato, 0 se non presente
     */
    BigDecimal caricaImportoAnticipato(AreaEffetti areaEffetti, RapportoBancarioAzienda rapportoBancarioAzienda,
            Date dataValuta);

    /**
     * Carica tutti i pagamenti della rata.
     * 
     * @param idRata
     *            idRata di cui caricare i pagamenti.
     * @return pagamenti caricati
     */
    @AsyncMethodInvocation
    List<Pagamento> caricaPagamenti(Integer idRata);

    /**
     * Carica i pagamenti dell'area acconto.
     * 
     * @param areaAcconto
     *            areaAcconto per la quale caricare i pagamenti
     * @return pagamenti legati all'acconto
     */
    @AsyncMethodInvocation
    List<Pagamento> caricaPagamentiByAreaAcconto(AreaAcconto areaAcconto);

    /**
     * Carica la rata identificata dall'id passato.
     * 
     * @param idRata
     *            l'id della rata da caricare
     * @return la rata con l'id specificato
     */
    Rata caricaRata(Integer idRata);

    /**
     * 
     * @return settings di tesoreria
     */
    TesoreriaSettings caricaSettings();

    /**
     * Carica la situazione rate filtrando con la mappa di parametri ricevuta.
     * 
     * @param parametri
     *            per il report. Contiene:<br>
     *            <ul>
     *            <li>tipoPartita tipoPartita da considerare</li>
     *            <li>dataInizio data inizio situazione</li>
     *            <li>dataFine data fine situazione</li>
     *            <li>idEntita id entità da filtrare, se null non viene considerato</li>
     *            </ul>
     * @return situazione delle rate ancora aperte
     */
    @AsyncMethodInvocation
    List<SituazioneRata> caricaSituazioneRate(Map<Object, Object> parametri);

    /**
     * 
     * @param idEntita
     *            id dell'entità per la quale caricare le rate da poter utilizzare per l'acconto.
     * @param tipoPartita
     *            tipo partita ATTIVA/PASSIVA
     * @return list di rate rate da poter pagare con un acconto.
     */
    @AsyncMethodInvocation
    List<SituazioneRata> caricaSituazioneRateDaUtilizzarePerAcconto(Integer idEntita, TipoPartita tipoPartita);

    /**
     * Carica i solleciti.
     * 
     * @return solleciti caricati
     */
    @AsyncMethodInvocation
    List<Sollecito> caricaSolleciti();

    /**
     * carica i solleciti per un determinato cliente.
     * 
     * @param idCliente
     *            id del cliente.
     * @return list of solleciti .
     */
    List<Sollecito> caricaSollecitiByCliente(Integer idCliente);

    /**
     * carica i solleciti per una rata.
     * 
     * @param idRata
     *            rata di riferimento
     * @return lista de solleciti.
     */
    List<Sollecito> caricaSollecitiByRata(Integer idRata);

    /**
     * carica la lista di {@link TemplateSolleciti}.
     * 
     * @return lista di template solliciti caricati
     */
    List<TemplateSolleciti> caricaTemplateSolleciti();

    /**
     * carica {@link TemplateSolleciti} identificato da idTemplateSollecito.
     * 
     * @param idTemplateSollecito
     *            id del template
     * @return lista di template caricati
     * @throws PagamentiException
     *             PagamentiException
     */
    TemplateSolleciti caricaTemplateSollecito(int idTemplateSollecito) throws PagamentiException;

    /**
     * Carica il tipo documento base del tipo operazione.
     * 
     * @param tipoOperazione
     *            tipo operazione
     * @return tipo documento base
     * @throws TipoDocumentoBaseException
     *             rilanciata se non esiste il tipo documento base per l'operazione
     */
    TipoDocumentoBasePartite caricaTipoDocumentoBase(
            it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione tipoOperazione)
                    throws TipoDocumentoBaseException;

    /**
     * Crea un documento di accredito da una lista di assegni scelti associando i pagamenti degli assegni all'accredito.
     * 
     * @param parametriCreazioneAreaChiusure
     *            i parametri da cui recuperare codice,data,rapporto bancario
     * @param assegni
     *            gli assegni da accreditare.
     * @return AreaAccreditoAssegno
     */
    AreaAccreditoAssegno creaAreaAccreditoAssegno(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
            List<AreaAssegno> assegni);

    /**
     * Crea un'area anticipo con le righe anticipo.
     * 
     * @param situazioneRigaAnticipo
     *            da collegare all'area
     * @return l'areaAnticipo
     */
    @AsyncMethodInvocation
    AreaAnticipo creaAreaAnticipo(List<SituazioneRigaAnticipo> situazioneRigaAnticipo);

    /**
     * Crea l'area bonifico.
     * 
     * @param dataDocumento
     *            dataDocumento
     * @param numeroDocumento
     *            numeroDocumento
     * @param areaPagamenti
     *            areaPagamenti da collegare al bonifico
     * @param spese
     *            spese
     * @param pagamenti
     *            pagamenti effettivi, vanno salvati solo questi sull'area pagamenti, l'areaBonifico prende i pagamenti
     *            dell'areaPagamenti collegata
     * @return AreaBonifico
     */
    AreaBonifico creaAreaBonifico(Date dataDocumento, String numeroDocumento, AreaPagamenti areaPagamenti,
            BigDecimal spese, Set<Pagamento> pagamenti);

    /**
     * Richiama il giusto manager per Effetti e Pagamenti.
     * 
     * @param parametriCreazioneAreaChiusure
     *            contenitore parametri
     * @param pagamenti
     *            i pagamenti che servono a chiudere effetti
     * @return aree create
     */
    @AsyncMethodInvocation
    List<AreaChiusure> creaAreaChiusurePerPagamenti(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
            List<Pagamento> pagamenti);

    /**
     * Creazione dell'area insoluti.
     * 
     * @param dataDocumento
     *            relativa al doc da generare
     * @param nDocumento
     *            relativo al doc da generare
     * @param speseInsoluto
     *            spese insoluto
     * @param effetti
     *            da cui prendere il riferimento dell'area effetti
     * @return l'area creata
     */
    AreaInsoluti creaAreaInsoluti(Date dataDocumento, String nDocumento, BigDecimal speseInsoluto,
            Set<Effetto> effetti);

    /**
     * Creazione di aree Accredito in base alla data Valuta e area Effetto.
     * 
     * @param effetti
     *            da cui ricavare i dati per generare l'area Accredio
     * @param dataScritturaPosticipata
     *            la data richiesta per la scrittura posticipata di un accredito
     * @return la lista delle aree Accredito generate
     * @throws DataRichiestaDopoIncassoException
     *             data richiesta per scrittura posticipata exception
     */
    List<AreaAccredito> creaAreeAccredito(List<Effetto> effetti, Date dataScritturaPosticipata)
            throws DataRichiestaDopoIncassoException;

    /**
     * Creazione della distinta Bancaria.
     * 
     * @param dataDocumento
     *            da generare
     * @param numeroDocumento
     *            da generare
     * @param areaEffetto
     *            da collegare alla distinta
     * @param effetti
     *            effetti con data valuta impostata
     * @param spese
     *            spese di gestione della distinta. Verranno divise per il numero di effetti e settate in ogni effetto
     *            come spesa.
     * @param speseDistinta
     *            spese della distina
     * @return l'areaDistitaBancaria creata
     */
    @AsyncMethodInvocation
    AreaDistintaBancaria creaDistintaBancaria(Date dataDocumento, String numeroDocumento, AreaEffetti areaEffetto,
            BigDecimal spese, BigDecimal speseDistinta, Set<Effetto> effetti);

    /**
     * Usando tutti gli acconti disponibili vengono creati i pagamenti per ogni rata.
     * 
     * @param pagamenti
     *            pagamento da recupero rata e importo da pagare con associato l'importo da pagare per l'acconto
     * @param acconti
     *            acconti di origine disponibili, se null carica acconti automatici per ogni rata
     * @return la lista di aree chiusura generate
     */
    @AsyncMethodInvocation
    List<AreaChiusure> creaPagamentiConAcconto(List<Pagamento> pagamenti, List<AreaAcconto> acconti);

    /**
     * Ritorna il testo per il report del sollecito.
     * 
     * @param testo
     *            testo contenente le variabili da avvalorare
     * @param sollecito
     *            sollecito di cui generare il testo dal template
     * @return ritorna il testo per il sollecito
     */
    String creaTesto(String testo, Sollecito sollecito);

    /**
     * Crea un file in locale e ritorna il path del file creato.
     * 
     * @param idDocumento
     *            area da esportare
     * @return path del file creato sul server per poterlo recuperare in un secondo momento
     * @throws CodiceSIAAssenteException
     * @throws TipoDocumentoChiusuraAssenteException
     */
    @AsyncMethodInvocation
    String generaFlusso(Integer idDocumento);

    /**
     * Ricerca le aree tesorerie in base ai paramentri di ricerca.
     * 
     * @param parametriRicercaAreeTesoreria
     *            parametri di ricerca
     * @return lista di aree caricate
     */
    @AsyncMethodInvocation
    List<AreaTesoreria> ricercaAreeTesorerie(ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria);

    /**
     * Ricerca gli effetti, parametri ricerca definiscono i limiti, risalendo dai pagamenti.
     * 
     * @param parametriRicercaEffetti
     *            limiti della ricerca
     * @return lista degli effetti
     */
    @AsyncMethodInvocation
    List<SituazioneEffetto> ricercaEffetti(ParametriRicercaEffetti parametriRicercaEffetti);

    /**
     * Ricerca le rate.
     * 
     * @param parametriRicercaRate
     *            parametri di ricerca
     * @return rate caricate
     */
    @AsyncMethodInvocation
    List<SituazioneRata> ricercaRate(ParametriRicercaRate parametriRicercaRate);

    /**
     * 
     * @param tesoreriaSettings
     *            settings da salvare
     * @return settings salvati
     */
    TesoreriaSettings salva(TesoreriaSettings tesoreriaSettings);

    /**
     * Salva un {@link DocumentoAcconto}.
     * 
     * @param areaAcconto
     *            acconto da salvare
     * @return acconto salvato
     */
    AreaAcconto salvaAreaAcconto(AreaAcconto areaAcconto);

    /**
     * Salva un'area tesoreria.
     * 
     * @param areaTesoreria
     *            areaTesoreria da salvare
     * @return areaTesoreria salvata
     */
    AreaTesoreria salvaAreaTesoreria(AreaTesoreria areaTesoreria);

    /**
     * Salva una lista di solleciti.
     * 
     * @param solleciti
     *            solleciti da salvare
     * @return solleciti salvati
     */
    List<Sollecito> salvaSolleciti(List<Sollecito> solleciti);

    /**
     * esegue il salvataggio di{@link Sollecito}.
     * 
     * @param sollecito
     *            sollecito da salvare
     * @return sollecito salvato
     */
    Sollecito salvaSollecito(Sollecito sollecito);

    /**
     * esegue il salvataggio di {@link TemplateSolleciti}.
     * 
     * @param templateSolleciti
     *            template da salvare
     * @return template salvati
     */
    TemplateSolleciti salvaTemplateSolleciti(TemplateSolleciti templateSolleciti);
}
