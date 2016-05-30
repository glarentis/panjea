package it.eurotn.panjea.magazzino.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaNotaDTO;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Riga contenuta in un area di magazzino. <br>
 * Contiene solamente delle note da aggiungere al corpo del documento.
 *
 * @author giangi
 */
@Entity
@Audited
@DiscriminatorValue("N")
public class RigaNota extends RigaMagazzino {

    private static final long serialVersionUID = 3195114942170575185L;

    private static final Logger LOGGER = Logger.getLogger(RigaNota.class);

    @Lob
    private String nota;

    {
        this.nota = "";
    }

    @Override
    protected RigaMagazzinoDTO creaIstanzaRigaMagazzinoDTO() {
        return new RigaNotaDTO();
    }

    @Override
    public RigaMagazzino creaRigaCollegata(AreaMagazzino areaMagazzino, double ordinamentoRigaCollagata) {
        LOGGER.debug("--> Enter creaRigaCollegata");

        RigaNota rigaNotaCollegata = new RigaNota();
        rigaNotaCollegata.setAreaMagazzino(areaMagazzino);
        rigaNotaCollegata.setAreaMagazzinoCollegata(this.getAreaMagazzino());
        rigaNotaCollegata.setLivello(this.getLivello() + 1);
        rigaNotaCollegata.setNota(this.getNota());
        rigaNotaCollegata.setOrdinamento(ordinamentoRigaCollagata);
        rigaNotaCollegata.setRigaMagazzinoCollegata(this);
        rigaNotaCollegata.setRigaAutomatica(false);

        LOGGER.debug("--> Exit creaRigaCollegata");

        return rigaNotaCollegata;
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
        StringBuilder stringBuffer = new StringBuilder();
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

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua,
            boolean escludiTagHtml) {
        String descrizioneRiga = getDescrizioneRiga(stampaAttributi, stampaNote, lingua);
        if (escludiTagHtml) {
            descrizioneRiga = PanjeaEJBUtil.removeHtml(descrizioneRiga);
        }
        return descrizioneRiga;
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua, boolean escludiTagHtml,
            boolean stampaConai) {
        return getDescrizioneRiga(stampaAttributi, stampaNote, lingua, escludiTagHtml);
    }

    /**
     * @return nota
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
        StringBuilder retValue = new StringBuilder();
        retValue.append("RigaNota[ ").append(super.toString()).append(" ]");
        return retValue.toString();
    }

}