package it.eurotn.panjea.magazzino.manager.documento.fatturazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.SortedList;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.manager.noteautomatiche.interfaces.NoteAutomaticheManager;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces.DocumentoFatturazioneGenerator;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.validation.RigaMagazzinoFatturazioneValidatorFactory;
import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.RigheArticoloBuilderFactoryBean.EGeneratoreRiga;
import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.IGeneratoreRigheArticolo;
import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.RigheArticoloBuilderFactory;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigheCollegateManager;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.StrategiaTotalizzazione;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.Totalizzatore;
import it.eurotn.panjea.magazzino.manager.intento.exception.ArticoloAddebitoDichiarazioneIntentoNonPresenteException;
import it.eurotn.panjea.magazzino.manager.intento.interfaces.DichiarazioneIntentoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.SediPagamentoManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(mappedName = "Panjea.DocumentoFatturazioneGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentoFatturazioneGenerator")
public class DocumentoFatturazioneGeneratorBean implements DocumentoFatturazioneGenerator {

    private class DataDocumentoAreeMagazzinoComparator implements Comparator<AreaMagazzinoFatturazione> {

        @Override
        public int compare(AreaMagazzinoFatturazione o1, AreaMagazzinoFatturazione o2) {

            if (o1.getCodiceTipoDocumento().compareTo(o2.getCodiceTipoDocumento()) != 0) {
                return o1.getCodiceTipoDocumento().compareTo(o2.getCodiceTipoDocumento());
            } else {
                if (o1.getDataDocumento().compareTo(o2.getDataDocumento()) != 0) {
                    return o1.getDataDocumento().compareTo(o2.getDataDocumento());
                } else {
                    return o1.getNumeroDocumento().compareTo(o2.getNumeroDocumento());
                }
            }
        }
    }

