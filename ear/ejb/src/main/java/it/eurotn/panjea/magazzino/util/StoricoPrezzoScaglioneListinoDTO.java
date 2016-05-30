package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fattazzo
 *
 */
public class StoricoPrezzoScaglioneListinoDTO implements Serializable {

    private static final long serialVersionUID = 8765289794598743498L;

    private Integer idRigaListino;
    private Integer idScaglione;

    private Date data;

    private Integer numeroDecimaliPrezzo;
    private BigDecimal prezzo;

    private String nota;

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the idRigaListino
     */
    public Integer getIdRigaListino() {
        return idRigaListino;
    }

    /**
     * @return the idScaglione
     */
    public Integer getIdScaglione() {
        return idScaglione;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the prezzo
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param idRigaListino
     *            the idRigaListino to set
     */
    public void setIdRigaListino(Integer idRigaListino) {
        this.idRigaListino = idRigaListino;
    }

    /**
     * @param idScaglione
     *            the idScaglione to set
     */
    public void setIdScaglione(Integer idScaglione) {
        this.idScaglione = idScaglione;
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }
}
