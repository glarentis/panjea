package it.eurotn.panjea.magazzino.util;

import java.math.BigDecimal;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;

/**
 * @author fattazzo
 *
 */
public class ScaglioneListinoDTO {

    private RigaListino rigaListino;

    private ScaglioneListino scaglioneListino;

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return rigaListino.getArticolo();
    }

    /**
     * @return the rigaListino
     */
    public RigaListino getRigaListino() {
        return rigaListino;
    }

    /**
     * @return the scaglioneListino
     */
    public ScaglioneListino getScaglioneListino() {
        return scaglioneListino;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.rigaListino.setArticolo(new ArticoloLite());
        this.rigaListino.getArticolo().setId(idArticolo);
    }

    /**
     * @param idRigaListino
     *            the idRigaListino to set
     */
    public void setIdRigaListino(Integer idRigaListino) {
        this.rigaListino = new RigaListino();
        this.rigaListino.setId(idRigaListino);
    }

    /**
     * @param idScaglioneListino
     *            the idScaglioneListino to set
     */
    public void setIdScaglioneListino(Integer idScaglioneListino) {
        this.scaglioneListino.setId(idScaglioneListino);
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.scaglioneListino.setNota(note);
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.rigaListino.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzo(BigDecimal prezzo) {
        this.scaglioneListino.setPrezzo(prezzo);
    }

    /**
     * @param quantita
     *            the quantita to set
     */
    public void setQuantita(Double quantita) {
        this.scaglioneListino = new ScaglioneListino();
        this.scaglioneListino.setRigaListino(rigaListino);
        this.scaglioneListino.setQuantita(quantita);
    }

    /**
     * @param versionArticolo
     *            the versionArticolo to set
     */
    public void setVersionArticolo(Integer versionArticolo) {
        this.rigaListino.getArticolo().setVersion(versionArticolo);
    }

    /**
     * @param versionRigaListino
     *            the versionRigaListino to set
     */
    public void setVersionRigaListino(Integer versionRigaListino) {
        this.rigaListino.setVersion(versionRigaListino);
    }

}
