package it.eurotn.panjea.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import it.eurotn.panjea.mail.exception.EmailException;

public class EmailMessage {

    private static final Logger LOGGER = Logger.getLogger(EmailMessage.class);

    private Message message;

    private Collection<Attachment> attachments;

    /**
     * Costruttore.
     *
     * @param message
     */
    public EmailMessage(Message message) {
        super();
        this.message = message;
        this.attachments = null;
    }

    private void addAttachement(String name, InputStream stream) {

        byte[] byteContent;
        try {
            byteContent = IOUtils.toByteArray(stream);
        } catch (IOException e) {
            LOGGER.error("--> errore durante il caricamento del contenuto dell'allegato", e);
            byteContent = null;
        }

        Attachment attachment = new Attachment(byteContent, name);
        this.attachments.add(attachment);
    }

    private void estractAttachements() throws EmailException {
        this.attachments = new ArrayList<>();
        try {
            LOGGER.debug("--> <" + message.getFrom()[0] + "> " + message.getSubject());
            Multipart multipart = (Multipart) message.getContent();
            LOGGER.debug("--> Message has " + multipart.getCount() + " multipart elements");

            for (int j = 0; j < multipart.getCount(); j++) {
                BodyPart bodyPart = multipart.getBodyPart(j);

                if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    if (bodyPart.getContent().getClass().equals(MimeMultipart.class)) {
                        MimeMultipart mimemultipart = (MimeMultipart) bodyPart.getContent();

                        for (int k = 0; k < mimemultipart.getCount(); k++) {
                            if (mimemultipart.getBodyPart(k).getFileName() != null) {
                                LOGGER.debug("--> Allegato trovato: " + mimemultipart.getBodyPart(k).getFileName());
                                addAttachement(mimemultipart.getBodyPart(k).getFileName(),
                                        mimemultipart.getBodyPart(k).getInputStream());
                            }
                        }
                    }
                    continue;
                }
                LOGGER.debug("--> Allegato trovato: " + bodyPart.getFileName());
                addAttachement(bodyPart.getFileName(), bodyPart.getInputStream());
            }
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento degli allegati del messaggio.", e);
            throw new EmailException("Errore durante il caricamento degli allegati del messaggio.", e);
        }
    }

    /**
     * Restituisce gli allegati presenti nel messaggio.
     *
     * @return allegati presenti
     * @throws MessagingException
     * @throws IOException
     */
    public final Collection<Attachment> getAttachements() throws EmailException {
        if (attachments == null) {
            estractAttachements();
        }

        return attachments;
    }

    /**
     * Restituisce la lista contenente i nomi degli allegati presenti.
     *
     * @return nomi allegati
     */
    public final Collection<String> getAttachementsName() throws EmailException {
        if (attachments == null) {
            estractAttachements();
        }

        Collection<String> names = new TreeSet<String>();
        for (Attachment attachment : attachments) {
            names.add(attachment.getName());
        }
        return names;
    }
}