    private static Logger logger = Logger.getLogger(DocumentoFatturazioneGeneratorBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private RigheCollegateManager righeCollegateManager;

    @EJB
    private AreaIvaManager areaIvaManager;

    @EJB
    private ITotalizzatoriQueryExecutor totalizzatoriQueryExecutor;

    @EJB
    private StrategiaTotalizzazione strategiaTotalizzazione;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @EJB
    private SediPagamentoManager sediPagamentoManager;

    @EJB
    private SediMagazzinoManager sediMagazzinoManager;

    @EJB
    private AnagraficaTabelleManager anagraficaTabelleManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private RigheArticoloBuilderFactory generatoriRigaArticoloFactory;

    @EJB
    private NoteAutomaticheManager noteAutomaticheManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    @EJB
    private DichiarazioneIntentoManager dichiarazioneIntentoManager;

    @EJB
    private DepositiManager depositiManager;

    /**
     * Carica il deposito principale dell'azienda loggata.
     *
     * @return deposito
     */
    private Deposito caricaDepositoAziendaPrincipale() {
        return depositiManager.caricaDepositoPrincipale();
    }

    /**
     * Esegue un controllo sui documenti passati come parametro verificando se rientrano nella dichiarazione di intento
     * e se l'importo totale supera la soglia.
     *
     * @param areaMagazzino
     *            area magazzino
     * @param listRigheDaCollegare
     *            documenti da controllare
     * @return lista che contiene come primo valore <code>true</code> se almeno un documento rientra nella dichiarazione
     *         di intento e come secondo valore <code>true</code> se la soglia è superata
     */
    private List<Boolean> checkDocumentiPerDichiarazioneIntento(AreaMagazzino areaMagazzino,
            List<RigaMagazzino> listRigheDaCollegare) {

        StringBuilder idaree = new StringBuilder(listRigheDaCollegare.size() * 10);
        for (RigaMagazzino rigaMagazzino : listRigheDaCollegare) {
            idaree.append(rigaMagazzino.getAreaMagazzino().getId()).append(",");
        }

        // Devo eseguire una query e non sfruttare il fatto che quando creo le righe collagate le
        // ciclo perchè ho solo i dati delle righe e non delle aree
        StringBuilder sb = new StringBuilder(400);
        sb.append(
                "select sum(case am.tipologiaCodiceIvaAlternativo when 3 then 1 else 0 end),coalesce(sum(doc.importoInValutaAzienda),0) ");
        sb.append("from maga_area_magazzino am inner join docu_documenti doc on am.documento_id = doc.id ");
        sb.append("where am.id in (");
        sb.append(StringUtils.chop(idaree.toString()));
        sb.append(") ");
        @SuppressWarnings("unchecked")
        List<Object[]> result = panjeaDAO.prepareNativeQuery(sb.toString()).list();

        boolean areeDaCollegareConDicIntento = ((BigDecimal) result.get(0)[0]).intValue() > 0;
        BigDecimal totaleGenerale = (BigDecimal) result.get(0)[1];

        org.hibernate.Session session = (org.hibernate.Session) panjeaDAO.getEntityManager().getDelegate();
        session.enableFilter(MagazzinoSettings.ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE)
                .setParameter("paramDataRegistrazione", areaMagazzino.getDataRegistrazione());
        MagazzinoSettings magazzinoSettings = null;
        try {
            magazzinoSettings = magazzinoSettingsManager.caricaMagazzinoSettings();
        } finally {
            session.disableFilter(MagazzinoSettings.ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE);
        }

        if (areeDaCollegareConDicIntento && (magazzinoSettings == null
                || CollectionUtils.isEmpty(magazzinoSettings.getAddebitiDichiarazioneIntento()))) {
            throw new ArticoloAddebitoDichiarazioneIntentoNonPresenteException();
        }

        BigDecimal sogliaAddebito = magazzinoSettings.getSogliaAddebitoDichiarazioneInVigore() != null
                ? magazzinoSettings.getSogliaAddebitoDichiarazioneInVigore().getPrezzo() : BigDecimal.ZERO;

        List<Boolean> results = new ArrayList<Boolean>();
        results.add(areeDaCollegareConDicIntento);
        results.add(totaleGenerale.compareTo(sogliaAddebito) > 0);
        return results;
    }

    /**
     * Crea l'area di magazzino di destinazione.
     *
     * @param tipoAreaMagazzinoDestinazione
     *            tipo area di destinazione
     * @param dataDocumentoDestinazione
     *            data del documento di destinazione
     * @param sedeEntitaDestinazione
     *            sede entità del documento di destinazione
     * @param numeroDocumento
     *            numer del documento di destinazione
     * @param noteFatturazione
     *            note di fatturazione
     * @param entitaFatturazione
     *            entita del documento di fatturazione
     * @param codiceValutaFatturazione
     *            codice valuta
     * @param annoMovimentoFatturazione
     *            anno movimento
     * @param addebitaSpese
     *            indica se le spese verranno addebitate sul documento di destinazione
     * @param codiceAzienda
     *            codice azienda loggata
     * @param utente
     *            utente loggato
     * @param uuidContabilizzaizone
     *            uuid di contabilizzazione
     * @return area creata
     */
    private AreaMagazzino creaAreaMagazzinoDestinazione(TipoAreaMagazzino tipoAreaMagazzinoDestinazione,
            Date dataDocumentoDestinazione, SedeEntita sedeEntitaDestinazioneParam, int numeroDocumento,
            String noteFatturazione, EntitaLite entitaFatturazione, String codiceValutaFatturazione,
            int annoMovimentoFatturazione, boolean addebitaSpese, String codiceAzienda, String utente,
            String uuidContabilizzaizone) {

        SedeEntita sedeEntitaDestinazione = null;
        try {
            sedeEntitaDestinazione = sediEntitaManager.caricaSedeEntita(sedeEntitaDestinazioneParam.getId());
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento della sede.", e);
            throw new RuntimeException("Errore durante il caricamento della sede.", e);
        }

        Documento documento = new Documento();

        documento.getCodice().setCodice(Integer.toString(numeroDocumento).toString() + " " + utente);
        documento.setCodiceAzienda(codiceAzienda);
        documento.setDataDocumento(dataDocumentoDestinazione);
        documento.setEntita(entitaFatturazione);
        documento.setSedeEntita(sedeEntitaDestinazione);
        documento.setTipoDocumento(tipoAreaMagazzinoDestinazione.getTipoDocumento());
        Importo importoDoc = new Importo(codiceValutaFatturazione, BigDecimal.ONE);
        importoDoc.setImportoInValuta(BigDecimal.ZERO);
        importoDoc.setImportoInValutaAzienda(BigDecimal.ZERO);
        documento.setTotale(importoDoc);

        AreaMagazzino areaMagazzino = new AreaMagazzino();

        areaMagazzino.setDocumento(documento);
        areaMagazzino.setDataRegistrazione(dataDocumentoDestinazione);
        areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);
        areaMagazzino.setStatoSpedizione(StatoSpedizione.DA_VERIFICARE);
        areaMagazzino.setAnnoMovimento(annoMovimentoFatturazione);
        areaMagazzino.setTipoAreaMagazzino(tipoAreaMagazzinoDestinazione);
        areaMagazzino.setUUIDContabilizzazione(uuidContabilizzaizone);
        Deposito depositoPrinc = caricaDepositoAziendaPrincipale();
        DepositoLite depositoPrincipaleLite = new DepositoLite();
        depositoPrincipaleLite.setId(depositoPrinc.getId());
        depositoPrincipaleLite.setVersion(depositoPrinc.getVersion());
        areaMagazzino.setDepositoOrigine(depositoPrincipaleLite);
        if (sedeEntitaDestinazione.getZonaGeografica() != null
                && sedeEntitaDestinazione.getZonaGeografica().getId() != null) {
            areaMagazzino.setIdZonaGeografica(sedeEntitaDestinazione.getZonaGeografica().getId());
        }

        SedeMagazzino sedeMagazzino = sediMagazzinoManager.caricaSedeMagazzinoBySedeEntita(sedeEntitaDestinazione,
                false);
        areaMagazzino.assegnaCodiceIvaAlternativo(sedeMagazzino);

        DatiGenerazione datiGenerazione = creaDatiGenerazione(dataDocumentoDestinazione, noteFatturazione, utente);
        areaMagazzino.setDatiGenerazione(datiGenerazione);

        areaMagazzino.setAddebitoSpeseIncasso(addebitaSpese);
        return areaMagazzino;
    }

