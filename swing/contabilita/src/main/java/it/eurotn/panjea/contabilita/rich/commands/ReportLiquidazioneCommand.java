package it.eurotn.panjea.contabilita.rich.commands;

import java.awt.Image;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.image.ImageSource;
import org.springframework.rules.closure.Closure;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.pm.RigaLiquidazioneIvaPM;
import it.eurotn.panjea.contabilita.rich.pm.RigaPagamentoLiquidazionePM;
import it.eurotn.panjea.contabilita.rich.pm.RigaTotaliLiquidazionePM;
import it.eurotn.panjea.contabilita.rich.scriptlet.StampaLiquidazioneIvaScriptlet;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.FooterBean;
import it.eurotn.rich.report.HeaderBean;
import it.eurotn.rich.report.JecLocalReport;
import it.eurotn.util.PanjeaEJBUtil;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

public class ReportLiquidazioneCommand extends ActionCommand {

    private class CodiceCodiceIvaComparator implements Comparator<TotaliCodiceIvaDTO> {

        @Override
        public int compare(TotaliCodiceIvaDTO o1, TotaliCodiceIvaDTO o2) {
            return o1.getCodiceIva().compareTo(o2.getCodiceIva());
        }

    }

    private static class RegistroIvaComparator implements Comparator<RegistroIva>, Serializable {

        private static final long serialVersionUID = 7007803399172117858L;

        @Override
        public int compare(RegistroIva o1, RegistroIva o2) {
            return o1.getTipoRegistro().compareTo(o2.getTipoRegistro());
        }
    }

    private static final String DIALOG_NO_MOVIMENTI_TITLE = "liquidazioneIVADialogPage.dialogNoRigheLiquidazione.title";
    private static final String DIALOG_NO_MOVIMENTI_MESSAGE = "liquidazioneIVADialogPage.dialogNoRigheLiquidazione.message";
    private IContabilitaBD contabilitaBD = null;
    private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;
    private AziendaCorrente aziendaCorrente = null;
    private Integer anno;
    private String periodo;
    private Date dataInizio;
    private Date dataFine;
    private boolean definitivo;

    private GiornaleIva giornaleIva;

    // di pagine del report.Solo la
    // scriptlet ha getVariable

    private JRDefaultScriptlet scriptlet; // Utilizzata per recuperare il numero

    /**
     * Default constuctor.
     * 
     * @param id
     *            l'id del command
     */
    public ReportLiquidazioneCommand(final String id) {
        super(id);
    }

