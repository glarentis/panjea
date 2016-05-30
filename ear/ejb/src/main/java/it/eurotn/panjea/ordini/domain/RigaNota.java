package it.eurotn.panjea.ordini.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.ordini.util.RigaNotaDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Riga contenuta in un area ordine. <br>
 * Contiene solamente delle note da aggiungere al corpo del documento.
 *
 * @author giangi
 */
@Entity(name = "RigaNotaOrdine")
@DiscriminatorValue("N")
public class RigaNota extends RigaOrdine {

    private static final long serialVersionUID = 252639083686746282L;
    /**
     * @uml.property name="nota"
     */
    @Lob
    private String nota;

    {
        this.nota = "";
    }

    @Override
    protected RigaOrdineDTO creaIstanzaRigaOrdineDTO() {
        return new RigaNotaDTO();
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
        // StringBuffer stringBuffer = new StringBuffer();
        // if (stampaNote && getNota() != null) {
        // stringBuffer.append("<i>");
        //
        // String noteRiga = getNota();
        //
        // if (noteRiga.indexOf("<head>") != -1) {
        // noteRiga = noteRiga.replaceAll("<head>", "");
        // noteRiga = noteRiga.replaceAll("</head>", "");
        // }
        //
        // if (noteRiga.indexOf("<p style=\"margin-top: 0\">") != -1) {
        // noteRiga = noteRiga.replaceFirst("<p style=\"margin-top: 0\">", "");
        // }
        //
        // if (noteRiga.indexOf("</p>") != -1) {
        // noteRiga = noteRiga.replaceFirst("</p>", "<BR>");
        // }
        //
        // stringBuffer.append(noteRiga);
        // stringBuffer.append("</i>");
        // }
        // return stringBuffer.toString();
        StringBuffer stringBuffer = new StringBuffer();
        if (stampaNote && getNota() != null) {
            stringBuffer.append("<html><i>");
            String noteRiga = getNotePerStampa(lingua);
            if (noteRiga != null) {
                stringBuffer.append(noteRiga);
            }
            stringBuffer.append("</i></html>");
        }
        return stringBuffer.toString();
    }

    /**
     * @return the nota
     * @uml.property name="nota"
     */
    public String getNota() {
        return nota;
    }

    @Override
    public String getNotePerStampa(String lingua) {
        String noteRiga = getNota();
        if (noteRiga.indexOf("<html>") != -1) {
            String head = StringUtils.substringBetween(noteRiga, "<head>", "</head>");
            if (head != null) {
                noteRiga = noteRiga.replace(head, "");
            }
            noteRiga = noteRiga.replaceAll("<html>", "");
            noteRiga = noteRiga.replaceAll("</html>", "");
            noteRiga = noteRiga.replaceAll("<head>", "");
            noteRiga = noteRiga.replaceAll("</head>", "");
            noteRiga = noteRiga.replaceAll("<body>", "");
            noteRiga = noteRiga.replaceAll("</body>", "");
            noteRiga = noteRiga.replaceAll("<p style=\"margin-top: 0\">", "<div>");
            noteRiga = noteRiga.replaceAll("</p>", "</div>");
            noteRiga = noteRiga.replaceFirst("<div>", "");
            noteRiga = noteRiga.replaceAll("<div>", "<br>");
            noteRiga = noteRiga.replaceAll("</div>", "");
            noteRiga = noteRiga.trim();
        }
        return noteRiga;
    }

    /**
     * @param nota
     *            the nota to set
     * @uml.property name="nota"
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {
        StringBuffer retValue = new StringBuffer();
        retValue.append("RigaNota[ ").append(super.toString()).append(" ]");
        return retValue.toString();
    }

}