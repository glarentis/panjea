package it.eurotn.panjea.tesoreria.solleciti.editors;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.commons.io.FilenameUtils;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.exception.ReportNonTrovatoException;
import it.eurotn.rich.services.IMailService;
import it.eurotn.rich.services.MailLocalService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

/**
 *
 * @author angelo.
 *
 */
public class StampaSollecitiDialog extends ApplicationDialog {

    /**
     * command per creare i solleciti.
     *
     * @author angelo
     */
    public class StampaSollecitiCommand extends ActionCommand {

        public static final String COMMAND_ID = "stampaSollecitiCommand";

        /**
         * Genera i soleciti.
         */
        public StampaSollecitiCommand() {
            super(COMMAND_ID);
            this.setSecurityControllerId(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            if (solleciti == null || solleciti.isEmpty()) {
                return;
            }

            List<Sollecito> sollecitiSalvati = tesoreriaBD.salvaSolleciti(solleciti);
            setSolleciti(sollecitiSalvati);

            Map<Object, Object> parametri;
            JecReport jecReport = null;
            try {
                for (Sollecito sollecito : solleciti) {

                    if (!sollecito.isEmail() && !sollecito.isStampa()) {
                        continue;
                    }

                    parametri = new HashMap<Object, Object>();
                    parametri.put("idSollecito", sollecito.getId());
                    parametri.put("azienda", aziendaCorrente.getCodice());

                    if (sollecito.isStampa()) {
                        String path = TemplateSolleciti.REPORT_PATH + "/" + sollecito.getTemplate().getReportName();
                        JecReport jecReportTmp = new JecReport(path, parametri);
                        jecReportTmp.setReportName("Sollecito");

                        if (jecReport == null) {
                            jecReport = jecReportTmp;
                        } else {
                            jecReport.addReport(jecReportTmp);
                        }
                    }

                    if (sollecito.isEmail()) {
                        try {
                            String path = TemplateSolleciti.REPORT_PATH + "/"
                                    + sollecito.getTemplate().getReportNameMail();
                            final JecReport jecReportMail = new JecReport(path, parametri);
                            final Sollecito s = sollecito;
                            jecReportMail.setReportName("Sollecito");
                            jecReportMail.generaReport(new Closure() {

                                @Override
                                public Object call(Object arg0) {
                                    try {
                                        java.io.File reportPdf;
                                        reportPdf = java.io.File.createTempFile("report", ".pdf");
                                        JasperExportManager.exportReportToPdfFile(jecReportMail.getJrPrint(),
                                                reportPdf.getPath());
                                        generaEmail(s, reportPdf);
                                    } catch (IOException e) {
                                        logger.error("--> errore durante la creazione dell'email per il sollecito ", e);
                                    } catch (JRException e) {
                                        logger.error("--> errore durante la creazione dell'email per il sollecito ", e);
                                    }
                                    return null;
                                }
                            });
                        } catch (Exception e) {
                            logger.error("--> errore durante la creazione dell'email per il sollecito ", e);
                        }
                    }
                }
            } catch (Exception error) {
                Message message = null;
                if (error instanceof ReportNonTrovatoException) {
                    ReportNonTrovatoException exception = (ReportNonTrovatoException) error;
                    message = new DefaultMessage("Impossibile trovare il report con il path " + exception.getMessage(),
                            Severity.ERROR);
                } else {
                    message = new DefaultMessage("Stampa del documento " + getReportName() + " non riuscita",
                            Severity.ERROR);
                }
                logger.error("-->errore nell'eseguire il report", error);
                new MessageAlert(message).showAlert();
            } finally {
                StampaSollecitiDialog.this.getFinishCommand().execute();
                if (jecReport != null) {
                    jecReport.execute();
                }
            }
        }
    }

    public static final String PAGE_ID = "stampaSollecitiDialog";

    private JideTableWidget<Sollecito> sollecitiTable;

    private AziendaCorrente aziendaCorrente;

    private IAnagraficaBD anagraficaBD;
    private ITesoreriaBD tesoreriaBD;

    private List<Sollecito> solleciti;

    private StampaSollecitiCommand stampaSollecitiCommand;

    /**
     * default costructor .
     *
     */
    public StampaSollecitiDialog() {
        super(RcpSupport.getMessage(PAGE_ID + ".title"), null);

        sollecitiTable = new JideTableWidget<Sollecito>("sollecitoDialog", new SollecitoReportTableModel());
        sollecitiTable.setTableType(TableType.GROUP);
        sollecitiTable.setAggregatedColumns(new String[] { "cliente" });
        sollecitiTable.getTable().addMouseListener(new SelectRowListener(sollecitiTable));
    }

    @Override
    protected JComponent createDialogContentPane() {
        JComponent contentPanel = getComponentFactory().createPanel(new BorderLayout());
        contentPanel.add(sollecitiTable.getComponent(), BorderLayout.CENTER);
        return contentPanel;
    }

    /**
     * prende il file del report e il solecito e chiama send mail.
     *
     * @param sollecito
     *            sollecito
     * @param file
     *            allegatto
     */
    private void generaEmail(Sollecito sollecito, java.io.File file) {

        IMailService mailLocalService = RcpSupport.getBean(MailLocalService.BEAN_ID);
        String mailDestinazione = new String();
        Rata rata = sollecito.getRigheSollecito().iterator().next().getRata();
        Entita entita = anagraficaBD.caricaEntita(rata.getAreaRate().getDocumento().getEntita(), false);
        mailDestinazione = entita.getAnagrafica().getSedeAnagrafica().getIndirizzoMail();
        ParametriMail mail = new ParametriMail();
        mail.setTesto(sollecito.getTestoMail());
        mail.setDatiMail(mailLocalService.caricaUtenteCorrente().getDatiMailPredefiniti());

        Destinatario d = new Destinatario();
        d.setEmail(mailDestinazione);
        d.setEntita(entita.getEntitaLite());
        mail.getDestinatari().add(d);
        if (file != null) {
            mail.addAttachments(file.getPath(), "Sollecito." + FilenameUtils.getExtension(file.getName()));
        }
        mail.setOggetto(sollecito.getOggettoMail());
        mail.setNota(sollecito.getNota());
        mailLocalService.send(mail, true, false);
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return (new AbstractCommand[] { getStampaSollecitiCommand(), getCancelCommand() });
    }

    /**
     * nome del report.
     *
     * @return nome del report.
     */
    protected String getReportName() {
        return "SOLLECITO NUMERO ";
    }

    /**
     * @return the stampaSollecitiCommand
     */
    public StampaSollecitiCommand getStampaSollecitiCommand() {
        if (stampaSollecitiCommand == null) {
            stampaSollecitiCommand = new StampaSollecitiCommand();
        }

        return stampaSollecitiCommand;
    }

    /**
     * @return the tesoreriaBD
     */
    public ITesoreriaBD getTesoreriaBD() {
        return tesoreriaBD;
    }

    @Override
    protected boolean onFinish() {
        return true;
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param solleciti
     *            the solleciti to set
     */
    public void setSolleciti(List<Sollecito> solleciti) {
        this.solleciti = solleciti;
        sollecitiTable.setRows(solleciti);
    }

    /**
     * @param tesoreriaBD
     *            the tesoreriaBD to set
     */
    public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
        this.tesoreriaBD = tesoreriaBD;
    }

}