    /**
     * Prepara i dati per il report e lo esegue.
     * 
     * @param dataInizioLiq
     *            la data iniziale del periodo di cui visualizzare la liquidazione
     * @param dataFineLiq
     *            la data finale del periodo di cui visualizzare la liquidazione
     */
    public void creaReport(Date dataInizioLiq, Date dataFineLiq) {
        logger.debug("--> Enter creaReport");
        final LiquidazioneIvaDTO liquidazioneIvaDTO = contabilitaBD.caricaLiquidazioneIva(dataInizioLiq, dataFineLiq);
        if (liquidazioneIvaDTO.getTotaliRegistri() == null) {
            // Non ho movimenti
            MessageDialog dialog = new MessageDialog(getMessage(DIALOG_NO_MOVIMENTI_TITLE),
                    getMessage(DIALOG_NO_MOVIMENTI_MESSAGE));
            dialog.showDialog();
            return;
        }

        List<RigaLiquidazioneIvaPM> righeLiquidazione = creaRigheLiquidazioneIvaPM(liquidazioneIvaDTO);

        JecLocalReport jecReport = new JecLocalReport();
        jecReport.setReportName("Liquidazione IVA");
        jecReport.setXmlReportResource(new ClassPathResource(
                "/it/eurotn/panjea/contabilita/rich/reports/resources/StampaLiquidazioneIVA.jasper"));
        jecReport.setMessages("it.eurotn.panjea.messages.cont.reports.messages");

        JecLocalReport jecSubReportTotali = new JecLocalReport();
        jecSubReportTotali.setXmlReportResource(new ClassPathResource(
                "/it/eurotn/panjea/contabilita/rich/reports/resources/SubReportStampaLiquidazioneIVA.jasper"));
        jecReport.getSubReports().put("REPORT_SUBRIEPILOGO", jecSubReportTotali);

        JecLocalReport jecSubRiepilogoAnnuale = new JecLocalReport();
        jecSubRiepilogoAnnuale.setXmlReportResource(new ClassPathResource(
                "/it/eurotn/panjea/contabilita/rich/reports/resources/SubReportLiquidazioneAnnuale.jasper"));
        jecReport.getSubReports().put("REPORT_SUBRIEPILOGOANNUALE", jecSubRiepilogoAnnuale);

        JecLocalReport jecSubRiepilogoPagamenti = new JecLocalReport();
        jecSubRiepilogoPagamenti.setXmlReportResource(new ClassPathResource(
                "/it/eurotn/panjea/contabilita/rich/reports/resources/SubReportPagamentiLiquidazioneAnnuale.jasper"));
        jecReport.getSubReports().put("REPORT_SUBRIEPILOGOPAGAMENTI", jecSubRiepilogoPagamenti);

        jecReport.setDataReport(righeLiquidazione);
        HeaderBean headerBean = new HeaderBean();
        headerBean.setCodiceAzienda(aziendaCorrente.getDenominazione());
        headerBean.setUtenteCorrente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
        FooterBean footerBean = new FooterBean();
        jecReport.setDataHeader(headerBean);
        jecReport.setDataFooter(footerBean);
        jecReport.getReportParameters().put("subReportTotaliRegistriList",
                createRigheTotaliLiquidazionePM(liquidazioneIvaDTO));
        jecReport.getReportParameters().put("periodoLiquidazione", " " + periodo);
        jecReport.getReportParameters().put("annoLiquidazioneIva", " " + anno.toString());
        jecReport.getReportParameters().put("aziendaCorrente", aziendaCorrente);
        RegistroIva registroIva = getRegistroIvaRiepilogo();
        if (registroIva != null) {
            jecReport.getReportParameters().put("registroRiepilogativo", registroIva.getDescrizione());
        }

        Map<String, BigDecimal> totali = liquidazioneIvaDTO.getTotaliPerido();
        BigDecimal totale = totali.get(LiquidazioneIvaDTO.RISULTATO_TOTALE_CON_ACCONTO_KEY);
        BigDecimal periodoPrecedente = totali.get(LiquidazioneIvaDTO.RISULTATO_PRECEDENTE_KEY);
        BigDecimal accontoDicembre = totali.get(LiquidazioneIvaDTO.RISULTATO_ACCONTO_DICEMBRE);

        if (liquidazioneIvaDTO.getPercTrimestraleValore().compareTo(BigDecimal.ZERO) != 0) {
            jecReport.getReportParameters().put("percTrimestraleValore", liquidazioneIvaDTO.getPercTrimestraleValore());
            jecReport.getReportParameters().put("percTrimestraleImporto",
                    liquidazioneIvaDTO.getPercTrimestraleImporto());
        } else {
            jecReport.getReportParameters().put("percTrimestraleValore", null);
            jecReport.getReportParameters().put("percTrimestraleImporto", null);
        }
        jecReport.getReportParameters().put("risultatoLiq", totale);
        jecReport.getReportParameters().put("periodoPrecedente", periodoPrecedente);
        jecReport.getReportParameters().put("accontoDicembre", accontoDicembre);

        jecReport.getReportParameters().put("isAnnuale", liquidazioneIvaDTO.isAnnuale());
        jecReport.getReportParameters().put("beniStrumentaliTotale", liquidazioneIvaDTO.getBeniStrumentaliTotale());
        jecReport.getReportParameters().put("volumeAffariAziende", liquidazioneIvaDTO.getVolumeAffariAziende());
        jecReport.getReportParameters().put("volumeAffariPrivati", liquidazioneIvaDTO.getVolumeAffariPrivati());
        jecReport.getReportParameters().put("volumeAffariTotale", liquidazioneIvaDTO.getVolumeAffariTotale());
        jecReport.getReportParameters().put("subReportTotaliAnnualiList",
                creaRighePagamentoLiquidazione(liquidazioneIvaDTO));
        jecReport.getReportParameters().put("subReportDettaglioPagamentiList",
                liquidazioneIvaDTO.getRigheVolumeAffariPrivati());

        Integer ultimoNumeroPagina = 0;
        if (definitivo) {
            scriptlet = new StampaLiquidazioneIvaScriptlet();
            jecReport.getReportParameters().put("REPORT_SCRIPTLET", scriptlet);

            // rimuovo l'immagine di background dato che rimane nel report singleton una volta settata e quindi anche se
            // eseguo la stampa definitiva se prima ne ho fatta una provvisoria mi ritrovo con il background facsimile
            jecReport.getReportParameters().remove("imageBackGround");

            // Recupero il registro precedente
            GiornaleIva giornaleIvaPrecedente = contabilitaBD.caricaGiornaleIvaPrecedente(giornaleIva);
            if (giornaleIvaPrecedente != null && (giornaleIva.getMese() > 1 || giornaleIva.getMese() == -1)) {
                ultimoNumeroPagina = giornaleIvaPrecedente.getNumeroPagina();
            }

        } else {
            ImageSource imageSource = (ImageSource) Application.services()
                    .getService(org.springframework.richclient.image.ImageSource.class);
            Image facsimile = imageSource.getImage("stampaGiornale.background.image");
            jecReport.getReportParameters().put("imageBackGround", facsimile);
        }

        jecReport.getReportParameters().put("ultimoNumeroPagina", ultimoNumeroPagina);

        jecReport.execute(new Closure() {

            @Override
            public Object call(Object arg0) {
                JecLocalReport reportResult = (JecLocalReport) arg0;
                if (definitivo) {
                    Integer numPagine = new Integer(0);
                    try {
                        numPagine = (Integer) scriptlet.getVariableValue("PAGE_NUMBER");
                    } catch (JRScriptletException e) {
                        throw new RuntimeException("Errore nella stampa del report");
                    }
                    // Salvo il giornale Iva
                    ContabilitaSettings contabilitaSettings = contabilitaAnagraficaBD.caricaContabilitaSettings();
                    giornaleIva.setMinimaleIVA(contabilitaSettings.getMinimaleIVA());
                    giornaleIva.setPercTrimestraleImporto(liquidazioneIvaDTO.getPercTrimestraleImporto());
                    giornaleIva.setPercTrimestraleValore(liquidazioneIvaDTO.getPercTrimestraleValore());
                    int ultimoNumeroPagina = (Integer) ObjectUtils
                            .defaultIfNull(reportResult.getReportParameters().get("ultimoNumeroPagina"), 0);
                    giornaleIva.setNumeroPagina(ultimoNumeroPagina + numPagine);
                    contabilitaBD.salvaGiornaleIva(giornaleIva);
                }
                return null;
            }
        });

        logger.debug("--> Exit creaReport");
    }