    /**
     * Crea i dati di fatturazione per la fatturazione in corso.
     *
     * @param dataDocumentoDestinazione
     *            dataDocumentoDestinazione
     * @param noteFatturazione
     *            noteFatturazione
     * @param utente
     *            utente
     * @return dati utili a raggruppare i documenti di destinazione
     */
    private DatiGenerazione creaDatiGenerazione(Date dataDocumentoDestinazione, String noteFatturazione,
            String utente) {
        // setto i dati di fatturazione del movimento
        DatiGenerazione datiGenerazione = new DatiGenerazione();
        datiGenerazione.setDataGenerazione(dataDocumentoDestinazione);
        datiGenerazione.setUtente(utente);
        datiGenerazione.setNote(noteFatturazione);
        datiGenerazione.setTipoGenerazione(TipoGenerazione.FATTURAZIONE);
        return datiGenerazione;
    }

    /**
     * Se sono state configurate delle note automatiche per il documento di destinazione, viene creata una riga nota
     * automatice.
     *
     * @param areaMagazzino
     *            area magazzino di riferimento
     */
    private void creaRigaNotaAutomatica(AreaMagazzino areaMagazzino, boolean aggiungiNoteDicIntento) {

        List<NotaAutomatica> noteAutomatiche = noteAutomaticheManager
                .caricaNoteAutomatiche(areaMagazzino.getDataRegistrazione(), areaMagazzino.getDocumento());
        List<NotaAnagrafica> noteAnagrafiche = anagraficaTabelleManager.caricaNoteAnagrafica();

        boolean noteAutomatichePresenti = noteAutomatiche != null && !noteAutomatiche.isEmpty();
        boolean noteDichiarazioneIntentoPresenti = false;

        StringBuilder sb = new StringBuilder("<html>");

        if (noteAutomatichePresenti) {
            for (NotaAutomatica nota : noteAutomatiche) {

                if (!sb.toString().equals("<html>")) {
                    sb.append("<BR>");
                }

                String notaStr = nota.getNotaElaborata(noteAnagrafiche);
                notaStr = notaStr.replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<head>", "")
                        .replaceAll("</head>", "").replaceAll("<body>", "").replaceAll("</body>", "");

                sb.append(notaStr);
            }
        }

        if (areaMagazzino.getDocumento().getSedeEntita() != null
                && areaMagazzino.getDocumento().getSedeEntita().getId() != null) {

            // verifico se devo aggiungere il testo della dichiarazione di intento
            if (aggiungiNoteDicIntento) {
                SedeMagazzino sedeMagazzino = sediMagazzinoManager
                        .caricaSedeMagazzinoBySedeEntita(areaMagazzino.getDocumento().getSedeEntita(), false);

                noteDichiarazioneIntentoPresenti = true;

                if (noteAutomatichePresenti) {
                    sb.append("<BR>");
                }
                sb.append(sedeMagazzino.getCodiceIvaAlternativo().getDescrizioneDocumenti());
                sb.append("<BR>");
                sb.append(sedeMagazzino.getDichiarazioneIntento().getTesto());
            }
        }

        sb.append("</html>");

        if (noteAutomatichePresenti || noteDichiarazioneIntentoPresenti) {
            rigaMagazzinoManager.creaRigaNoteAutomatica(areaMagazzino, sb.toString());
        }
    }

