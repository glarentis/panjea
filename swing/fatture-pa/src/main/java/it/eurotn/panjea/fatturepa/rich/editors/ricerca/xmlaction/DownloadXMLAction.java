package it.eurotn.panjea.fatturepa.rich.editors.ricerca.xmlaction;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.util.ExtensionFileFilter;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.rich.report.editor.export.EsportazioneStampaMessageAlert;

public final class DownloadXMLAction {

    private static final IFatturePABD FATTUREPABD = RcpSupport.getBean(FatturePABD.BEAN_ID);

    private static final Logger LOGGER = Logger.getLogger(DownloadXMLAction.class);

    private DownloadXMLAction() {
    }

    /**
     * Scarica il file XML in una directory scelta dall'utente.
     *
     * @param areaMagazzinoFatturaPA
     *            area magazzino fattura pa
     */
    public static void download(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA) {

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        ExtensionFileFilter eseguibileFileFilter = new ExtensionFileFilter(new String[] {}, "File XML");
        fc.addChoosableFileFilter(eseguibileFileFilter);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(eseguibileFileFilter);
        int returnVal = fc.showOpenDialog(Application.instance().getActiveWindow().getControl());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            EsportazioneStampaMessageAlert exportAlert = new EsportazioneStampaMessageAlert(
                    "Salvataggio del file in corso...");
            exportAlert.showAlert();
            File dirSave = fc.getSelectedFile();

            String fileSave = null;
            byte[] fileContent = null;

            // Il file da scaricare è quello firmato se esiste, altrimenti quello senza firma
            if (!StringUtils.isBlank(areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato())) {
                fileSave = dirSave.getPath() + File.separator
                        + areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato();

                fileContent = FATTUREPABD
                        .downloadXMLFirmato(areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato());
            } else {
                fileSave = dirSave.getPath() + File.separator + areaMagazzinoFatturaPA.getXmlFattura().getXmlFileName();

                fileContent = areaMagazzinoFatturaPA.getXmlFattura().getXmlFattura().getBytes();
            }

            try {
                Files.write(Paths.get(fileSave), fileContent);

                exportAlert.finishExport(new File(fileSave));
            } catch (Exception e1) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("--> Errore durante il salvataggio.", e1);
                }
                exportAlert.errorExport();
            }
        }
    }

}
