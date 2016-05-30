package it.eurotn.panjea.fatturepa.rich.commands;

import java.awt.Dimension;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePAAnagraficaBD;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePAAnagraficaBD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.panjea.fatturepa.signer.FileSigner;
import it.eurotn.panjea.fatturepa.signer.FileSignerFactory;
import it.eurotn.panjea.fatturepa.signer.SignResult;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.dialog.MessageAlert;

public class CreaEFirmaXMLFatturePACommand extends ActionCommand {

    private class Result {

        private Documento documento;

        private String xmlCreationResult;

        private SignResult signResult;

        /**
         * @return the documento
         */
        public Documento getDocumento() {
            return documento;
        }

        /**
         * @return the signResult
         */
        public SignResult getSignResult() {
            return signResult;
        }

        /**
         * @return the xmlCreationResult
         */
        public String getXmlCreationResult() {
            return xmlCreationResult;
        }

        /**
         * @param documento
         *            the documento to set
         */
        public void setDocumento(Documento documento) {
            this.documento = documento;
        }

        /**
         * @param signResult
         *            the signResult to set
         */
        public void setSignResult(SignResult signResult) {
            this.signResult = signResult;
        }

        /**
         * @param xmlCreationResult
         *            the xmlCreationResult to set
         */
        public void setXmlCreationResult(String xmlCreationResult) {
            this.xmlCreationResult = xmlCreationResult;
        }
    }

    private class ResultDialog extends MessageDialog {

        private JTabbedPane tabbedResult;

        public ResultDialog() {
            super("Risultato operazione", "_");
            setDialogScaleFactor(1.0f);
            setPreferredSize(new Dimension(800, 600));

            tabbedResult = getComponentFactory().createTabbedPane();
        }

        @Override
        protected JComponent createDialogContentPane() {
            return tabbedResult;
        }

        public void setResults(List<Result> results) {

            tabbedResult.removeAll();

            for (Result result : results) {
                JPanel panel = getComponentFactory().createPanel(new VerticalLayout(5));
                if (!StringUtils.isBlank(result.getXmlCreationResult())) {
                    panel.add(new JLabel("<html><b>Creazione XML</b></html>"));
                    panel.add(new JLabel(result.getXmlCreationResult()));
                }
                if (result.getSignResult() != null) {
                    panel.add(new JLabel("<html><b>Firma file XML</b></html>"));
                    StringBuilder message = new StringBuilder(200);
                    if (StringUtils.isBlank(result.signResult.getFileSigned())) {
                        // c'è stato un errore durante la firma perchè il file firmato non esiste
                        message.append(
                                "<html>ATTENZIONE: si è verificato un errore durante la firma del file XML.<br><br>");
                    } else {
                        message.append("<html>La firma del file XML è avvenuta con successo.<br><br>");
                    }
                    message.append("Resoconto dell'operazione:<br>");
                    message.append(StringUtils.replace(result.signResult.getSignLog(), "\n", "<br>"));
                    message.append("</html>");
                    panel.add(new JLabel(message.toString()));
                }

                GuiStandardUtils.attachBorder(panel);

                tabbedResult.addTab(ObjectConverterManager.toString(result.getDocumento(), Documento.class, null),
                        panel);
            }
        }

    }

    public enum TipoOperazione {
        CREA_XML, FIRMA_XML, CREA_E_FIRMA_XML
    }

    private static final Logger LOGGER = Logger.getLogger(CreaEFirmaXMLFatturePACommand.class);

    public static final String PARAM_ID_AREE_MAGAZZINO = "paramIdAreeMagazzino";
    public static final String PARAM_OPERAZIONE = "paramOperazione";

    private IFatturePABD fatturePABD;
    private IFatturePAAnagraficaBD fatturePAAnagraficaBD;

    private ResultDialog resultDialog = new ResultDialog();

    /**
     * Costruttore.
     */
    public CreaEFirmaXMLFatturePACommand() {
        super("creaEFirmaXMLFatturePACommand");
        RcpSupport.configure(this);

        fatturePABD = RcpSupport.getBean(FatturePABD.BEAN_ID);
        fatturePAAnagraficaBD = RcpSupport.getBean(FatturePAAnagraficaBD.BEAN_ID);
    }

    private String creaXMLFattura(Integer idAreaMagazzino) {

        String log = "";
        try {
            fatturePABD.creaXMLFattura(idAreaMagazzino);

            log = "<html>File XML creato correttamente</html>";
        } catch (XMLCreationException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Errore durante la creazione del file XML.", e);
            }
            log = e.getFormattedMessage();
        }