    /**
     * Genera il documento di fatturazione.
     *
     * @param areeMagazzino
     *            aree da fatturare
     * @param tipoAreaMagazzinoDestinazione
     *            tipo area di destinazione
     * @param dataDocumentoDestinazione
     *            data del documento di destinazione
     * @param sedeEntitaDestinazione
     *            sede entità del documento di destinazione
     * @param numeroDocumento
     *            numer del documento di destinazione
     * @param noteFatturazione
     *            note di fatturazione
     * @param entitaDestinazione
     *            entità del documento di destinazione
     * @param codiceValuta
     *            codice valuta
     * @param annoMovimento
     *            anno movimento
     * @param addebitaSpese
     *            indica se le spese verranno addebitate sul documento di destinazione
     * @param uuidContabilizzazione
     *            uuid di contabilizzazione
     * @param codicePagamento
     *            codice di pagamento
     * @param importoSpese
     *            spese
     */
    private void generaDocumento(List<AreaMagazzinoFatturazione> areeMagazzino,
            TipoAreaMagazzino tipoAreaMagazzinoDestinazione, Date dataDocumentoDestinazione,
            SedeEntita sedeEntitaDestinazione, Integer numeroDocumento, String noteFatturazione,
            EntitaLite entitaDestinazione, String codiceValuta, Integer annoMovimento, boolean addebitaSpese,
            String uuidContabilizzazione, CodicePagamento codicePagamento, BigDecimal importoSpese, String utente) {

        AreaMagazzino areaMagazzino = creaAreaMagazzinoDestinazione(tipoAreaMagazzinoDestinazione,
                dataDocumentoDestinazione, sedeEntitaDestinazione, numeroDocumento, noteFatturazione,
                entitaDestinazione, codiceValuta, annoMovimento, addebitaSpese, getCodiceAzienda(), utente,
                uuidContabilizzazione);

        // salva l'area magazzino
        try {
            areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, true);
        } catch (Exception e) {
            logger.error("--> Errore durante la creazione del documento di fatturazione", e);
            throw new RuntimeException("Errore durante la creazione del documento di fatturazione", e);
        }

        AreaRate areaRate = new AreaRate();
        areaRate.setDocumento(areaMagazzino.getDocumento());
        areaRate.setCodicePagamento(codicePagamento);
        areaRate.setSpeseIncasso(importoSpese);
        // salvo l'area partite
        areaRate = areaRateManager.salvaAreaRate(areaRate);

