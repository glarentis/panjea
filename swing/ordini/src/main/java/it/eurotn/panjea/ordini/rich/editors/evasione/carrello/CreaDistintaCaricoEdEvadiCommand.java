package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.InputApplicationDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.GroupingList;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.exception.CodicePagamentoAssenteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoEvasioneAssenteException;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.manager.documento.evasione.DistintaCaricoManagerBean.ECaricamentoRigaDistinta;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.evasione.RigaComparator;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.AreaOrdineEvasioneDialog;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.AreaOrdinePM;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.AreeMagazzinoDatiAccompagnatoriDialog;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception.CodicePagamentoAssenteDialog;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception.CodicePagamentoEvasioneAssenteDialog;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception.EntitaSenzaTipoDocumentoEvasioneDialog;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.JecReports;
import it.eurotn.rich.report.ReportManager;

public class CreaDistintaCaricoEdEvadiCommand extends ApplicationWindowAwareCommand {

    private class DataEvasioneDialog extends InputApplicationDialog {

        /**
         * Costruttore.
         *
         */
        public DataEvasioneDialog() {
            super();
            setTitle("Inserimento della data di evasione");
            setInputLabelMessage("Data di evasione");
            setPreferredSize(new Dimension(300, 100));

            IDateEditor textFieldDateEditor = new JTextFieldDateEditor("dd/MM/yy", "##/##/##", '_');
            JDateChooser dateChooser = new JDateChooser(textFieldDateEditor);
            dateChooser.setDate(Calendar.getInstance().getTime());
            setInputField(dateChooser);
        }