    /**
     * Prepara le righe di dettaglio dei codici iva e relativi valori della liquidazione.
     * 
     * @param liquidazioneIvaDTO
     *            la liquidazione di cui presentare i dati
     * @return List<RigaLiquidazioneIvaPM>
     */
    private List<RigaLiquidazioneIvaPM> creaRigheLiquidazioneIvaPM(LiquidazioneIvaDTO liquidazioneIvaDTO) {

        List<RigaLiquidazioneIvaPM> listRighePM = new ArrayList<RigaLiquidazioneIvaPM>();

        Map<RegistroIva, List<TotaliCodiceIvaDTO>> totali = liquidazioneIvaDTO.getTotaliRegistri();
        Set<RegistroIva> chiavi = null;
        if (totali == null) {
            chiavi = new HashSet<RegistroIva>();
        } else {
            chiavi = liquidazioneIvaDTO.getTotaliRegistri().keySet();
        }

        List<RegistroIva> listChiavi = new ArrayList<RegistroIva>();
        listChiavi.addAll(chiavi);

        Collections.sort(listChiavi, new RegistroIvaComparator());

        for (RegistroIva registroIva : listChiavi) {

            switch (registroIva.getTipoRegistro()) {
            case ACQUISTO:
                List<TotaliCodiceIvaDTO> listTotaliRegistri = liquidazioneIvaDTO.getTotaliRegistri().get(registroIva);
                List<TotaliCodiceIvaDTO> listTotaliIvaSospesaAcquisto = null;
                if (liquidazioneIvaDTO.getTotaliIvaSospesa() != null) {
                    listTotaliIvaSospesaAcquisto = liquidazioneIvaDTO.getTotaliIvaSospesa().get(registroIva);
                }
                if (listTotaliRegistri != null) {
                    if (listTotaliIvaSospesaAcquisto != null) {
                        listTotaliRegistri.addAll(listTotaliIvaSospesaAcquisto);
                        Collections.sort(listTotaliRegistri, new CodiceCodiceIvaComparator());
                    }
                    for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : listTotaliRegistri) {

                        RigaLiquidazioneIvaPM ivaPM = new RigaLiquidazioneIvaPM(registroIva, "ACQUISTO",
                                totaliCodiceIvaDTO);
                        listRighePM.add(ivaPM);
                    }
                }
                break;
            case VENDITA:
                List<TotaliCodiceIvaDTO> listTotaliRegistriVendite = liquidazioneIvaDTO.getTotaliRegistri()
                        .get(registroIva);
                List<TotaliCodiceIvaDTO> listTotaliIvaSospesaVendite = null;
                if (liquidazioneIvaDTO.getTotaliIvaSospesa() != null) {
                    listTotaliIvaSospesaVendite = liquidazioneIvaDTO.getTotaliIvaSospesa().get(registroIva);
                }
                if (listTotaliRegistriVendite != null) {
                    if (listTotaliIvaSospesaVendite != null) {
                        listTotaliRegistriVendite.addAll(listTotaliIvaSospesaVendite);
                        Collections.sort(listTotaliRegistriVendite, new CodiceCodiceIvaComparator());
                    }
                    for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : listTotaliRegistriVendite) {

                        RigaLiquidazioneIvaPM ivaPM = new RigaLiquidazioneIvaPM(registroIva, "VENDITA",
                                totaliCodiceIvaDTO);
                        listRighePM.add(ivaPM);
                    }
                }
                break;
            case CORRISPETTIVO:
                Map<RegistroIva, List<TotaliCodiceIvaDTO>> perCorrispettivo = liquidazioneIvaDTO.getVentilazioniIva();
                if (perCorrispettivo == null) {
                    // ho il registroIva (anagrafica) di tipo corrispettivo, ma
                    // non ho righe iva legate ad un documento
                    // collegato al registro iva corrispettivo stesso.
                    break;
                }
                List<TotaliCodiceIvaDTO> listRigaVentilazione = liquidazioneIvaDTO.getVentilazioniIva()
                        .get(registroIva);

                if (listRigaVentilazione != null) {
                    for (TotaliCodiceIvaDTO rigaVentilazioneIva : listRigaVentilazione) {
                        RigaLiquidazioneIvaPM ivaPM = new RigaLiquidazioneIvaPM(registroIva, "VENTILAZIONE",
                                rigaVentilazioneIva);
                        listRighePM.add(ivaPM);
                    }
                }
                List<TotaliCodiceIvaDTO> listTotaliRegistriCorr = liquidazioneIvaDTO.getTotaliRegistri()
                        .get(registroIva);
                if (listTotaliRegistriCorr != null) {
                    for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : listTotaliRegistriCorr) {
                        RigaLiquidazioneIvaPM ivaPM = new RigaLiquidazioneIvaPM(registroIva, "CORRISPETTIVO",
                                totaliCodiceIvaDTO);
                        listRighePM.add(ivaPM);
                    }
                }
                break;
            }
        }
        return listRighePM;
    }

    /**
     * Crea le righe per i totali dei documenti di pagamento della liquidazione raggruppati per tipo documento.
     * 
     * @param liquidazioneIvaDTO
     *            la liquidazione da cui prendere i totali pagamenti liquidazione
     * @return List<RigaPagamentoLiquidazionePM>
     */
    private List<RigaPagamentoLiquidazionePM> creaRighePagamentoLiquidazione(LiquidazioneIvaDTO liquidazioneIvaDTO) {
        List<RigaPagamentoLiquidazionePM> totaliList = new ArrayList<RigaPagamentoLiquidazionePM>();

        Map<TipoDocumento, BigDecimal> totali = liquidazioneIvaDTO.getTotaliPagamento();
        if (totali != null) {
            Set<TipoDocumento> chiaviTipiDocumento = totali.keySet();

            for (TipoDocumento tipoDocumento : chiaviTipiDocumento) {
                BigDecimal totale = totali.get(tipoDocumento);
                RigaPagamentoLiquidazionePM riga = new RigaPagamentoLiquidazionePM(tipoDocumento, totale);
                totaliList.add(riga);
            }
        }
        return totaliList;
    }

    /**
     * Prepara i dati per riepilogare i totali della liquidazione.
     * 
     * @param liquidazioneIvaDTO
     *            la liquidazione di cui presentare i totali
     * @return List<RigaTotaliLiquidazionePM>
     */
    private List<RigaTotaliLiquidazionePM> createRigheTotaliLiquidazionePM(LiquidazioneIvaDTO liquidazioneIvaDTO) {

        List<RigaTotaliLiquidazionePM> list = new ArrayList<RigaTotaliLiquidazionePM>();

        Set<RegistroIva> chiavi = liquidazioneIvaDTO.getTotali().keySet();
        Set<RegistroIva> chiaviSplitPayment = liquidazioneIvaDTO.getTotaliSplitPayment().keySet();

        List<RegistroIva> listChiavi = new ArrayList<RegistroIva>();
        listChiavi.addAll(chiavi);

        Collections.sort(listChiavi, new RegistroIvaComparator());

        // Riordino per mettere i registri pro-rata nella posizione corretta
        List<RegistroIva> registriProRata = new ArrayList<RegistroIva>();
        List<RegistroIva> registri = new ArrayList<RegistroIva>();
        for (RegistroIva registroIva : listChiavi) {
            if (registroIva.isProRata()) {
                registriProRata.add(registroIva);
            } else {
                registri.add(registroIva);
            }
        }

        listChiavi.clear();
        for (RegistroIva registroIva : registri) {
            listChiavi.add(registroIva);
            for (RegistroIva registroProRata : registriProRata) {
                if (registroProRata.getDescrizione().equals(registroIva.getDescrizioneRegistroProRata())) {
                    listChiavi.add(registroProRata);
                }
            }
        }

        for (RegistroIva registroIva : listChiavi) {
            BigDecimal tot = liquidazioneIvaDTO.getTotali().get(registroIva);
            if (tot.compareTo(BigDecimal.ZERO) != 0) {
                RigaTotaliLiquidazionePM rigaTotaliLiquidazionePM = new RigaTotaliLiquidazionePM(registroIva, tot);
                list.add(rigaTotaliLiquidazionePM);
            }
        }

        listChiavi.clear();
        for (RegistroIva registroIva : chiaviSplitPayment) {
            BigDecimal tot = liquidazioneIvaDTO.getTotaliSplitPayment().get(registroIva);
            if (tot.compareTo(BigDecimal.ZERO) != 0) {

                RegistroIva registro = PanjeaEJBUtil.cloneObject(registroIva);
                registro.setDescrizione(registro.getDescrizione().concat(" di cui split payment"));
                RigaTotaliLiquidazionePM rigaTotaliLiquidazionePM = new RigaTotaliLiquidazionePM(registro,
                        tot.negate());
                list.add(rigaTotaliLiquidazionePM);
            }
        }

        Collections.sort(list, new Comparator<RigaTotaliLiquidazionePM>() {

            @Override
            public int compare(RigaTotaliLiquidazionePM o1, RigaTotaliLiquidazionePM o2) {
                TipoRegistro tipoRegistro1 = o1.getRegistroIva().getTipoRegistro();
                TipoRegistro tipoRegistro2 = o2.getRegistroIva().getTipoRegistro();
                int compareTo = tipoRegistro1.compareTo(tipoRegistro2);
                if (compareTo == 0) {
                    return o1.getRegistroIva().getDescrizione().compareTo(o2.getRegistroIva().getDescrizione());
                }
                return compareTo;
            }

        });

        return list;
    }

    @Override
    protected void doExecuteCommand() {
        creaReport(dataInizio, dataFine);
    }

    /**
     * @return the anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return the aziendaCorrente
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    /**
     * @return the contabilitaBD
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
    }

    /**
     * @return the anno
     */
    public Date getDataFine() {
        return dataFine;
    }

    /**
     * @return the dataInizio
     */
    public Date getDataInizio() {
        return dataInizio;
    }

    /**
     * @return the giornaleIva
     */
    public GiornaleIva getGiornaleIva() {
        return giornaleIva;
    }

    /**
     * @param code
     *            la chiave di cui richiedere il valore
     * @return the string message
     */
    private String getMessage(String code) {
        return ((MessageSource) ApplicationServicesLocator.services().getService(MessageSource.class)).getMessage(code,
                new Object[] {}, Locale.getDefault());
    }

    /**
     * @return the periodo
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * Carica i conti base definiti e restituisce quello di tipo riepilogo.
     * 
     * @return conto base
     */
    private RegistroIva getRegistroIvaRiepilogo() {

        List<RegistroIva> registriIva = contabilitaAnagraficaBD.caricaRegistriIva("nome", null);

        for (RegistroIva registroIva : registriIva) {
            if (registroIva.getTipoRegistro() == TipoRegistro.RIEPILOGATIVO) {
                return registroIva;
            }
        }

        return null;
    }

    /**
     * @return the definitivo
     */
    public boolean isDefinitivo() {
        return definitivo;
    }

    /**
     * @param anno
     *            the anno to set
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param contabilitaAnagraficaBD
     *            the contabilitaAnagraficaBD to set
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    /**
     * @param dataFine
     *            the dataFine to set
     */
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * @param dataInizio
     *            the dataInizio to set
     */
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     * @param definitivo
     *            the definitivo to set
     */
    public void setDefinitivo(boolean definitivo) {
        this.definitivo = definitivo;
    }

    /**
     * @param giornaleIva
     *            the giornaleIva to set
     */
    public void setGiornaleIva(GiornaleIva giornaleIva) {
        this.giornaleIva = giornaleIva;
    }

    /**
     * @param periodo
     *            the periodo to set
     */
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