        // Collego le righe
        MagazzinoSettings settings = magazzinoSettingsManager.caricaMagazzinoSettings();
        RigaMagazzinoFatturazioneValidatorFactory validatorFactory = new RigaMagazzinoFatturazioneValidatorFactory();

        List<RigaMagazzino> listRigheDaCollegare = new ArrayList<RigaMagazzino>();
        for (AreaMagazzinoFatturazione areaOrigine : areeMagazzino) {
            for (RigaMagazzino rigaMagazzino : areaOrigine.getRigheMagazzino()) {
                if (validatorFactory.getValidator(rigaMagazzino).isValid(rigaMagazzino, settings)) {
                    listRigheDaCollegare.add(rigaMagazzino);
                }
            }
        }

        righeCollegateManager.collegaRigaMagazzino(listRigheDaCollegare, areaMagazzino, false);

        boolean addebitoDicIntento = areaMagazzino.getDocumento().getSedeEntita().getSedeMagazzino()
                .getDichiarazioneIntento().isAddebito();
        // la riga articolo di dichiarazione di intento la creo subito se ci sono aree da collegare
        // che la gestiscono e
        // se iltotale di tutte le aree supera la soglia.
        // Porcata immane. Sono costretto a farmi ritornare una lista perchè in questo modo eseguo
        // la query per il
        // controllo dei documenti da collegare una sola volta in quanto la generazione della riga
        // articolo e della riga
        // nota per la dichiarazione di intento ( vedi in fondo nel generaRigaNotaAutomatica ) hanno
        // controlli diversi.
        List<Boolean> checkDocumenti = checkDocumentiPerDichiarazioneIntento(areaMagazzino, listRigheDaCollegare);
        boolean areeDaCollegareConDicIntento = checkDocumenti.get(0);
        boolean sogliaSuperata = checkDocumenti.get(1);
        if (addebitoDicIntento && areeDaCollegareConDicIntento && sogliaSuperata) {
            dichiarazioneIntentoManager.generaRigheArticolo(areaMagazzino);
        }

        // aggiungo le righe conai e omaggio alla creazione del documento di fatturazione
        // provvisorio, alla conferma
        // della fatturazione non ricreo le righe conai ma mantengo quelle qui create
        List<IGeneratoreRigheArticolo> generatori = generatoriRigaArticoloFactory
                .creaGeneratoriRigheArticolo(EGeneratoreRiga.FATTURAZIONE);
        for (IGeneratoreRigheArticolo iGeneratoreRigheArticolo : generatori) {
            iGeneratoreRigheArticolo.generaRigheArticolo(areaMagazzino);
        }

        Totalizzatore totalizzatore = strategiaTotalizzazione
                .getTotalizzatore(areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());

        totalizzatoriQueryExecutor.setAreaDocumento(areaMagazzino);
        totalizzatoriQueryExecutor.setQueryString("RigaArticolo.caricaByTipo");

