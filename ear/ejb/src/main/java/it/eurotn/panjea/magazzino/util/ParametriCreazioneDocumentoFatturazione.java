package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ParametriCreazioneDocumentoFatturazione implements Serializable {

    private static final long serialVersionUID = 8496573348543582722L;

    /**
     * @uml.property name="dataDocumento"
     */
    private Date dataDocumento;

    /**
     * @uml.property name="note"
     */
    private String note;

    /**
     * @return the dataDocumento
     */
    public Date getDataDocumento() {
        return dataDocumento;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

}
