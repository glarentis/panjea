package it.eurotn.panjea.fatturepa.manager;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.DatiMailRicezioneSdI;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.domain.NotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.manager.interfaces.EmailNotificheSdICheckerManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPASettingsManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturePAManager;
import it.eurotn.panjea.mail.service.JbossMailService;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

@Stateless(name = "Panjea.EmailNotificheSdICheckerManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.EmailNotificheSdICheckerManager")
public class EmailNotificheSdICheckerManagerBean implements EmailNotificheSdICheckerManager {

    private class ImportMessage {

        private Date receivedDate;

        private String messageID;

        private String content;

        private String attachmentName;

        /**
         * Costruttore.
         *
         */
        public ImportMessage() {
            super();
        }

        /**
         * @return the attachmentName
         */
        public String getAttachmentName() {
            return attachmentName;
        }

        /**
         * @return the content
         */
        public String getContent() {
            return content;
        }

        /**
         * @return the messageID
         */
        public String getMessageID() {
            return messageID;
        }

        /**
         * @return the receivedDate
         */
        public Date getReceivedDate() {
            return receivedDate;
        }

        /**
         * @param attachmentName
         *            the attachmentName to set
         */
        public void setAttachmentName(String attachmentName) {
            this.attachmentName = attachmentName;
        }

        /**
         * @param content
         *            the content to set
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         * @param messageID
         *            the messageID to set
         */
        public void setMessageID(String messageID) {
            this.messageID = messageID;
        }

