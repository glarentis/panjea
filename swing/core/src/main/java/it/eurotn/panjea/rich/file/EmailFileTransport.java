/**
 * 
 */
package it.eurotn.panjea.rich.file;

import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.magazzino.domain.rendicontazione.DatiSpedizione;
import it.eurotn.rich.control.mail.IPanjeaMailClient;
import it.eurotn.rich.report.editor.export.ChooseEmailTypeDialog;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

/**
 * @author fattazzo
 * 
 */
public class EmailFileTransport extends AbstractFileTransport {

    private LocalFileTransport localFileTransport = new LocalFileTransport();

    private IPanjeaMailClient panjeaMailClient;

    {
        panjeaMailClient = RcpSupport.getBean(IPanjeaMailClient.BEAN_ID);
    }

    @Override
    public boolean send(List<byte[]> files, DatiSpedizione datiSpedizione, Map<String, Object> parametri) {

        // uso LocalFileTransport per farmi creare i file che poi verranno usati come allegato nella mail
        if (localFileTransport.send(files, datiSpedizione, parametri)) {

            List<File> fileGenerati = localFileTransport.getFileCreati();

            ParametriMail parametriMail = new ParametriMail();
            parametriMail.setOggetto("Invio file di rendicontazione.");
            for (File file : fileGenerati) {
                parametriMail.addAttachments(file.getAbsolutePath(), null);
            }

            final Destinatario destinatario = new Destinatario();
            destinatario.setEntita(datiSpedizione.getEntita());
            destinatario.setNome(datiSpedizione.getSedeEntita().getSede().getDescrizione());

            SedeAnagrafica sede = datiSpedizione.getSedeEntita().getSede();
            if (!sede.getIndirizzoMail().isEmpty() && !sede.getIndirizzoPEC().isEmpty()) {
                new ChooseEmailTypeDialog(sede.getIndirizzoMail(), sede.getIndirizzoPEC(), new Closure() {

                    @Override
                    public Object call(Object argument) {
                        destinatario.setEmail((String) argument);
                        return null;
                    }
                }).showDialog();
            } else {
                destinatario
                        .setEmail(sede.getIndirizzoMail().isEmpty() ? sede.getIndirizzoPEC() : sede.getIndirizzoMail());
            }
            parametriMail.getDestinatari().add(destinatario);

            panjeaMailClient.show(parametriMail, null);

        }

        return true;
    }
}