        @Override
        protected boolean onFinish() {

            Date data = ((JDateChooser) getInputField()).getDate();

            if (data != null) {
                onFinish(data);
                return true;
            } else {
                return false;
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(CreaDistintaCaricoEdEvadiCommand.class);

    public static final String PARAM_RIGHE = "paramRighe";
    public static final String PARAM_EVADI_DISTINTE = "paramEvadiDistinte";

    public static final String COMMAND_ID = "creaDistintaCaricoEdEvadiCommand";

    private IOrdiniDocumentoBD ordiniDocumentoBD = null;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;

    private CarrelloEvasioneOrdiniTablePage carrelloEvasioneOrdiniTablePage;

    private AziendaCorrente aziendaCorrente;
    private ReportManager reportManager;

    /**
     *
     * Costruttore.
     *
     * @param ordiniDocumentoBD
     *            bd del documento.
     * @param carrelloPage
     *            pagina che gestisce il carrello di evasione
     */
    public CreaDistintaCaricoEdEvadiCommand(final IOrdiniDocumentoBD ordiniDocumentoBD,
            final CarrelloEvasioneOrdiniTablePage carrelloPage) {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        this.ordiniDocumentoBD = ordiniDocumentoBD;
        this.carrelloEvasioneOrdiniTablePage = carrelloPage;

        aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        this.reportManager = (ReportManager) Application.instance().getApplicationContext().getBean("reportManager");
        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    /**
     * Crea il report di stampa delle distinte di carico.
     *
     * @param distinte
     *            distinte da stampare
     * @return report creato
     */
    private JecReport creaDistinteReport(List<DistintaCarico> distinte) {
        JecReport reportDistinta = null;
        if (reportManager.reportExist("Ordine/Distinta", "distinta")) {

            List<Integer> idDistinte = new ArrayList<>();
            for (DistintaCarico distintaCarico : distinte) {
                idDistinte.add(distintaCarico.getId());
            }

            Map<Object, Object> parametri = new HashMap<>();
            parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
            parametri.put("descAzienda", aziendaCorrente.getDenominazione());
            parametri.put("azienda", aziendaCorrente.getCodice());
            parametri.put("aliasDataSource", "");
            parametri.put("idDistinte", idDistinte);
            parametri.put("caricamentoRighe", ECaricamentoRigaDistinta.SOLO_NON_FORZATE.ordinal());
            reportDistinta = new JecReport("Ordine/Distinta/distinta", parametri);
            reportDistinta.setReportName("Distinta di carico");
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Future<JecReport> runningReport = executor.submit(reportDistinta);
            try {
                reportDistinta = runningReport.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("-->errore nel generare il report delle distinte di carico", e);
                throw new GenericException(e);
            }
        }

        return reportDistinta;
    }

    /**
     *
     * @param righeDaEvadere
     *            righe selezionate per l'evasione
     * @return lista di areeOrdine raggruppate sulle righe
     */
    private List<AreaOrdinePM> creaOrdiniDaEvadere(List<RigaDistintaCarico> righeDaEvadere) {
        GroupingList<RigaDistintaCarico> ordiniDaEvadere = new GroupingList<>(GlazedLists.eventList(righeDaEvadere),
                new RigaComparator());
        List<AreaOrdinePM> ordini = new ArrayList<>();
        for (List<RigaDistintaCarico> righeOrdine : ordiniDaEvadere) {
            AreaOrdinePM areaOrdinePM = new AreaOrdinePM(righeOrdine);
            ordini.add(areaOrdinePM);
        }
        return ordini;
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("--> Enter doExecuteCommand");

        final InputApplicationDialog dialog = new DataEvasioneDialog();
        dialog.setFinishAction(new Closure() {

            @Override
            public Object call(Object obj) {

                dialog.setEnabled(false);
                dialog.getDialog().setVisible(false);

                Calendar tmpCalendar = Calendar.getInstance();
                Calendar evasioneCalendar = Calendar.getInstance();
                evasioneCalendar.setTime((Date) obj);
                evasioneCalendar.set(Calendar.HOUR_OF_DAY, tmpCalendar.get(Calendar.HOUR_OF_DAY));
                evasioneCalendar.set(Calendar.MINUTE, tmpCalendar.get(Calendar.MINUTE));
                evasioneCalendar.set(Calendar.SECOND, tmpCalendar.get(Calendar.SECOND));
                evasioneCalendar.set(Calendar.MILLISECOND, 0);
                Date dataEvasione = evasioneCalendar.getTime();

                @SuppressWarnings("unchecked")
                // Wrappo in una lista perchè il table model contiene una glazed list non serializzabile
                List<RigaDistintaCarico> righe = new ArrayList<>((List<RigaDistintaCarico>) getParameter(PARAM_RIGHE));

                // Creo le distinte
                List<DistintaCarico> distinte = ordiniDocumentoBD.creaDistintadiCarico(righe);

                // ATTENZIONE setto la quantità evasa sulle righe distinte perchè l'evasione usa quella evasa per
                // settare la quantità sulla riga magazzino creata. Non uso il metodo
                // rigaDistintaCarico.setStato(StatoRiga.SELEZIONATA); perchè nel caso delle righe forzate la quantità
                // evasa rimane a 0
                List<RigaDistintaCarico> righeDistinte = ordiniDocumentoBD.caricaRigheDistintaCarico(distinte);
                for (RigaDistintaCarico rigaDistintaCarico : righeDistinte) {
                    rigaDistintaCarico.setQtaEvasa(rigaDistintaCarico.getQtaDaEvadere());
                }

                righeDistinte = ordiniDocumentoBD.aggiornaRigheCaricoConDatiEvasione(righeDistinte);

                try {
                    List<AreaOrdinePM> ordiniDaEvadere = creaOrdiniDaEvadere(righeDistinte);

                    AreaOrdineEvasioneDialog dialog = new AreaOrdineEvasioneDialog(ordiniDaEvadere);
                    dialog.showDialog();
                    ordiniDaEvadere = dialog.getRigheOrdinePM();
                    if (ordiniDaEvadere == null) {
                        // se non ho ordini da evadere significa che l'utente ha annullato l'evasione e quindi devo
                        // cancellare le distinte precedentemente create.
                        Set<RigaDistintaCarico> righeSet = new TreeSet<>();
                        righeSet.addAll(righeDistinte);
                        ordiniDocumentoBD.cancellaRigheDistintaCarico(righeSet);
                        return null;
                    } else {
                        righeDistinte = new ArrayList<>();
                        for (AreaOrdinePM areaOrdinePM : ordiniDaEvadere) {
                            righeDistinte.addAll(areaOrdinePM.getRighe());
                        }

                        // Creo il report delle distinte create ora perchè dopo l'evasione le distinte vengono
                        // cancellate. Solo se l'evasione va a buon fine vado a stamparle.
                        JecReport reportDistinteCarico = creaDistinteReport(distinte);

                        // Evado le distinte.
                        ordiniDocumentoBD.evadiOrdini(righeDistinte, dataEvasione);

                        // se ci sono documenti che richiedono i dati accompagnatori apro il dialog per compilarli
                        openRichiestaDatiAccompagnatoriDialog(dataEvasione);

                        // Ad evasione effettuata apro il report delle distinte
                        if (reportDistinteCarico != null) {
                            reportDistinteCarico.postExecute(null).call(null);
                        }

                        // Apro l'editor della ricerca aree magazzino per
                        // visualizzare quelle create
                        openAndPreviewAreeMagazzinoEditor(dataEvasione);
                        carrelloEvasioneOrdiniTablePage.getSvuotaCarrelloCommand().execute();
                    }
                } catch (EntitaSenzaTipoDocumentoEvasioneException e) {
                    // cancello le distinte create e chiedo il tipo documento di
                    // evasione per quelli che ancora non
                    // ce l'hanno associato.
                    Set<RigaDistintaCarico> righeSet = new TreeSet<>();
                    righeSet.addAll(righeDistinte);
                    ordiniDocumentoBD.cancellaRigheDistintaCarico(righeSet);

                    PanjeaTitledPageApplicationDialog dialog = new EntitaSenzaTipoDocumentoEvasioneDialog(e);
                    dialog.showDialog();
                } catch (CodicePagamentoEvasioneAssenteException e) {
                    logger.trace(e);
                    PanjeaTitledPageApplicationDialog dialog = new CodicePagamentoEvasioneAssenteDialog(
                            e.getAreeOrdine());
                    dialog.showDialog();
                    LifecycleApplicationEvent event = new OpenEditorEvent(new DistintaCarico());
                    Application.instance().getApplicationContext().publishEvent(event);
                    carrelloEvasioneOrdiniTablePage.getSvuotaCarrelloCommand().execute();
                } catch (CodicePagamentoAssenteException e) {
                    logger.trace(e);
                    PanjeaTitledPageApplicationDialog dialog = new CodicePagamentoAssenteDialog(e.getAreeOrdine());
                    dialog.showDialog();
                    LifecycleApplicationEvent event = new OpenEditorEvent(new DistintaCarico());
                    Application.instance().getApplicationContext().publishEvent(event);
                    carrelloEvasioneOrdiniTablePage.getSvuotaCarrelloCommand().execute();
                } catch (ContabilizzazioneException e) {
                    LifecycleApplicationEvent event = new OpenEditorEvent(e);
                    Application.instance().getApplicationContext().publishEvent(event);
                    carrelloEvasioneOrdiniTablePage.getSvuotaCarrelloCommand().execute();
                } catch (Exception e) {
                    logger.error("-->errore nell'evasione documenti", e);

                    Set<RigaDistintaCarico> righeSet = new TreeSet<>();
                    righeSet.addAll(righeDistinte);
                    ordiniDocumentoBD.cancellaRigheDistintaCarico(righeSet);

                    PanjeaSwingUtil.checkAndThrowException(e);
                }

                return null;
            }
        });
        dialog.showDialog();
        logger.debug("--> Exit doExecuteCommand");
    }

    /**
     * Visualizza l'editor della ricerca aree magazzino per visualizzare le aree create dalla evasione.
     *
     * @param dataEvasione
     *            data dell'evasione
     */
    private void openAndPreviewAreeMagazzinoEditor(Date dataEvasione) {
        ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = new ParametriRicercaAreaMagazzino();

        parametriRicercaAreaMagazzino.setAnnoCompetenza(null);
        parametriRicercaAreaMagazzino.setDataGenerazione(dataEvasione);
        parametriRicercaAreaMagazzino.getDataRegistrazione().setDataFinale(dataEvasione);
        parametriRicercaAreaMagazzino.getTipiGenerazione().add(TipoGenerazione.EVASIONE);
        parametriRicercaAreaMagazzino.setEffettuaRicerca(true);

        List<AreaMagazzinoRicerca> areeMagazzino = magazzinoDocumentoBD
                .ricercaAreeMagazzino(parametriRicercaAreaMagazzino);
        parametriRicercaAreaMagazzino.setAreeMagazzino(areeMagazzino);

        // Apro i documenti
        LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaAreaMagazzino);
        Application.instance().getApplicationContext().publishEvent(event);

        // Creo una mappa con chiave id per recuperare l'area più velocemente.
        Map<Integer, AreaMagazzinoRicerca> areeMagazzinoMap = new HashMap<>();
        for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzino) {
            areeMagazzinoMap.put(areaMagazzinoRicerca.getIdAreaMagazzino(), areaMagazzinoRicerca);
        }

        // recupero le aree che devo stampare (ad esempio escludo aree con righe a peso
        // variabile...) ordinate
        // correttamente
        List<Integer> idAreeDaStampare = magazzinoDocumentoBD.caricaIdAreeMagazzinoPerStampaEvasione(areeMagazzino);

        List<AreaMagazzino> areeMagazzinoStampa = new ArrayList<>();
        for (Integer idAreaMagazzino : idAreeDaStampare) {
            AreaMagazzino areaMagazzinoLite = new AreaMagazzino();
            AreaMagazzinoRicerca areaMagazzinoRicerca = areeMagazzinoMap.get(idAreaMagazzino);
            if (areaMagazzinoRicerca != null) {
                areaMagazzinoLite.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
                areaMagazzinoLite.setTipoAreaMagazzino(areaMagazzinoRicerca.getTipoAreaMagazzino());
                areaMagazzinoLite.setDocumento(areaMagazzinoRicerca.getDocumento());
                areaMagazzinoLite.setStampaPrezzi(areaMagazzinoRicerca.isStampaPrezzi());
                areeMagazzinoStampa.add(areaMagazzinoLite);
            }
        }

        List<JecReport> reportsDocumenti = new ArrayList<>();

        for (AreaMagazzino areaDocumentoDaStampare : areeMagazzinoStampa) {
            JecReportDocumento reportDocumento = new JecReportDocumento(areaDocumentoDaStampare);
            reportDocumento.setStampaPrezzi(areaDocumentoDaStampare.isStampaPrezzi());
            reportsDocumenti.add(reportDocumento);
        }
        JecReports.stampaReports(reportsDocumenti, false, false, false);
    }

    private void openRichiestaDatiAccompagnatoriDialog(Date dataEvasione) {

        List<AreaMagazzinoRicerca> aree = magazzinoDocumentoBD
                .caricaAreeMagazzinoConRichiestaDatiAccompagnatori(dataEvasione);

        // se ho aree che richiedono i dati accompagnatori visualizzo il dialog
        if (!aree.isEmpty()) {
            AreeMagazzinoDatiAccompagnatoriDialog dialog = new AreeMagazzinoDatiAccompagnatoriDialog(aree);
            dialog.showDialog();
        }
    }
}