        /**
         * @param receivedDate
         *            the receivedDate to set
         */
        public void setReceivedDate(Date receivedDate) {
            this.receivedDate = receivedDate;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(EmailNotificheSdICheckerManagerBean.class);

    private static final String XMLREGEXPATTERN = "[A-Z]{2}[0-9]{11}_[0-9]{5}_[A-Z]{2}_[0-9]{3}.xml";

    private static final String IDENTIFICATIVO_SDI_ELEMENT = "IdentificativoSdI";
    private static final String ESITO_SDI_ELEMENT = "Esito";

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private JbossMailService jbossMailService;

    @EJB
    private FatturaPASettingsManager fatturaPASettingsManager;

    @EJB
    private FatturePAManager fatturePAManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void checkMail(String codiceAzienda) {

        final FatturaPASettings fatturaPASettings = fatturaPASettingsManager.caricaFatturaPASettings();

        if (!fatturaPASettings.isControlloNotificheSdiAbilitato()) {
            return;
        }

        if (!fatturaPASettings.getDatiMailRicezioneSdI().isValid()) {
            panjeaMessage.send("Attenzione, Dati mail ricezione SdI non configurati correttamente",
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            jbossMailService.send("Errore servizione controllo mail SdI",
                    "Attenzione, Dati mail ricezione SdI non configurati correttamente", null);
            throw new RuntimeException("Attenzione, Dati mail ricezione SdI non configurati correttamente");
        }

        // carico i messaggi presenti
        Message[] messages = getInboxMessages(fatturaPASettings.getDatiMailRicezioneSdI(),
                fatturaPASettings.getEmailRicezioneSdI(), fatturaPASettings.getNumeroGiorniControlloNotifiche());

        if (messages == null) {
            return;
        }
        // filtro i messaggi che contengono un allegato che rispecchia il pattern della notifica SdI
        LOGGER.debug("--> Filtro solo i messaggi che contengono allegati conformi alle notifice SdI");
        List<ImportMessage> filteredMessages = filtraNotificheSdIMessages(messages);
        LOGGER.debug("--> Messaggi validi: " + filteredMessages.size());

        for (ImportMessage msg : filteredMessages) {
            // controllo di non aver già importato la notifica
            try {
                if (!fatturePAManager.checkEmailMessageIDNotifica(msg.getMessageID())) {
                    LOGGER.debug("--> File " + msg.getAttachmentName() + " non presente, lo importo");
                    // non ho mai importato il messaggio, procedo
                    importNotifica(msg);
                } else {
                    LOGGER.debug("--> File " + msg.getAttachmentName() + " già importato, lo salto");
                }
            } catch (Exception e) {
                LOGGER.error("--> errore durante il salvataggio della notifica.", e);
                throw new RuntimeException("errore durante il salvataggio della notifica.", e);
            }
        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public String checkMailForTest() {

        StringBuilder logs = new StringBuilder("<html><ul>");

        final FatturaPASettings fatturaPASettings = fatturaPASettingsManager.caricaFatturaPASettings();

        DatiMailRicezioneSdI datiMail = fatturaPASettings.getDatiMailRicezioneSdI();
        if (!datiMail.isValid()) {
            logs.append("<li>Dati mail ricezione SdI non configurati correttamente</li>");
        } else {
            Folder inbox = null;
            try {
                logs.append("<li>Tentativo di connessione imaps al server " + datiMail.getServer() + "</li>");
                Properties props = new Properties();
                Session session = Session.getInstance(props);
                session.setDebug(false);
                Store store = session.getStore("imaps");
                store.connect(datiMail.getServer(), datiMail.getUtenteMail(), datiMail.getPasswordMail());
                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);
                logs.append("<li>Connessione OK</li>");
            } catch (Exception mex) {
                String messaggio = mex.getMessage();
                if (mex.getCause() != null && mex.getCause() instanceof UnknownHostException) {
                    messaggio = "Host sconoscuto: " + mex.getCause().getMessage();
                }

                logs.append("<li><b>Errore durante la lettura dei messaggi per la mail " + datiMail.getServer()
                        + ": </b>" + messaggio + "</li>");
            }

            if (inbox != null) {
                // filtro da oggi a oggi-1
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                ReceivedDateTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LT, calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, -fatturaPASettings.getNumeroGiorniControlloNotifiche());
                ReceivedDateTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, calendar.getTime());
                logs.append("<li>Filtro i messaggi da " + DateFormatUtils.format(newerThan.getDate(), "dd/MM/yyy")
                        + " a " + DateFormatUtils.format(olderThan.getDate(), "dd/MM/yyy") + "</li>");

                // filtro per il mittente
                FromStringTerm fromStringTerm = new FromStringTerm(fatturaPASettings.getEmailRicezioneSdI());
                logs.append("<li>Filtro i messaggi ricevuti da " + fatturaPASettings.getEmailRicezioneSdI() + "</li>");

                AndTerm andTerm = new AndTerm(new SearchTerm[] { newerThan, olderThan, fromStringTerm });

                Message[] messages = null;
                try {
                    messages = inbox.search(andTerm);
                    logs.append("<li>Messaggi ricevuti dal SDI trovati: "
                            + (messages != null ? String.valueOf(messages.length) : "0") + "</li>");
                } catch (MessagingException e) {
                    logs.append("<li><b>Errore durante la ricerca dei messaggi: </b>" + e.getMessage() + "</li>");
                }
            }
        }

        logs.append("</ul></html>");
        return logs.toString();
    }

    /**
     * Cerca ricorsivamente all'interno del messaggio se esiste l'allegato XML di notifica dell'SdI e in caso positivo
     * avvalora il parametro importMessage con tutti i dati necessari. Devo cercare ricorsivamente l'XML perchè può
     * essere allegato direttamente al messaggio oppure può risultare come allegato di allegati ( es: la mail dell'SdI
     * viene allegata come eml dal messaggio della pec del cliente)
     *
     * @param importMessage
     *            messaggio di notifica SdI
     * @param contentMsg
     *            message content
     * @param message
     *            message
     */
    private void checkSdIAttachment(ImportMessage importMessage, Object contentMsg, Message message) {

        try {
            if (contentMsg instanceof Multipart) {
                Multipart content = (Multipart) contentMsg;
                for (int i = 0; i < content.getCount(); i++) {
                    MimeBodyPart part = (MimeBodyPart) content.getBodyPart(i);

                    if (StringUtils.endsWith(part.getFileName(), "xml")) {
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
                                && notificaFileNameMatcher(part.getFileName())) {
                            String xmlNotificaString = IOUtils.toString(part.getInputStream());
                            importMessage.setReceivedDate(message.getSentDate());
                            importMessage.setMessageID(((MimeMessage) message).getMessageID());
                            importMessage.setContent(xmlNotificaString);
                            importMessage.setAttachmentName(part.getFileName());
                        }
                    } else if (StringUtils.endsWith(part.getFileName(), "eml")) {
                        MimeMessage messageEml = new MimeMessage(Session.getInstance(new Properties()),
                                part.getInputStream());
                        checkSdIAttachment(importMessage, messageEml.getContent(), messageEml);
                    } else {
                        checkSdIAttachment(importMessage, part.getContent(), message);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante il parse degli allegati", e);
        }
    }

    private List<ImportMessage> filtraNotificheSdIMessages(Message[] messages) {

        List<ImportMessage> filteredMessages = new ArrayList<ImportMessage>();
        for (Message msg : messages) {
            ImportMessage importMessage = new ImportMessage();
            try {
                checkSdIAttachment(importMessage, msg.getContent(), msg);
            } catch (Exception e) {
                importMessage.setMessageID(null);
            }

            if (importMessage.getMessageID() != null) {
                filteredMessages.add(importMessage);
            }
        }

        return filteredMessages;
    }

    private String getElementValue(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }
        return null;
    }

    private boolean getEsitoNotifica(StatoFatturaPA statoFattura, Element notificaElement) {
        boolean esito;

        switch (statoFattura) {
        case NS:
            esito = false;
            break;
        case NE:
            // controllo l'esito per capire se è stata accettata o rifiutata. EC01 = ACCETTAZIONE EC02 = RIFIUTO
            String esitoValue = getElementValue(ESITO_SDI_ELEMENT, notificaElement);
            esito = java.util.Objects.equals(esitoValue, "EC01");
            break;
        default:
            esito = true;
            break;
        }

        return esito;
    }

    /**
     * Carica tutti i messaggi presenti compresi tra la data attuale e il giorno prima.
     *
     * @param numeroGiorniControllo
     *            numero giorni da cui far partire il controllo ( data odierna - giorni di controllo )
     * @param fatturaPASettings
     *            settings
     * @return messaggi caricati
     */
    private Message[] getInboxMessages(final DatiMailRicezioneSdI datiMail, String fromToCheck,
            Integer numeroGiorniControllo) {

        Message[] messages = null;

        try {
            LOGGER.debug("--> Tentativo di connessione imaps al server " + datiMail.getServer());
            Properties props = new Properties();
            Session session = Session.getInstance(props);
            session.setDebug(false);
            Store store = session.getStore("imaps");
            store.connect(datiMail.getServer(), datiMail.getUtenteMail(), datiMail.getPasswordMail());
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            LOGGER.debug("--> Connessione OK");

            // cerco i messaggi presenti
            messages = searchMessages(inbox, fromToCheck, numeroGiorniControllo);
        } catch (Exception mex) {
            String error = "errore durante la lettura dei messaggi per la mail: " + datiMail.getServer();
            LOGGER.error("--> " + error, mex);
            panjeaMessage.send(error, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            jbossMailService.send("Errore servizio controllo mail SdI", error, null);
            throw new RuntimeException(error, mex);
        }

        return messages;
    }

    private void importNotifica(ImportMessage message) {

        try {
            String originaXmlFileName = null;
            Pattern pattern = Pattern.compile("([A-Z]{2}[a-zA-Z0-9]{11}_[a-zA-Z0-9]{5})");
            Matcher matcher = pattern.matcher(message.getAttachmentName());
            if (matcher.find()) {
                originaXmlFileName = matcher.group(1) + ".xml";
            }

            if (!StringUtils.isBlank(originaXmlFileName) && !StringUtils.isBlank(message.getContent())) {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder
                        .parse(new InputSource(new ByteArrayInputStream(message.getContent().getBytes())));
                Element rootElement = document.getDocumentElement();

                String identificativoSdI = getElementValue(IDENTIFICATIVO_SDI_ELEMENT, rootElement);

                AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePAManager
                        .caricaAreaMagazzinoFatturaPA(new BigInteger(identificativoSdI), originaXmlFileName);
                if (areaMagazzinoFatturaPA != null) {

                    StatoFatturaPA stato = StatoFatturaPA
                            .valueOf(StringUtils.split(message.getAttachmentName(), "_")[2]);

                    // creo la nuova notifica
                    NotificaFatturaPA notificaFatturaPA = new NotificaFatturaPA();
                    notificaFatturaPA.setData(message.getReceivedDate());
                    notificaFatturaPA.setDatiEsito(message.getContent());
                    notificaFatturaPA.setDatiEsitoDaSDI(true);
                    notificaFatturaPA.setEmailMessageID(message.getMessageID());
                    notificaFatturaPA.setStatoFattura(stato);
                    notificaFatturaPA.setEsitoPositivo(getEsitoNotifica(stato, rootElement));

                    areaMagazzinoFatturaPA.setNotificaCorrente(notificaFatturaPA);
                    areaMagazzinoFatturaPA.setIdentificativoSDI(new BigInteger(identificativoSdI));

                    fatturePAManager.salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);

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
        } catch (Exception e) {
            LOGGER.error("--> errore durante l'importazione della mail di notifica del SdI", e);
            throw new RuntimeException("errore durante l'importazione della mail di notifica del SdI", e);
        }
    }

    private boolean notificaFileNameMatcher(String fileName) {
        return !StringUtils.isBlank(fileName) && fileName.matches(XMLREGEXPATTERN);
    }

    /**
     * Esegue la ricerca dei messaggi in base al mittente e alle date (oggi e oggi-1).
     *
     * @param inboxFolder
     *            inbox folder
     * @param fromFilter
     *            mittente da filtrare
     * @param numeroGiorniControllo
     *            numero giorni da cui far partire il controllo ( data odierna - giorni di controllo )
     * @return messaggi trovati
     * @throws MessagingException
     *             MessagingException
     */
    private Message[] searchMessages(Folder inboxFolder, String fromFilter, Integer numeroGiorniControllo)
            throws MessagingException {

        // filtro da oggi a oggi-1
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ReceivedDateTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LT, calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -numeroGiorniControllo);
        ReceivedDateTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, calendar.getTime());

        // filtro per il mittente
        FromStringTerm fromStringTerm = new FromStringTerm(fromFilter);

        AndTerm andTerm = new AndTerm(new SearchTerm[] { newerThan, olderThan, fromStringTerm });

        LOGGER.debug("--> Filtro i messaggi da " + DateFormatUtils.format(newerThan.getDate(), "dd/MM/yyy") + " a "
                + DateFormatUtils.format(olderThan.getDate(), "dd/MM/yyy") + " ricevute da " + fromFilter);

        Message[] messages = null;
        messages = inboxFolder.search(andTerm);

        LOGGER.debug("--> Messaggi trovati: " + (messages != null ? String.valueOf(messages.length) : "0"));

        return messages;
    }

}
