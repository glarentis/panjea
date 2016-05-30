package it.eurotn.panjea.anagrafica.domain;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;

public class ParametriMail implements Serializable {

    private static final long serialVersionUID = 6658259682026351196L;
    // destinatari
    private Set<Destinatario> destinatari;
    // oggetto della mail
    private String oggetto;

    // testo del mail
    private String testo;

    private String nota;

    private String nomeAllegato;

    // path di file attach se ci sono
    private Map<String, String> attachments = null;

    // se presente viene usato il suo valore come filtro da applicare all'apertura delal rubrica
    private String filtroRubrica;

    private DatiMail datiMail;

    private boolean notificaLettura;

    /**
     * Costruttore.
     */
    public ParametriMail() {
        super();
        this.destinatari = new TreeSet<Destinatario>();
        this.attachments = new HashMap<String, String>();
    }

    /**
     * @param attachment
     *            The attachment full path da aggiungere
     * @param attachmentName
     *            attachmentName per personalizzare il nome o null nel caso si voglia mantenere il nome del file come
     *            nome allegato
     */
    public void addAttachments(String attachment, String attachmentName) {
        this.attachments.put(attachment, attachmentName);
    }

    /**
     * @param utente
     *            utente che spedisce la mail
     * @return set contenente le mail create basandosi sui dati settati nei parametri
     */
    public Set<Mail> createEmails(Utente utente) {
        // Creo un'istanza di mail per ogni indirizzo
        // e faccio lo streaming dei file sull'oggetto

        // Creo gli array di byte che poi referenzio nelle le istanze delle mail create
        Map<String, byte[]> filesMap = new HashMap<String, byte[]>();
        Map<String, String> filesName = new HashMap<String, String>();

        for (String filePath : attachments.keySet()) {
            try {
                // se il file che voglio allegare nella mail è un file temporaneo lo rinomino con quello indica nei
                // parametri mail
                File fileTmp = new File(filePath);
                File file = new File(filePath);
                if (System.getProperty("java.io.tmpdir").equals(file.getParent())) {
                    int dotIdx = filePath.lastIndexOf(".");
                    String extension = filePath.substring(dotIdx + 1);
                    file = new File(file.getParent() + File.separator + attachments.get(filePath) + "." + extension);
                }
                filesName.put(fileTmp.getAbsolutePath(), attachments.get(filePath));
                filesMap.put(fileTmp.getAbsolutePath(), FileUtils.readFileToByteArray(fileTmp));
            } catch (IOException e) {
                throw new RuntimeException("Errore durante la luttura del file " + filePath);
            }
        }
        Set<Mail> mails = new HashSet<Mail>();
        for (Destinatario destinatario : destinatari) {
            if (destinatario.getEmail() != null && !destinatario.getEmail().isEmpty()) {
                Mail mail = new Mail();
                mail.setDestinatario(destinatario);
                mail.setNota(nota);
                mail.setOggetto(oggetto);
                mail.setTesto(testo);
                mail.setAllegati(filesMap);
                mail.setNomiAllegati(filesName);
                mail.setUtenteDiSpedizione(utente);
                mails.add(mail);
            }
        }
        return mails;
    }

    /**
     * @return Returns the sttachments.
     */
    public Map<String, String> getAttachments() {
        return attachments;
    }

    /**
     * @return the datiMail
     */
    public DatiMail getDatiMail() {
        return datiMail;
    }

    /**
     * @return Returns the destinatari.
     */
    public Set<Destinatario> getDestinatari() {
        return destinatari;
    }

    /**
     * @return the filtroRubrica
     */
    public String getFiltroRubrica() {
        return filtroRubrica;
    }

    /**
     * @return Returns the nomeAllegato.
     */
    public String getNomeAllegato() {
        return nomeAllegato;
    }

    /**
     * @return Returns the nota.
     */
    public String getNota() {
        return nota;
    }

    /**
     * @return the oggetto
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
     * @return true se la mail contiene un allegato.
     */
    public boolean hasAttachment() {
        return attachments != null && !attachments.isEmpty();
    }

    /**
     * @return the notificaLettura
     */
    public boolean isNotificaLettura() {
        return notificaLettura;
    }

    /**
     * @param attachments
     *            The attachments to set.
     */
    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    /**
     * @param datiMail
     *            the datiMail to set
     */
    public void setDatiMail(DatiMail datiMail) {
        this.datiMail = datiMail;

        notificaLettura = false;
        if (datiMail != null) {
            notificaLettura = datiMail.isNotificaLettura();
        }
    }

    /**
     * @param destinatari
     *            The destinatari to set.
     */
    public void setDestinatari(Set<Destinatario> destinatari) {
        this.destinatari = destinatari;
    }

    /**
     * @param filtroRubrica
     *            the filtroRubrica to set
     */
    public void setFiltroRubrica(String filtroRubrica) {
        this.filtroRubrica = filtroRubrica;
    }

    /**
     * @param nomeAllegato
     *            The nomeAllegato to set.
     */
    public void setNomeAllegato(String nomeAllegato) {
        this.nomeAllegato = nomeAllegato;
    }

    /**
     * @param nota
     *            The nota to set.
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @param notificaLettura
     *            the notificaLettura to set
     */
    public void setNotificaLettura(boolean notificaLettura) {
        this.notificaLettura = notificaLettura;
    }

    /**
     * @param oggetto
     *            the oggetto to set
     */
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    /**
     * @param testo
     *            the testo to set
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Mail[");
        buffer.append(super.toString());
        buffer.append(" oggetto = ").append(oggetto);
        buffer.append(" testo = ").append(testo);
        buffer.append("]");
        return buffer.toString();
    }
}