        AreaIva areaIva = areaIvaManager.generaAreaIvaDaMagazzino(areaMagazzino, areaRate);
        areaMagazzino.setDocumento(totalizzatore.totalizzaDocumento(areaMagazzino.getDocumento(),
                areaMagazzino.getTotaliArea(), totalizzatoriQueryExecutor, areaIva.getRigheIva()));
        areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.INFATTURAZIONE);

        try {
            areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, true);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio dell'area magazzino", e);
            throw new RuntimeException("Errore durante il salvataggio dell'area magazzino", e);
        }

        // creo la nota automatica
        creaRigaNotaAutomatica(areaMagazzino, areeDaCollegareConDicIntento);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 7200)
    @Override
    public void generaDocumentoFatturazione(List<AreaMagazzinoFatturazione> listAree,
            TipoAreaMagazzino tipoAreaMagazzinoDestinazione, Date dataDocumentoDestinazione,
            SedeEntita sedeEntitaDestinazione, EntitaLite entitaDestinazione, int numeroDocumento,
            String noteFatturazione, String uuidContabilizzazione, String utente)
                    throws RigaArticoloNonValidaException {

        logger.debug("--> Enter generaDocumentoFatturazione");
        Calendar calendarInizio = Calendar.getInstance();

        List<AreaMagazzinoFatturazione> areeMagazzino = ordinaAreePerData(listAree);

        BigDecimal importoSpese = BigDecimal.ZERO;
        boolean addebitaSpese = false;
        // Codice pagamento da associare al doc. di destinazione
        CodicePagamento codicePagamento = null;

        // Anche se non ho da aggiungere le spese devo avere il codice di
        // pagamento.
        // Seleziono sempre il primo perchè anche se sono raggruppate il codice
        // di pagamento è uguale per tutti
        codicePagamento = areeMagazzino.get(0).getCodicePagamento();
        if (codicePagamento != null && codicePagamento.isAbilitato()) {
            logger.error("--> non ho il codice pagamento per l'areaMagazzino con id "
                    + areeMagazzino.get(0).getAreaMagazzino().getId());
            throw new RuntimeException("--> non ho il codice pagamento per l'areaMagazzino con id "
                    + areeMagazzino.get(0).getAreaMagazzino().getId());
        }

        if (areeMagazzino.get(0).isRaggruppamentoBolle()) {
            // sto creando una fattura da una serie di bolle.
            // Ciclo sui documenti e controllo se almeno un documento ha delle
            // spese addebitate.
            // In questo caso devo addebitarle anche sulla fattura che sarà
            // generata prendendo l'importo dal codice
            // pagamento.
            // Tutti gli altri documenti nella lista hanno lo stesso codice
            // pagamento (perchè raggruppo per codice
            // pagamento)
            // Ciclo sui documenti per vedere se ho almeno un documento con le
            // spese di incasso abilitato.
            for (AreaMagazzinoFatturazione area : areeMagazzino) {
                if (area.isAddebitoSpeseIncasso()) {
                    addebitaSpese = true;
                    if (codicePagamento != null) {
                        importoSpese = codicePagamento.getImportoSpese();
                    }
                    break;
                }
            }
        } else {
            // se il documento non ha raggruppamento bolle prendo le sue spese
            // incasso
            if (areeMagazzino.get(0).isAddebitoSpeseIncasso()) {
                addebitaSpese = true;
                importoSpese = areeMagazzino.get(0).getSpeseIncasso();
            }
        }

        AreaMagazzinoFatturazione areaMagaRif = areeMagazzino.get(0);

        generaDocumento(areeMagazzino, tipoAreaMagazzinoDestinazione, dataDocumentoDestinazione, sedeEntitaDestinazione,
                numeroDocumento, noteFatturazione, entitaDestinazione, areaMagaRif.getCodiceValuta(),
                areaMagaRif.getAnnoMovimento(), addebitaSpese, uuidContabilizzazione, codicePagamento, importoSpese,
                utente);

        logger.debug("--> Exit generaDocumentoFatturazione. Tempo di esecuzione: "
                + (Calendar.getInstance().getTimeInMillis() - calendarInizio.getTimeInMillis()));
        logger.debug(
                "--------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 7200)
    @Override
    public void generaDocumentoRifatturazione(List<AreaMagazzinoFatturazione> listAree,
            TipoAreaMagazzino tipoAreaMagazzinoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione, String uuidContabilizzazione, String utente)
                    throws RigaArticoloNonValidaException, SedePerRifatturazioneAssenteException {

        List<AreaMagazzinoFatturazione> areeMagazzino = ordinaAreePerData(listAree);

        // controllo che la sede per rifatturazione sia presente su tutte le
        // aree
        verificaPresenzaSedePerRifatturazione(listAree, sedePerRifatturazione);

        SedeEntita sedeEntitaRifatturazione;
        try {
            sedeEntitaRifatturazione = sediEntitaManager
                    .caricaSedeEntita(sedePerRifatturazione.getSedeEntita().getId());
        } catch (Exception e) {
            logger.error("-->Errore durante in caricamento della sede entita per rifatturazione", e);
            throw new RuntimeException("Errore durante in caricamento della sede entita per rifatturazione", e);
        }
        SedePagamento sedePagamentoRifatturazione = sediPagamentoManager
                .caricaSedePagamentoBySedeEntita(sedeEntitaRifatturazione, false);

        CodicePagamento codicePagamento = sedePagamentoRifatturazione.getCodicePagamento();

        EntitaLite entitaLite = sedePerRifatturazione.getSedeEntita().getEntita();

        String valutaAzienda = aziendeManager.caricaAzienda().getCodiceValuta();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataDocumentoDestinazione);
        int annoFatturazione = calendar.get(Calendar.YEAR);

        generaDocumento(areeMagazzino, tipoAreaMagazzinoDestinazione, dataDocumentoDestinazione,
                sedeEntitaRifatturazione, -1, noteFatturazione, entitaLite, valutaAzienda, annoFatturazione,
                sedePerRifatturazione.isCalcoloSpese(), uuidContabilizzazione, codicePagamento,
                codicePagamento.getImportoSpese(), utente);
    }

    /**
     *
     * @return codice azienda loggata
     */
    private String getCodiceAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    /**
     * @param aree
     *            areemagazzino da ordinare
     * @return lista di aree ordinate
     */
    private List<AreaMagazzinoFatturazione> ordinaAreePerData(List<AreaMagazzinoFatturazione> aree) {
        // mettono in ordine i documenti per data documento, tipo documento e
        // numero documento
        Comparator<AreaMagazzinoFatturazione> areeComparator = new DataDocumentoAreeMagazzinoComparator();
        BasicEventList<AreaMagazzinoFatturazione> areeDaOrdinare = new BasicEventList<AreaMagazzinoFatturazione>();
        areeDaOrdinare.addAll(aree);
        SortedList<AreaMagazzinoFatturazione> areeOrdinate = new SortedList<AreaMagazzinoFatturazione>(areeDaOrdinare,
                areeComparator);
        return areeOrdinate;
    }

    /**
     * Verifica che tutte le {@link AreaMagazzinoLite} abbiano la sede per rifatturazione sulla sede entità.
     *
     * @param areeMagazzino
     *            lista di aree da controllare
     * @param sedePerRifatturazione
     *            sede per rifatturazione
     * @throws SedePerRifatturazioneAssenteException
     *             sollevata se almeno un'area è priva di sede per rifatturazione
     */
    private void verificaPresenzaSedePerRifatturazione(List<AreaMagazzinoFatturazione> areeMagazzino,
            SedeMagazzinoLite sedePerRifatturazione) throws SedePerRifatturazioneAssenteException {

        List<AreaMagazzino> areeMagazzinoSenzaSedeRifatturazione = new ArrayList<AreaMagazzino>();
        List<AreaMagazzino> areeMagazzinoConDiversaSedeRifatturazione = new ArrayList<AreaMagazzino>();

        for (AreaMagazzinoFatturazione areaMagazzino : areeMagazzino) {
            SedeEntita sedeEntita = new SedeEntita();
            sedeEntita.setId(areaMagazzino.getAreaMagazzino().getDocumento().getSedeEntita().getId());
            sedeEntita.setVersion(areaMagazzino.getAreaMagazzino().getDocumento().getSedeEntita().getVersion());
            SedeMagazzino sedeMagazzino = sediMagazzinoManager.caricaSedeMagazzinoBySedeEntita(sedeEntita, false);

            if (sedeMagazzino.getSedePerRifatturazione() == null) {
                areeMagazzinoSenzaSedeRifatturazione.add(areaMagazzino.getAreaMagazzino());
            } else {
                if (!sedeMagazzino.getSedePerRifatturazione().equals(sedePerRifatturazione)) {
                    areeMagazzinoConDiversaSedeRifatturazione.add(areaMagazzino.getAreaMagazzino());
                }
            }
        }

        if (!areeMagazzinoSenzaSedeRifatturazione.isEmpty() || !areeMagazzinoConDiversaSedeRifatturazione.isEmpty()) {
            throw new SedePerRifatturazioneAssenteException(sedePerRifatturazione, areeMagazzinoSenzaSedeRifatturazione,
                    areeMagazzinoConDiversaSedeRifatturazione);
        }
    }

}
