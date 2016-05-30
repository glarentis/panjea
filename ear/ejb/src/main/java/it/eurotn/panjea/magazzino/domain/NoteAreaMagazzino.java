package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;

/**
 * Note del area magazzino.
 *
 * @author angelo
 */
public class NoteAreaMagazzino implements Serializable {

    private static final long serialVersionUID = 6669457638236073154L;
    /**
     * @uml.property name="noteSede"
     */
    private String noteSede = null;
    /**
     * @uml.property name="noteEntita"
     */
    private String noteEntita = null;
    /**
     * @uml.property name="noteBlocco"
     */
    private String noteBlocco = null;

    /**
     * @uml.property name="bloccato"
     */
    private boolean bloccato;

    /**
     * @return the noteBlocco
     * @uml.property name="noteBlocco"
     */
    public String getNoteBlocco() {
        return noteBlocco;
    }

    /**
     * @return the noteEntita
     * @uml.property name="noteEntita"
     */
    public String getNoteEntita() {
        return noteEntita;
    }

    /**
     * @return the noteSede
     * @uml.property name="noteSede"
     */
    public String getNoteSede() {
        return noteSede;
    }

    /**
     * @return the bloccato
     * @uml.property name="bloccato"
     */
    public boolean isBloccato() {
        return bloccato;
    }

    /**
     * @return the isEmpty
     */
    public boolean isEmpty() {
        boolean emptyNoteSede = false;
        boolean emptyNoteEntita = false;
        boolean emptyNoteBlocco = false;
        emptyNoteSede = (noteSede == null || noteSede.isEmpty());
        emptyNoteEntita = (noteEntita == null || noteEntita.isEmpty());
        emptyNoteBlocco = (noteBlocco == null || noteBlocco.isEmpty());
        return emptyNoteBlocco && emptyNoteEntita && emptyNoteSede;
    }

    /**
     * @param bloccato
     *            the bloccato to set
     * @uml.property name="bloccato"
     */
    public void setBloccato(boolean bloccato) {
        this.bloccato = bloccato;
    }

    /**
     * @param noteBlocco
     *            the noteBlocco to set
     * @uml.property name="noteBlocco"
     */
    public void setNoteBlocco(String noteBlocco) {
        this.noteBlocco = noteBlocco;
    }

    /**
     * @param noteEntita
     *            the noteEntita to set
     * @uml.property name="noteEntita"
     */
    public void setNoteEntita(String noteEntita) {
        this.noteEntita = noteEntita;
    }

    /**
     * @param noteSede
     *            the noteSede to set
     * @uml.property name="noteSede"
     */
    public void setNoteSede(String noteSede) {
        this.noteSede = noteSede;
    }

}
