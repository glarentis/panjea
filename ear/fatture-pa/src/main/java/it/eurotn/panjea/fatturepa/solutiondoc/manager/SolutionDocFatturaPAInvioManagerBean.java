package it.eurotn.panjea.fatturepa.solutiondoc.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.NotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.manager.interfaces.AziendaFatturaPAManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturePAManager;
import it.eurotn.panjea.fatturepa.solutiondoc.manager.interfaces.SolutionDocFatturaPAInvioManager;
import it.eurotn.panjea.fatturepa.solutiondoc.webservice.manager.SolutionDocFatturaPAManager;
import it.eurotn.panjea.fatturepa.util.AreaNotificaFatturaPADTO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

@Stateless(name = "Panjea.SolutionDocFatturaPAInvioManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.SolutionDocFatturaPAInvioManager")
public class SolutionDocFatturaPAInvioManagerBean implements SolutionDocFatturaPAInvioManager {

    @EJB
    private SolutionDocFatturaPAManager solutionDocFatturaPAManager;

    @EJB
    private FatturePAManager fatturePAManager;

    @EJB
    private AziendaFatturaPAManager aziendaFatturaPAManager;

    @EJB
    private PanjeaMessage panjeaMessage;

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
    public void checkEsitiFatturePA() {

        // carico tutte le fatture di cui ancora non ho un esito finale
        List<AreaMagazzinoFatturaPA> fatturePAAperte = fatturePAManager.caricaFatturePAAperte();

        for (AreaMagazzinoFatturaPA areaMagazzinoFatturaPA : fatturePAAperte) {

            // carico tutte le notifiche che sono attualmente presenti per la fattura
            List<AreaNotificaFatturaPADTO> notifichePresenti = fatturePAManager
                    .caricaAreaNotificheFatturaPA(areaMagazzinoFatturaPA.getId());
            Set<StatoFatturaPA> statiPresenti = new TreeSet<StatoFatturaPA>();
            for (AreaNotificaFatturaPADTO areaNotificaFatturaPADTO : notifichePresenti) {
                statiPresenti.add(areaNotificaFatturaPADTO.getStatoFatturaPA());
            }

            Map<StatoFatturaPA, String> esitiSdIFatturaPA = solutionDocFatturaPAManager
                    .getEsitiSdIFatturaPA(areaMagazzinoFatturaPA.getIdentificativoSDI().toString(), "");
            for (Entry<StatoFatturaPA, String> entry : esitiSdIFatturaPA.entrySet()) {
                if (!statiPresenti.contains(entry.getKey())) {
                    // non ho questo stato quindi lo importo
                    byte[] fileEsitoFatturaPA = solutionDocFatturaPAManager.getFileEsitoFatturaPA(entry.getValue());
                    if (fileEsitoFatturaPA != null) {

                        NotificaFatturaPA notificaFatturaPA = new NotificaFatturaPA();
                        notificaFatturaPA.setData(null);
                        notificaFatturaPA.setDatiEsito(new String(fileEsitoFatturaPA));
                        notificaFatturaPA.setDatiEsitoDaSDI(true);
                        notificaFatturaPA.setStatoFattura(entry.getKey());

                        areaMagazzinoFatturaPA = fatturePAManager
                                .caricaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA.getId());
                        areaMagazzinoFatturaPA.setNotificaCorrente(notificaFatturaPA);
                        areaMagazzinoFatturaPA = fatturePAManager.salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);

                        Documento doc = areaMagazzinoFatturaPA.getAreaMagazzino().getDocumento();
                        StringBuilder sb = new StringBuilder(200);
                        sb.append("Nuova notifica dal SdI importata correttamente.\n");
                        sb.append("Documento: ");
                        sb.append(doc.getTipoDocumento().getCodice());
                        sb.append(" n° " + doc.getCodice().getCodice());
                        sb.append(" del " + DateFormatUtils.format(doc.getDataDocumento(), "dd/MM/yyyy"));
                        panjeaMessage.send(sb.toString(), PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                    }
                }
            }
        }
    }

    @Override
    public String invioSdiFatturaPA(Integer idAreaMagazzinoFatturaPA) {

        solutionDocFatturaPAManager.contattoHub();
        solutionDocFatturaPAManager.checkClienteFatturaPA();

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePAManager
                .caricaAreaMagazzinoFatturaPA(idAreaMagazzinoFatturaPA);

        if (StringUtils.isBlank(areaMagazzinoFatturaPA.getXmlFattura().getXmlFileName())) {
            return null;
        }

        // nome del file zip di cui fare l'upload
        String nomeZip = solutionDocFatturaPAManager.getNomeFileZipFatturaPA();
        if (StringUtils.endsWith(nomeZip, ".zip")) {
            System.out.println(nomeZip);
        } else {
            throw new RuntimeException(nomeZip);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        addToZipFile(areaMagazzinoFatturaPA.getXmlFattura().getXmlFileName(),
                areaMagazzinoFatturaPA.getXmlFattura().getXmlFattura(), zipOutputStream);

        try {
            zipOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // upload del file
        String result = solutionDocFatturaPAManager.uploadFileFatturaPA(nomeZip, byteArrayOutputStream.toByteArray());

        if (!result.endsWith(".zip")) {
            throw new RuntimeException(result);
        }

        AziendaFatturaPA aziendaFatturaPA = aziendaFatturaPAManager.caricaAziendaFatturaPA();

        Object[] invioResult = solutionDocFatturaPAManager.invioSdiFatturaPA(nomeZip, aziendaFatturaPA.getSedeNazione(),
                aziendaFatturaPA.getCodiceIdentificativoFiscale(), false, true);

        String idSdI = null;
        if (invioResult != null && invioResult.length > 0) {
            if (StringUtils.startsWith((CharSequence) invioResult[0], "Errore")) {
                throw new GenericException((String) invioResult[0]);
            }
            idSdI = (String) invioResult[0];
        } else {
            throw new GenericException("Errore generico durante l'invio della fattura PA al SdI");
        }

        return idSdI;
    }
}
