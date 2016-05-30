package it.eurotn.rich.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.util.ByteArrayDataSource;

import org.apache.axis.encoding.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.Mail;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.rich.bd.IMailBD;
import it.eurotn.panjea.rich.bd.MailBD;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;

public class SendMailTask implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(SendMailTask.class);

    private ParametriMail parametri;
    private Utente utente;
    private boolean aggiungiFirma;

    private IMailBD mailBD;

    private boolean salvaMail;

    /**
     * Task per spedire la mail; faccio un task a parte perchè se il server non è raggiungibile o ci sono problemi nella
     * connessione, il timeout è molto lungo e altrimenti non permetterebbe di eseguire altre operazioni.
     *
     * @param parametri
     *            parametri mail
     * @param utente
     *            utente
     * @param aggiungiFirma
     *            firma presente
     */
    public SendMailTask(final ParametriMail parametri, final Utente utente, final boolean aggiungiFirma) {
        this(parametri, utente, aggiungiFirma, true);
    }

    /**
     * Task per spedire la mail; faccio un task a parte perchè se il server non è raggiungibile o ci sono problemi nella
     * connessione, il timeout è molto lungo e altrimenti non permetterebbe di eseguire altre operazioni.
     *
     * @param parametri
     *            parametri mail
     * @param utente
     *            utente
     * @param aggiungiFirma
     *            firma presente
     * @param salvaMail
     *            salva la mail inviata per averne il log in panjea
     */
    public SendMailTask(final ParametriMail parametri, final Utente utente, final boolean aggiungiFirma,
            final boolean salvaMail) {
        super();
        this.parametri = parametri;
        this.utente = utente;
        this.aggiungiFirma = aggiungiFirma;
        this.salvaMail = salvaMail;
        this.mailBD = RcpSupport.getBean(MailBD.BEAN_ID);
    }

    private static String replaceDiv(String htmlText) {
        String noDivHtml = htmlText.replace("<div>", "");
        noDivHtml = noDivHtml.replace("</div>", "<br>");
        return noDivHtml;
    }

    /**
     * Crea l'email da spedire.
     *
     * @param mail
     *            mail da spedire
     * @param firma
     *            test da aggiungere in calce al testo dell'email. NULL per non aggiungere la firma.
     * @return EMail
     * @throws EmailException
     *             sollevata in caso di errore durante la creazione
     */
    private Email createMessage(Mail mail, String firma, final DatiMail datiMail) throws EmailException {

        StringBuilder text = new StringBuilder("");
        text.append(StringUtils.defaultString(mail.getTesto()));
        text.append(StringUtils.defaultString(firma));

        String bodyText = replaceDiv(text.toString());

        String fromUser = StringUtils.defaultString(utente.getNome()) + " "
                + StringUtils.defaultString(utente.getCognome());
        if (StringUtils.isEmpty(fromUser)) {
            fromUser = datiMail.getEmail();
        }

        // creazione email
        HtmlEmail email = new HtmlEmail();
        email.setHostName(datiMail.getServer());
        email.setSmtpPort(datiMail.getPort());

        if (parametri.isNotificaLettura()) {
            email.addHeader("Disposition-Notification-To", datiMail.getEmail());
            email.addHeader("Return-Receipt-To", datiMail.getEmail());
        }

        // email.setDebug(true);
        if (datiMail.isAuth()) {
            // email.setAuthentication(datiMail.getUtenteMail(), datiMail.getPasswordMail());
            email.setAuthenticator(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(datiMail.getUtenteMail(), datiMail.getPasswordMail());
                }
            });
        }
        if (!StringUtils.isEmpty(datiMail.getEmailDiRisposta())) {
            try {
                email.setReplyTo(Arrays.asList(new InternetAddress(datiMail.getEmailDiRisposta())));
                email.setBounceAddress(datiMail.getEmailDiRisposta());
            } catch (AddressException e) {
                LOGGER.error(
                        "--> errore durante l'assegnazione dell'email di risposta " + datiMail.getEmailDiRisposta(), e);
                throw new EmailException(
                        "errore durante l'assegnazione dell'email di risposta " + datiMail.getEmailDiRisposta());
            }
        }
        switch (datiMail.getTipoConnessione()) {
        case STARTTSL:
            email.setStartTLSEnabled(true);
            break;
        case SSL:
            email.setSSLOnConnect(true);
            email.setSslSmtpPort(datiMail.getPort().toString());
            break;
        case NESSUNA:
        default:
            break;
        }
        try {
            email.setSocketConnectionTimeout(15000);
            email.addTo(mail.getDestinatario().getEmail());
            email.setFrom(datiMail.getEmail(), fromUser);
            email.setSubject(mail.getOggetto());
            String htmlMessage = embedBase64Contents(email, bodyText);
            email.setHtmlMsg(htmlMessage);

            // creazione allegati
            if (mail.hasAttachment()) {
                Set<String> filesName = mail.getAllegati().keySet();
                for (final String file : filesName) {
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setPath(file);
                    attachment.setDisposition(EmailAttachment.ATTACHMENT);
                    String nameAllegato = mail.getNomiAllegati().get(file) != null ? mail.getNomiAllegati().get(file)
                            : FilenameUtils.getName(file);
                    attachment.setName(nameAllegato);
                    email.attach(attachment);
                }
            }
        } catch (EmailException e) {
            LOGGER.error("--> errore durante la creazione della mail", e);
            throw new EmailException("errore durante la creazione della mail", e);
        }

        return email;
    }

    /**
     * Esegue l'embed di tutti i contenuti base64 e restituisce il testo html modificato.
     *
     * @param email
     *            email
     * @param htmlMessage
     *            testo html
     * @return testo modificato
     * @throws EmailException
     *             sollevata in caso di errore durante la creazione
     */
    private String embedBase64Contents(HtmlEmail email, String htmlMessage) throws EmailException {
        Matcher matcher = Pattern.compile("<img[^>]+src=\"([^\">]+)\">").matcher(htmlMessage);
        StringBuffer sb = new StringBuffer(htmlMessage.length());
        while (matcher.find()) {
            String group = matcher.group(0);
            int idxBase64 = group.indexOf("base64,");
            int idxData = group.indexOf("data:");
            // recupero il content type del base64 perchè mi serve per fare l'embed dell'immagine
            String contentType = group.substring(idxData + 5, idxBase64 - 1);

            try {
                // decodifico il base64 e inserisco il cid ottenuto come src dell'immagine
                byte[] bytes = Base64.decode(group.substring(idxBase64 + 7));
                String cid = email.embed(new ByteArrayDataSource(bytes, contentType),
                        "image" + UUID.randomUUID().getMostSignificantBits());
                matcher.appendReplacement(sb, "<img src=\"cid:" + cid + "\">");
            } catch (Exception e) {
                LOGGER.error("--> errore durante l'embed dell'immagine nella mail", e);
                throw new EmailException("errore durante l'embed dell'immagine nella mail", e);
            }
        }

        return matcher.appendTail(sb).toString();
    }

    @Override
    public final void run() {

        String firma = null;
        if (aggiungiFirma) {
            firma = parametri.getDatiMail().getFirma();
        }

        Set<Mail> mailCreate = parametri.createEmails(utente);
        for (Mail mail : mailCreate) {
            Email email = null;
            try {
                MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
                mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
                mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
                mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
                mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
                mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
                CommandMap.setDefaultCommandMap(mc);

                email = createMessage(mail, firma, parametri.getDatiMail());

                email.send();
                mail.setSuccesso(true);
            } catch (EmailException e) {
                LOGGER.error("--> errore impossibile spedire la Email ", e);
                mail.setSuccesso(false);
                throw new RuntimeException(e);
            } catch (Exception e1) {
                System.err.println(e1);
            } finally {
                // Salvo la mail
                if (email != null && salvaMail) {
                    salvaMailDMS(email, mail.getDestinatario().getEntita(), mail.isSuccesso());
                }
            }

        }
        if (parametri.hasAttachment()) {
            // cancello solo i file nella directory temporanea perchè sono quelli generati dall'esportazione e non
            // quelli allegati manualmente
            for (String filePath : parametri.getAttachments().keySet()) {
                File file = new File(filePath);
                if (System.getProperty("java.io.tmpdir").equals(file.getParent())) {
                    file.delete();
                }
            }
        }
    }

    /**
     * Salva la mail nella gestione documentale.
     *
     * @param email
     *            email da salvare
     * @param destinatario
     *            entita destinatario
     * @param inviata
     *            inviata con successo
     */
    private void salvaMailDMS(Email email, EntitaLite destinatario, boolean inviata) {
        ByteArrayOutputStream emlBytes = new ByteArrayOutputStream();
        try {
            if (email.getMimeMessage() == null) {
                email.buildMimeMessage();
            }
            email.getMimeMessage().writeTo(emlBytes);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la creazione del file eml della mail", e);
            throw new PanjeaRuntimeException("Errore durante il salvataggio della mail nella gestione documentale");
        }
        mailBD.salvaMail(emlBytes.toByteArray(), destinatario, inviata);
    }

}