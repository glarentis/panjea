package it.eurotn.panjea.fatturepa.solutiondoc.manager;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AreaNotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.manager.interfaces.AziendaFatturaPAManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPASettingsManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturePAManager;
import it.eurotn.panjea.fatturepa.solutiondoc.domain.Documento;
import it.eurotn.panjea.fatturepa.solutiondoc.domain.ImportaFatturaPA;
import it.eurotn.panjea.fatturepa.solutiondoc.manager.interfaces.SolutionDocFatturaPAConservazioneManager;
import it.eurotn.panjea.fatturepa.solutiondoc.webservice.manager.SolutionDocFatturaPAManager;
import it.eurotn.panjea.fatturepa.util.AreaNotificaFatturaPADTO;

@Stateless(name = "Panjea.SolutionDocFatturaPAConservazioneManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.SolutionDocFatturaPAConservazioneManager")
public class SolutionDocFatturaPAConservazioneManagerBean implements SolutionDocFatturaPAConservazioneManager {

    private static final Logger LOGGER = Logger.getLogger(SolutionDocFatturaPAConservazioneManagerBean.class);

    @EJB
    private FatturaPASettingsManager fatturaPASettingsManager;

    @EJB
    private FatturePAManager fatturePAManager;

    @EJB
    private AziendaFatturaPAManager aziendaFatturaPAManager;

    @EJB
    private SolutionDocFatturaPAManager solutionDocFatturaPAManager;

    private void addToZipFile(String fileName, byte[] data, ZipOutputStream zos) {

        ZipEntry zipEntry = new ZipEntry(fileName);
        zipEntry.setSize(data.length);

        try {
            zos.putNextEntry(zipEntry);
            zos.write(data);
            zos.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToZipFile(String fileName, String data, ZipOutputStream zos) {

        ZipEntry zipEntry = new ZipEntry(fileName);
        zipEntry.setSize(data.getBytes().length);

        try {
            zos.putNextEntry(zipEntry);
            zos.write(data.getBytes());
            zos.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void conservaXMLFatturePA() {

        solutionDocFatturaPAManager.contattoHub();
        solutionDocFatturaPAManager.checkClienteFatturaPA();

        List<AreaMagazzinoFatturaPA> aree = fatturePAManager.caricaAreaMagazzinoFatturaPANonConservate();

        for (AreaMagazzinoFatturaPA areaMagazzinoFatturaPA : aree) {
            // nome del file zip di cui fare l'upload
            String nomeZip = solutionDocFatturaPAManager.getNomeFileZipFatturaPA();
            if (StringUtils.endsWith(nomeZip, ".zip")) {
                System.out.println(nomeZip);
            } else {
                throw new RuntimeException(nomeZip);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

            List<AreaNotificaFatturaPADTO> notificheFatturaPA = fatturePAManager
                    .caricaAreaNotificheFatturaPA(areaMagazzinoFatturaPA.getId());

            // creo il file xml contenente la lista dei documenti da inviare
            ImportaFatturaPA importaFatturaPA = new ImportaFatturaPA();
            // Documento fattura
            Documento fattura = new Documento();
            fattura.setTipo("Fattura");
            fattura.setNomeFile(areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato());
            fattura.setIdSdi(areaMagazzinoFatturaPA.getIdentificativoSDI().toString());
            fattura.setData(notificheFatturaPA.get(0).getData());
            importaFatturaPA.getDocumenti().add(fattura);
            try {
                addToZipFile(
                        areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato(), fatturePAManager
                                .downloadXMLFirmato(areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato()),
                        zipOutputStream);
            } catch (PreferenceNotFoundException e1) {
                throw new RuntimeException(e1);
            }

            // documenti notifiche
            String xmlFile = areaMagazzinoFatturaPA.getXmlFattura().getXmlFileName();
            for (AreaNotificaFatturaPADTO notifica : notificheFatturaPA) {
                if (notifica.getStatoFatturaPA() == StatoFatturaPA._DI
                        || notifica.getStatoFatturaPA() == StatoFatturaPA._IN) {
                    continue;
                }
                String fileName = MessageFormat.format("{0}_{1}_001.xml", new Object[] {
                        xmlFile.subSequence(0, xmlFile.length() - 4), notifica.getStatoFatturaPA().name() });

                AreaNotificaFatturaPA areaNotificaFatturaPA = fatturePAManager
                        .caricaAreaNotificaFatturaPA(notifica.getId());
                addToZipFile(fileName, areaNotificaFatturaPA.getNotificaFatturaPA().getDatiEsito(), zipOutputStream);

                Documento notificaDoc = new Documento();
                notificaDoc.setTipo("Esito");
                notificaDoc.setNomeFile(fileName);
                notificaDoc.setIdSdi(areaMagazzinoFatturaPA.getIdentificativoSDI().toString());
                importaFatturaPA.getDocumenti().add(notificaDoc);
            }

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ImportaFatturaPA.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                StringWriter stringWriter = new StringWriter();

                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                QName qName = new QName("org.example.schema", "ImportaFatturaPA");
                JAXBElement<ImportaFatturaPA> root = new JAXBElement<ImportaFatturaPA>(qName, ImportaFatturaPA.class,
                        importaFatturaPA);

                jaxbMarshaller.marshal(root, stringWriter);

                addToZipFile("fileDefinition.xml", stringWriter.toString(), zipOutputStream);

                zipOutputStream.close();
            } catch (Exception e) {
                LOGGER.error("Exception caught while unmarshalling object.", e);
            }

            // upload del file
            String result = solutionDocFatturaPAManager.uploadFileFatturaPA(nomeZip,
                    byteArrayOutputStream.toByteArray());

            if (!result.endsWith(".zip")) {
                throw new RuntimeException(result);
            }

            Object[] resultImport = solutionDocFatturaPAManager.importaFatturaPA(nomeZip, "fileDefinition.xml");

            if (resultImport.length > 0 && StringUtils.startsWith((CharSequence) resultImport[0], "Errore")) {
                LOGGER.error(resultImport[0]);
            } else {
                // importazione effettuata con successo
                // areaMagazzinoFatturaPA.setConservazioneSostitutivaEseguita(true);
                LOGGER.error(resultImport.length);
            }
        }
    }

}