        return log;
    }

    @Override
    protected void doExecuteCommand() {

        @SuppressWarnings("unchecked")
        List<Integer> idAree = (List<Integer>) getParameter(PARAM_ID_AREE_MAGAZZINO, null);
        TipoOperazione tipoOperazione = (TipoOperazione) getParameter(PARAM_OPERAZIONE,
                TipoOperazione.CREA_E_FIRMA_XML);

        if (idAree == null || idAree.isEmpty()) {
            return;
        }

        MessageAlert alert = new MessageAlert(new DefaultMessage("Creazione/Firma XML in corso..."), 0);
        alert.showAlert();

        FatturaPASettings fatturaPASettings = fatturePAAnagraficaBD.caricaFatturaPASettings();

        String pin = "";
        if (fatturaPASettings.isGestioneFirmaElettronica()
                && (tipoOperazione == TipoOperazione.FIRMA_XML || tipoOperazione == TipoOperazione.CREA_E_FIRMA_XML)) {
            InputApplicationDialog pinDialog = new InputApplicationDialog("Richiesta codice PIN", (Window) null);
            pinDialog.setInputLabelMessage("PIN");
            pinDialog.showDialog();
            pin = (String) pinDialog.getInputValue();
        }

        List<Result> results = new ArrayList<Result>();
        try {
            for (Integer idAreaMagazzino : idAree) {

                AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePABD
                        .caricaAreaMagazzinoFatturaPA(idAreaMagazzino);

                if (areaMagazzinoFatturaPA == null) {
                    continue;
                }
                Result result = processArea(tipoOperazione, idAreaMagazzino, areaMagazzinoFatturaPA, pin,
                        fatturaPASettings);
                results.add(result);

                // se il risultato della firma contiene "PIN errato" mi fermo altrimenti se firmo più di 3 documenti
                // sbagliando il pin si blocca il supporto e serve il codice puk per ripristinarlo
                if (result.getSignResult() != null
                        && result.getSignResult().getSignLog().toUpperCase().contains("PIN ERRATO")) {
                    break;
                }

            }
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            alert.closeAlert();
        }

        resultDialog.setResults(results);
        resultDialog.showDialog();

    }

    private SignResult firmaXMLFatture(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA, String pin,
            FatturaPASettings fatturaPASettings) {

        // verifico se la firma è gestita da Panjea
        if (!fatturaPASettings.isGestioneFirmaElettronica()) {
            PanjeaSwingUtil.checkAndThrowException(new GenericException(
                    "Le preferenze della fatturazione PA non prevedono la gestione delle firma elettronica."));
        }

        if (StringUtils.isBlank(pin)) {
            throw new GenericException("PIN necessario per la firma del file.");
        }

        // firmo tutti gli xml
        FileSigner fileSigner = FileSignerFactory.getFileSigner();
        if (fileSigner == null) {
            PanjeaSwingUtil.checkAndThrowException(new GenericException(
                    "Verificare le impostazioni delle preferenze della fatturazione PA per l'uso del software di firma."));
        }

        SignResult signResult = fileSigner.signFile(areaMagazzinoFatturaPA.getXmlFattura(), pin);

        // firma completata con successo, salvo l'XML firmato
        if (!StringUtils.isBlank(signResult.getFileSigned())) {
            byte[] xmlContent;
            try {
                xmlContent = FileUtils.readFileToByteArray(new File(signResult.getFileSigned()));
            } catch (IOException e) {
                logger.error("--> errore durante la lettura del file creato", e);
                throw new GenericException("errore durante la lettura del file creato");
            }

            fatturePABD.salvaXMLFatturaFirmato(areaMagazzinoFatturaPA.getAreaMagazzino().getId(), xmlContent,
                    FilenameUtils.getName(signResult.getFileSigned()));
        }

        return signResult;
    }

    private Result processArea(TipoOperazione tipoOperazione, Integer idAreaMagazzino, AreaMagazzinoFatturaPA area,
            String pin, FatturaPASettings fatturaPASettings) {

        Result result = new Result();

        result.setDocumento(area.getAreaMagazzino().getDocumento());
        switch (tipoOperazione) {
        case CREA_XML:
            result.setXmlCreationResult(creaXMLFattura(idAreaMagazzino));
            break;
        case FIRMA_XML:
            if (area != null) {
                result.setSignResult(sign(area, pin, fatturaPASettings));
            }
            break;
        default:
            result.setXmlCreationResult(creaXMLFattura(idAreaMagazzino));
            result.setSignResult(
                    sign(fatturePABD.caricaAreaMagazzinoFatturaPA(idAreaMagazzino), pin, fatturaPASettings));
            break;
        }

        return result;
    }

    private SignResult sign(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA, String pin,
            FatturaPASettings fatturaPASettings) {
        SignResult result = null;

        boolean xmlPresente = !StringUtils.isBlank(areaMagazzinoFatturaPA.getXmlFattura().getXmlFattura());
        boolean xmlFirmatoPresente = !StringUtils
                .isBlank(areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato());
        boolean statoOkPerFirma = areaMagazzinoFatturaPA.getNotificaCorrente().getStatoFattura() == null
                || areaMagazzinoFatturaPA.getNotificaCorrente().getStatoFattura() == StatoFatturaPA._DI
                || !areaMagazzinoFatturaPA.getNotificaCorrente().isEsitoPositivo();

        boolean firmaPossibile = xmlPresente && !xmlFirmatoPresente && statoOkPerFirma;

        if (firmaPossibile) {
            result = firmaXMLFatture(areaMagazzinoFatturaPA, pin, fatturaPASettings);
        } else {
            result = new SignResult();
            result.setSignLog(
                    "Non è possibile firmare il file. Le motivazioni possono essere:<ul><li>Il file XML non è stato ancora creato.</li><li>Il file XML risulta già firmato.</li><li>La fattura è già all'interno del processo di invio elettronico.</li></ul>");
        }

        return result;
    }

}
