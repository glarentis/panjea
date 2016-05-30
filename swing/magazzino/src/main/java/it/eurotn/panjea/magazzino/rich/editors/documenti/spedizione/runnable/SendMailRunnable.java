package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager.TipoLayoutPrefefinito;
import it.eurotn.rich.control.mail.IPanjeaMailClient;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.JecReports;
import it.eurotn.rich.report.editor.export.ExportToPDFCommand;
import it.eurotn.rich.report.result.JecReportResult;

public class SendMailRunnable extends SpedizioneRunnable {

    private static final long serialVersionUID = -5668484610507288946L;

    private static final Logger LOGGER = Logger.getLogger(SendMailRunnable.class);
    private TemplateSpedizioneMovimenti templateSpedizione;

    private IPanjeaMailClient panjeaMailClient;

    /**
     *
     * Costruttore.
     *
     * @param movimento
     *            movimento
     * @param tableWidget
     *            tabella dei movimenti
     * @param templateSpedizioneMovimenti
     *            template di spedizione mail
     */
    public SendMailRunnable(final MovimentoSpedizioneDTO movimento,
            final JideTableWidget<MovimentoSpedizioneDTO> tableWidget,
            final TemplateSpedizioneMovimenti templateSpedizioneMovimenti) {
        super(movimento, tableWidget, false);
        this.templateSpedizione = templateSpedizioneMovimenti;

        this.panjeaMailClient = RcpSupport.getBean(IPanjeaMailClient.BEAN_ID);
    }

    private ParametriMail creaParametriMail() {
        ParametriMail parametri = new ParametriMail();
        parametri.setDatiMail(getMovimento().getDatiMailUtente());

        Destinatario destinatario = new Destinatario();
        destinatario.setEmail(getMovimento().getIndirizzoMailMovimento());
        destinatario.setEntita(getMovimento().getAreaDocumento().getDocumento().getEntita());
        Set<Destinatario> destinatari = new TreeSet<Destinatario>();
        destinatari.add(destinatario);
        parametri.setDestinatari(destinatari);

        String nomeAllegato = ObjectConverterManager.toString(getMovimento().getAreaDocumento().getDocumento(),
                Documento.class, null);
        // temporaneo ( prova per capire come mai non funziona in dafra)
        if (StringUtils.isBlank(nomeAllegato) || nomeAllegato.contains("[")) {
            nomeAllegato = "documento";
        }
        parametri.setNomeAllegato(nomeAllegato.replace(" ", "").replace("/", "-").replace("\\", "-"));

        // oggetto e testo lo ricavo dal template
        parametri.setOggetto(templateSpedizione.replaceVariables(templateSpedizione.getOggetto(),
                getMovimento().getAreaDocumento()));
        parametri.setTesto(
                templateSpedizione.replaceVariables(templateSpedizione.getTesto(), getMovimento().getAreaDocumento()));

        return parametri;
    }

    @Override
    protected EsitoSpedizione doRun() {
        ParametriMail parametri = creaParametriMail();

        List<JecReport> reportsDocumenti = new ArrayList<JecReport>();
        JecReport reportDocumento = new JecReportDocumento(getMovimento().getAreaDocumento(),
                TipoLayoutPrefefinito.MAIL);
        reportsDocumenti.add(reportDocumento);

        boolean spedizioneOk = false;
        StringBuilder sbEsito = new StringBuilder(500);
        try {
            JecReportResult reportResult = JecReports.generaReports(reportsDocumenti);

            if (!reportResult.getReportErrors().hasErrors()) {

                Exception mailException = null;
                int tentativoInvio = 0;
                do {
                    try {
                        tentativoInvio++;
                        panjeaMailClient.send(parametri,
                                new ExportToPDFCommand("pdf", reportResult.getReportGenerati().get(0)), true);
                        sbEsito.append("Email inviata correttamente");
                        spedizioneOk = true;
                    } catch (Exception e) {
                        mailException = e;
                    } finally {
                        // dopo l'invio della mail spetto per non inviare la eventuale successiva ad
                        // un intervallo troppo ristretto
                        Thread.sleep(2000);
                    }
                } while (mailException != null && StringUtils.contains(mailException.getMessage(),
                        "Sending the email to the following server failed") && tentativoInvio <= 3);

                if (mailException != null) {
                    throw mailException;
                }
            } else {
                sbEsito.append("Errore durante l'invio della mail.\n");
                if (!reportResult.getReportErrors().getReportsNonTrovatiException().isEmpty()) {
                    sbEsito.append("Nessun layout trovato per i documenti:\n");
                    sbEsito.append(
                            StringUtils.join(reportResult.getReportErrors().getReportsNonTrovatiException(), "\n"));
                }
                if (!reportResult.getReportErrors().getReportsMaxPageException().isEmpty()) {
                    sbEsito.append("Numero massimo di pagine superato per i documenti:\n");
                    sbEsito.append(StringUtils.join(reportResult.getReportErrors().getReportsMaxPageException(), "\n"));
                }
                if (!reportResult.getReportErrors().getReportsGenericException().isEmpty()) {
                    sbEsito.append("Errore generico per i documenti:\n");
                    sbEsito.append(StringUtils.join(reportResult.getReportErrors().getReportsGenericException(), "\n"));
                }
            }
        } catch (Exception e) {
            LOGGER.error("-->errore nella spedizione della mail", e);
            spedizioneOk = false;
            sbEsito.append("Errore generico durante l'invio della mail. " + StringUtils.defaultString(e.getMessage()));
        }

        return new EsitoSpedizione(spedizioneOk, sbEsito.toString());
    }

}
