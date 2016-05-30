package it.eurotn.panjea.anagrafica.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.annotations.Index;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.sicurezza.domain.Utente;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Table(name = "anag_mail")
@NamedQueries({
        @NamedQuery(name = "Mail.caricaMailByDataEntita", query = "select m from Mail m left join fetch m.destinatario.entita where m.timeStamp>=:inizio and m.timeStamp<=:fine and m.destinatario.entita=:entita"),
        @NamedQuery(name = "Mail.caricaMailByData", query = "select m from Mail m left join fetch m.destinatario.entita where m.timeStamp>=:inizio and m.timeStamp<=:fine"),
        @NamedQuery(name = "Mail.deleteMailById", query = "delete from Mail m where id = :paramId") })
public class Mail extends EntityBase {

    private static final long serialVersionUID = 7165220238023688995L;

    @Column(length = 6)
    @Index(name = "codice")
    private String codice;

    @Transient
    private Map<String, byte[]> allegati;

    @Transient
    private Map<String, String> nomiAllegati;

    private String nomeAllegati;

    @Embedded
    private Destinatario destinatario;

    @Column(name = "nota", length = 120)
    private String nota;
    // oggetto della mail
    @Column(length = 120)
    private String oggetto;

    @Column(name = "testo")
    @Lob
    private String testo;

    @Column(name = "tipo", length = 10)
    private String tipo;

    private boolean successo;

    @ManyToOne
    private Utente utenteDiSpedizione;

    /**
     * Costruttore.
     */
    public Mail() {
        allegati = new HashMap<String, byte[]>();
        nomeAllegati = null;
    }

    /**
     * @return Returns the allegati.
     */
    public Map<String, byte[]> getAllegati() {
        return allegati;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return la data di spedizione basata sul timestamp
     */
    public Date getData() {
        Date data = null;
        if (getTimeStamp() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getTimeStamp());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            data = calendar.getTime();
        }
        return data;
    }

    /**
     * @return Returns the destinatario.
     */
    public Destinatario getDestinatario() {
        return destinatario;
    }

    /**
     * @return array contenente le estensioni degli allegati alla mail.
     */
    public String getNomeAllegati() {
        return nomeAllegati;
    }

    /**
     * @return the nomiAllegati
     */
    public Map<String, String> getNomiAllegati() {
        return nomiAllegati;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @return Returns the oggetto.
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * @return the testo
     */
    public String getTesto() {
        return testo;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the utenteDiSpedizione
     */
    public Utente getUtenteDiSpedizione() {
        return utenteDiSpedizione;
    }

    /**
     * @return true se la mail contiene allegati.
     */
    public boolean hasAttachment() {
        return allegati != null && !allegati.isEmpty();
    }

    /**
     * @return the successo
     */
    public boolean isSuccesso() {
        return successo;
    }

    /**
     * @param allegati
     *            The allegati to set.
     */
    public void setAllegati(Map<String, byte[]> allegati) {
        this.allegati = allegati;
        if (allegati != null && nomeAllegati == null) {
            StringBuilder sb = new StringBuilder();
            for (String pathTemp : allegati.keySet()) {
                sb.append(FilenameUtils.getName(pathTemp));
                sb.append("#");
            }
            nomeAllegati = sb.toString();
        }
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param destinatario
     *            The destinatario to set.
     */
    public void setDestinatario(Destinatario destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * @param nomiAllegati
     *            the nomiAllegati to set
     */
    public void setNomiAllegati(Map<String, String> nomiAllegati) {
        this.nomiAllegati = nomiAllegati;
        if (nomiAllegati != null) {
            StringBuilder sb = new StringBuilder();
            for (String pathTemp : nomiAllegati.keySet()) {
                sb.append(FilenameUtils.getName(pathTemp));
                sb.append("#");
            }
            nomeAllegati = sb.toString();
        }
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @param oggetto
     *            The oggetto to set.
     */
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    /**
     * @param successo
     *            the successo to set
     */
    public void setSuccesso(boolean successo) {
        this.successo = successo;
    }

    /**
     * @param testo
     *            the testo to set
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * @param tipo
     *            the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @param utenteDiSpedizione
     *            the utenteDiSpedizione to set
     */
    public void setUtenteDiSpedizione(Utente utenteDiSpedizione) {
        this.utenteDiSpedizione = utenteDiSpedizione;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Mail[");
        buffer.append(super.toString());
        buffer.append(" nota = ").append(nota);
        buffer.append(" testo = ").append(testo);
        buffer.append(" successo = ").append(successo);
        buffer.append(" nota = ").append(nota);
        buffer.append("]");
        return buffer.toString();
    }

}