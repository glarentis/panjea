package it.eurotn.panjea.rich.editors.webbrowser;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.ReportManager;

import java.awt.Desktop;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

public class JasperServerUrl extends PanjeaUrl implements Callback<PopupFeatures, WebEngine> {
    private class ExportTask extends Task<String> {
        private String ext;
        private Map<String, String> param;

        /**
         * 
         * @param ext
         *            espensione da esportare
         * @param param
         *            parametri del report.
         */
        public ExportTask(final String ext, final Map<String, String> param) {
            super();
            this.ext = ext;
            this.param = param;
        }

        @Override
        protected String call() throws Exception {
            final File fileExport = File.createTempFile("export", String.format(".%s", ext));
            fileExport.delete();
            rm.exportReport(param, reportPath, fileExport.getAbsolutePath(), ext);
            return fileExport.getAbsolutePath();
        }
    }

    private class PopUpChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
            String ext = FilenameUtils.getExtension(engine.getLocation());
            if (Arrays.asList(ReportManager.EXPORT_TYPE).contains(ext)) {

                Element el = doc.getElementById("exportActionForm");
                engine.executeScript("Report.exportReport('csv')");
                HTMLFormElement form = (HTMLFormElement) el;
                NodeList nodes = form.getElementsByTagName("input");
                Map<String, String> param = new HashMap<String, String>();
                for (int i = 0; i < nodes.getLength(); i++) {
                    HTMLInputElement input = (HTMLInputElement) nodes.item(i);
                    if (!input.getValue().isEmpty() && !input.getValue().equals("~NOTHING~")) {

                        String name = input.getName();
                        String value = input.getValue();

                        if (checkDateType(name)) {
                            value = String.valueOf(convertDateToMilliSec(value));
                        }

                        param.put(name, value);
                    }
                }
                Message mess = new DefaultMessage("Esportazione in corso...");
                MessageAlert exportMessageAlert = new MessageAlert(mess);
                exportMessageAlert.showAlert();
                ExportTask task = new ExportTask(ext, param);
                try {
                    new Thread(task).start();
                    String pathFile = task.get();
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(new File(pathFile));
                    } else {
                        new MessageDialog("Esportazione terminata", String.format("Percorso del file %s", pathFile))
                                .showDialog();
                    }
                } catch (Exception e) {
                    logger.error("-->errore nell'esportare il report ", e);
                    throw new PanjeaRuntimeException(e);
                } finally {
                    exportMessageAlert.closeAlert();
                }
            }
        }

        /**
         * Verifica se l'id di riferimento ha un imput di tipo data.
         * 
         * @param id
         *            id
         * @return <code>true</code> se di tipo data
         */
        private boolean checkDateType(String id) {
            boolean dateType = false;

            Element el = doc.getElementById(id);
            if (el != null) {
                NodeList nodes = el.getElementsByTagName("input");

                if (nodes.getLength() > 0) {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        HTMLInputElement node = (HTMLInputElement) nodes.item(i);
                        dateType = dateType
                                || (node.getAttribute("class") != null && node.getAttribute("class").contains("date"));
                    }
                }
            }

            return dateType;
        }

        /**
         * Converte la data da formato stringa a millisecondi.
         * 
         * @param dateString
         *            data in formato stringa
         * @return millisecondi
         */
        private long convertDateToMilliSec(String dateString) {
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(dateString));
            } catch (ParseException e) {
                logger.error("--> errore durante la conversione della data " + dateString, e);
                throw new RuntimeException("errore durante la conversione della data " + dateString);
            }
            return calendar.getTimeInMillis();
        }
    }

    private ReportManager rm;

    private Document doc;
    private String reportPath;
    private PopUpChangeListener popupChangeListener;
    private static Logger logger = Logger.getLogger(JasperServerUrl.class);

    @Override
    public WebEngine call(PopupFeatures paramP) {
        // try {
        WebView w2 = new WebView();
        final WebEngine eng = w2.getEngine();
        eng.getLoadWorker().workDoneProperty().addListener(popupChangeListener);
        return eng;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        super.changed(observableValue, oldValue, newValue);
        doc = engine.getDocument();
        if (doc != null) {
            Element el = doc.getElementById("exportActionForm");
            if (el != null) {
                reportPath = "";
                if (engine.getLocation().contains("reportUnit=")) {
                    String[] r = engine.getLocation().split("&");
                    for (String string : r) {
                        if (string.startsWith("reportUnit")) {
                            reportPath = string.replace("reportUnit=", "").replace("%2F", "/");
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getDisplayName() {
        return "Analisi";
    }

    /**
     * init valori server.
     */
    private void init() {
        popupChangeListener = new PopUpChangeListener();
        ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
        rm = RcpSupport.getBean("reportManager");
        setIndirizzo(rm.getServerHomeAddress());
        setFormName("loginForm");
        setPasswordControl("j_password");
        setUserNameControl("j_username");
        setUserName(utente.getDatiJasperServer().getUsername());
        setPassword(utente.getDatiJasperServer().getPassword());
    }

    @Override
    public void setEngine(WebEngine engine) {
        super.setEngine(engine);
        if (engine != null) {
            init();
            engine.setCreatePopupHandler(this);
        }
    }
}
