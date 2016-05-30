package it.eurotn.panjea.anagrafica.util;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.logicaldoc.webservice.WSAttribute;
import com.logicaldoc.webservice.document.WSDocument;

public class MailDTO implements Serializable {

    private static final long serialVersionUID = 7165220238023688995L;

    private Date data;

    private String emailDestinazione;

    private EntitaDocumento entitaDestinazione;

    private String oggetto;

    private boolean successo;

    private String emailDiSpedizione;

    private WSDocument wsDocument;

    /**
     * Costruttore.
     */
    public MailDTO() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param emailDocument
     *            email document
     */
    public MailDTO(final WSDocument emailDocument) {
        this.successo = !"Non inviata".equals(StringUtils.defaultString(emailDocument.getSourceType()));
        try {
            this.data = DateUtils.parseDate(emailDocument.getSourceDate(), "yyyy-MM-dd hh:mm:ss z");
        } catch (ParseException e) {
            this.data = null;
        }
        this.emailDiSpedizione = emailDocument.getSource();
        this.emailDestinazione = emailDocument.getRecipient();
        this.oggetto = emailDocument.getObject();
        // nel caso della mail ho un solo wsattribute possibile che è quello dell'entità
        if (emailDocument.getExtendedAttributes() != null && emailDocument.getExtendedAttributes().length == 1) {
            WSAttribute entAttribute = emailDocument.getExtendedAttributes()[0];

            // il valore dell'entità è fatto da codice|tipo|denominazione|id
            String[] entSplit = StringUtils.split(entAttribute.getStringValue(), "|");

            EntitaDocumento entita = new EntitaDocumento();
            entita.setTipoEntita(entSplit[1]);
            entita.setCodice(Integer.valueOf(entSplit[0]));
            entita.setDescrizione(entSplit[2]);
            entita.setId(Integer.valueOf(entSplit[3]));
            this.entitaDestinazione = entita;
        }

        this.wsDocument = emailDocument;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the emailDestinazione
     */
    public String getEmailDestinazione() {
        return emailDestinazione;
    }

    /**
     * @return the emailDiSpedizione
     */
    public String getEmailDiSpedizione() {
        return emailDiSpedizione;
    }

    /**
     * @return the entitaDestinazione
     */
    public EntitaDocumento getEntitaDestinazione() {
        return entitaDestinazione;
    }

    /**
     * @return the oggetto
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * @return the wsDocument
     */
    public WSDocument getWsDocument() {
        return wsDocument;
    }

    /**
     * @return the successo
     */
    public boolean isSuccesso() {
        return successo;
    }

}