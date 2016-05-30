package it.eurotn.panjea.spedizioni.rich.generazione;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.Timer;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.editors.entita.SedeEntitaForm;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.spedizioni.domain.DatiSpedizioni.TIPOINVIO;
import it.eurotn.panjea.spedizioni.exception.SpedizioniVettoreException;
import it.eurotn.panjea.spedizioni.rich.bd.ISpedizioniBD;
import it.eurotn.panjea.spedizioni.rich.bd.SpedizioniBD;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class SoftwareEtichette implements ActionListener {

    private static Logger logger = Logger.getLogger(SoftwareEtichette.class);

    private Vettore vettore;

    private ISpedizioniBD spedizioniBD;

    private Timer fileOutputTimer;

    private AreaMagazzino areaMagazzino;

    /**
     * Costruttore.
     *
     * @param vettore
     */
    public SoftwareEtichette(Vettore vettore) {
        super();
        this.vettore = vettore;
        this.spedizioniBD = RcpSupport.getBean(SpedizioniBD.BEAN_ID);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        File fileOut = new File(vettore.getDatiSpedizioni().getEtichettePath()
                + vettore.getDatiSpedizioni().getNameFileEtichetteToImport());

        if (fileOut.exists()) {
            try {
                fileOutputTimer.stop();
                spedizioniBD.leggiRisultatiEtichette(areaMagazzino, convertFileToByteArray(fileOut));
            } finally {
                fileOut.delete();
            }
        }
    }

    private byte[] convertFileToByteArray(File file) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            logger.error("-->errore durante conversione del file in byte", ex);
        }
        byte[] bytes = bos.toByteArray();

        return bytes;
    }

    /**
     * Crea le etichette per l'area magazzino specificata.
     *
     * @param areaMagazzino
     *            area magazzino di riferimento
     */
    public void creaEtichette(final AreaMagazzinoFullDTO areaMagazzinoFullDTO, Date dataInizioTrasporto) {

        this.areaMagazzino = areaMagazzinoFullDTO.getAreaMagazzino();

        if (logger.isDebugEnabled()) {
            logger.debug("--> Controllo se ci sono altre etichette in creazione.");
        }

        if (!isAvailable()) {
            SpedizioniVettoreException e = new SpedizioniVettoreException(
                    "Impossibile continuare. Esistono altri file di generazione etichette nella directory di esportazione.");
            throw new RuntimeException(e);
        }

        ParametriCreazioneEtichetteForm form = new ParametriCreazioneEtichetteForm();
        form.setFormObject(new ParametriCreazioneEtichette(areaMagazzino,
                areaMagazzinoFullDTO.getAreaRate().getCodicePagamento(), dataInizioTrasporto));
        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(form, null) {

            @Override
            protected boolean onFinish() {
                ((FormBackedDialogPage) getDialogPage()).getBackingFormPage().commit();
                ParametriCreazioneEtichette parametri = (ParametriCreazioneEtichette) ((FormBackedDialogPage) getDialogPage())
                        .getBackingFormPage().getFormObject();

                Cap capEntita = areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getCap();
                // se non esiste il cap della sede entità lo chiedo all'utente
                if (capEntita == null) {
                    MessageDialog dialog = new MessageDialog("Attenzione", "CAP mancante per la sede dell'entità "
                            + ObjectConverterManager.toString(areaMagazzino.getDocumento().getEntita()));
                    dialog.showDialog();
                    areaMagazzino = editSedeEntita(areaMagazzino);
                }

                byte[] fileEtichette = spedizioniBD.generaEtichette(areaMagazzino, parametri);
                saveFile(fileEtichette, vettore.getDatiSpedizioni().getEtichettePath()
                        + vettore.getDatiSpedizioni().getNameFileEtichetteToExport());
                fileOutputTimer = new Timer(1000, SoftwareEtichette.this);
                fileOutputTimer.start();
                return true;
            }
        };
        dialog.showDialog();

    }

    public void creaRendicontazione(List<AreaMagazzinoRicerca> aree) {

        byte[] fileRendicontazione = spedizioniBD.generaRendicontazione(aree, vettore);
        saveFile(fileRendicontazione, vettore.getDatiSpedizioni().getPathFileRendiconto());

        try {
            // per ora esiste sono il tipo FTP quindi faccio un IF. Quando si dovranno implementare MAIL e WEBSERVER
            // togliere l'IF e il metodo uploadFTP e introdurre un manager di invio dati.
            if (vettore.getDatiSpedizioni().getTipoInvio() == TIPOINVIO.FTP) {
                String ftpServer = vettore.getDatiSpedizioni().getIndirizzoInvio();
                String user = vettore.getDatiSpedizioni().getUserInvio();
                String password = vettore.getDatiSpedizioni().getPasswordInvio();
                File file = new File(vettore.getDatiSpedizioni().getPathFileRendiconto());
                String nameFileDest = file.getName();
                uploadFTP(ftpServer, user, password, nameFileDest, file);
            }

            spedizioniBD.rendicontaAreeMagazzino(aree);
        } catch (SpedizioniVettoreException e) {
            // in caso di errore metto il file generato nella directory Log per avere uno storico
            loggafileRendicontazione(vettore.getDatiSpedizioni().getPathFileRendiconto());
            throw new RuntimeException(e);
        }

        MessageDialog dialog = new MessageDialog("Esportazione completata",
                "File di rendicondazione generato con successo.");
        dialog.showDialog();
    }

    public AreaMagazzino editSedeEntita(final AreaMagazzino areaMagazzinoFullDTO) {

        SedeEntita sedeEntita = areaMagazzino.getDocumento().getSedeEntita();

        final SedeEntitaForm sedeEntitaForm = new SedeEntitaForm(new SedeEntita(), true);
        sedeEntitaForm.setTipoEntita(sedeEntita.getEntita().getTipo());

        sedeEntitaForm.getControl();
        sedeEntitaForm.setFormObject(sedeEntita);

        // creo il dialogo con il form sedeEntitaForm
        PanjeaTitledPageApplicationDialog dialogSede = new PanjeaTitledPageApplicationDialog(sedeEntitaForm,
                Application.instance().getActiveWindow().getControl()) {

            @Override
            protected String getTitle() {
                return "Modifica sede entità";
            }

            @Override
            protected void onCancel() {
                super.onCancel();
            }

            @Override
            protected boolean onFinish() {
                // recupero dal form sedeEntitaForm la nuova sede da
                // inserire per l'entita' selezionata
                SedeEntita sedeEntitaDaSalvare = (SedeEntita) sedeEntitaForm.getFormObject();
                if (sedeEntitaDaSalvare.getLingua() != null) {
                    String[] descLingua = sedeEntitaDaSalvare.getLingua().split("-");
                    sedeEntitaDaSalvare.setLingua(descLingua[0].trim());
                }
                SedeEntita sedeEntitaSalvata = null;
                // salvo l'entita'
                try {
                    IAnagraficaBD anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
                    sedeEntitaSalvata = anagraficaBD.salvaSedeEntita(sedeEntitaDaSalvare);
                    areaMagazzino.getDocumento().setSedeEntita(sedeEntitaSalvata);
                } catch (SedeEntitaPrincipaleAlreadyExistException e) {
                    throw new PanjeaRuntimeException(e);
                }
                return true;
            }
        };
        dialogSede.setTitlePaneTitle("Modifica sede entità");
        dialogSede.showDialog();

        return areaMagazzinoFullDTO;

    }

    /**
     * Verifica se il software è pronto.
     *
     * @return <code>true</code> se pronto
     */
    public boolean isAvailable() {

        File fileIn = new File(vettore.getDatiSpedizioni().getEtichettePath()
                + vettore.getDatiSpedizioni().getNameFileEtichetteToExport());
        File fileOut = new File(vettore.getDatiSpedizioni().getEtichettePath()
                + vettore.getDatiSpedizioni().getNameFileEtichetteToImport());

        return !fileIn.exists() && !fileOut.exists();
    }

    /**
     * Esegue il log del file di rendicontazione specificato. Per effetturare il log viene copiato il file nella sotto
     * directory "Log" e viene rinominato aggiungendo come prefisso al nome la data formattata come "yyyyMMddhhmmss".
     *
     * @param srFile
     *            file di rendicontazione
     */
    private void loggafileRendicontazione(String srFile) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            File f1 = new File(srFile);

            File dirLog = new File(f1.getParent() + File.separator + "Log");
            dirLog.mkdir();

            File f2 = new File(f1.getParent() + File.separator + "Log" + File.separator
                    + dateFormat.format(Calendar.getInstance().getTime()) + f1.getName());
            InputStream in = new FileInputStream(f1);

            // For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception ex) {
            logger.error("--> errore durante la copia del file.", ex);
        }
    }

    /**
     * Salva il file adto come array di byte nel percorso specificato.
     *
     * @param data
     *            file data
     * @param filePath
     *            path di salvataggio
     */
    private void saveFile(byte[] data, String filePath) {

        OutputStream out;
        try {
            out = new FileOutputStream(filePath);
            out.write(data);
            out.close();
        } catch (Exception e) {
            SpedizioniVettoreException e1 = new SpedizioniVettoreException(
                    "Errore durante la creazione del file " + filePath, e);
            throw new RuntimeException(e1);
        }
    }

    /**
     * Upload a file to a FTP server. A FTP URL is generated with the following syntax:
     * ftp://user:password@host:port/filePath;type=i.
     *
     * @param ftpServer
     *            , FTP server address (optional port ':portNumber').
     * @param user
     *            , Optional user name to login.
     * @param password
     *            , Optional password for user.
     * @param fileName
     *            , Destination file name on FTP server (with optional preceding relative path, e.g.
     *            "myDir/myFile.txt").
     * @param source
     *            , Source file to upload.
     * @throws MalformedURLException
     *             , IOException on error.
     */
    public void uploadFTP(String ftpServer, String user, String password, String fileName, File source)
            throws SpedizioniVettoreException {
        if (ftpServer != null && fileName != null && source != null) {
            StringBuffer sb = new StringBuffer("ftp://");
            // check for authentication else assume its anonymous access.
            if (user != null && password != null) {
                sb.append(user);
                sb.append(':');
                sb.append(password);
                sb.append('@');
            }
            sb.append(ftpServer);
            sb.append('/');
            sb.append(fileName);
            /*
             * type ==> a=ASCII mode, i=image (binary) mode, d= file directory listing
             */
            sb.append(";type=i");

            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                URL url = new URL(sb.toString());
                URLConnection urlc = url.openConnection();

                bos = new BufferedOutputStream(urlc.getOutputStream());
                bis = new BufferedInputStream(new FileInputStream(source));

                int i;
                // read byte by byte until end of stream
                while ((i = bis.read()) != -1) {
                    bos.write(i);
                }
            } catch (Exception e) {
                throw new SpedizioniVettoreException(
                        "Errore durante l'upload del file di rendicontazione sul server FTP del vettore.", e);
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ioe) {
                        logger.error("-->errore durante la chiusura della stream", ioe);
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ioe) {
                        logger.error("-->errore durante la chiusura della stream", ioe);
                    }
                }
            }
        } else {
            throw new SpedizioniVettoreException("Dati di invio spedizione sul vettore mancanti.");
        }
    }
}
