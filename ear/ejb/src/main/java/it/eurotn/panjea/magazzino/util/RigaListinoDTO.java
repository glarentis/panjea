package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fattazzo
 *
 */
public class RigaListinoDTO implements Serializable {

    private static final long serialVersionUID = 3142760005065005604L;

    private Integer id;

    private String categoria;

    private Integer idArticolo;

    private String codiceArticolo;

    private String descrizioneArticolo;
    private String barCodeArticolo;
    private Integer idCodiceIva;
    private String codiceCodiceIva;

    private Integer idUnitaMisura;
    private String codiceUnitaMisura;

    private BigDecimal prezzo;
    private Integer numeroDecimaliPrezzo;

    private Integer idVersioneListino;

    private Integer codiceVersioneListino;

    private Date dataVigoreVersioneListino;
    private Integer idListino;
    private String codiceListino;

    private String descrizioneListino;
    private BigDecimal ultimoCosto;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RigaListinoDTO other = (RigaListinoDTO) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @return the barCodeArticolo
     */
    public String getBarCodeArticolo() {
        return barCodeArticolo;
    }

    /**
     * @return Returns the categoria.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @return the codiceArticolo
     */
    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    /**
     * @return the codiceCodiceIva
     */
    public String getCodiceCodiceIva() {
        return codiceCodiceIva;
    }

    /**
     * @return the codiceListino
     */
    public String getCodiceListino() {
        return codiceListino;
    }

    /**
     * @return the codiceUnitaMisura
     */
    public String getCodiceUnitaMisura() {
        return codiceUnitaMisura;
    }

    /**
     * @return the codiceVersioneListino
     */
    public Integer getCodiceVersioneListino() {
        return codiceVersioneListino;
    }

    /**
     * @return the dataVigoreVersioneListino
     */
    public Date getDataVigoreVersioneListino() {
        return dataVigoreVersioneListino;
    }

    /**
     * @return the descrizioneArticolo
     */
    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    /**
     * @return the descrizioneListino
     */
    public String getDescrizioneListino() {
        return descrizioneListino;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the idArticolo
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return the idCodiceIva
     */
    public Integer getIdCodiceIva() {
        return idCodiceIva;
    }

    /**
     * @return the idListino
     */
    public Integer getIdListino() {
        return idListino;
    }

    /**
     * @return the idUnitaMisura
     */
    public Integer getIdUnitaMisura() {
        return idUnitaMisura;
    }

    /**
     * @return the idVersioneListino
     */
    public Integer getIdVersioneListino() {
        return idVersioneListino;
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
     * @return the ultimoCosto
     */
    public BigDecimal getUltimoCosto() {
        return ultimoCosto;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @param barCodeArticolo
     *            the barcodee articolo to set
     */
    public void setBarCodeArticolo(String barCodeArticolo) {
        this.barCodeArticolo = barCodeArticolo;
    }

    /**
     * @param categoria
     *            The categoria to set.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * @param codiceArticolo
     *            the codice articolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * @param codiceCodiceIva
     *            the codice codice iva to set
     */
    public void setCodiceCodiceIva(String codiceCodiceIva) {
        this.codiceCodiceIva = codiceCodiceIva;
    }

    /**
     * @param codiceListino
     *            the codiceListino to set
     */
    public void setCodiceListino(String codiceListino) {
        this.codiceListino = codiceListino;
    }

    /**
     * @param codiceUnitaMisura
     *            the codice unità di musura to set
     */
    public void setCodiceUnitaMisura(String codiceUnitaMisura) {
        this.codiceUnitaMisura = codiceUnitaMisura;
    }

    /**
     * @param codiceVersioneListino
     *            the codiceVersioneListino to set
     */
    public void setCodiceVersioneListino(Integer codiceVersioneListino) {
        this.codiceVersioneListino = codiceVersioneListino;
    }

    /**
     * @param dataVigoreVersioneListino
     *            the dataVigoreVersioneListino to set
     */
    public void setDataVigoreVersioneListino(Date dataVigoreVersioneListino) {
        this.dataVigoreVersioneListino = dataVigoreVersioneListino;
    }

    /**
     * @param descrizioneArticolo
     *            the descrizione articolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    /**
     * @param descrizioneListino
     *            the descrizioneListino to set
     */
    public void setDescrizioneListino(String descrizioneListino) {
        this.descrizioneListino = descrizioneListino;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param idArticolo
     *            the id articolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idCodiceIva
     *            the id codice iva to set
     */
    public void setIdCodiceIva(Integer idCodiceIva) {
        this.idCodiceIva = idCodiceIva;
    }

    /**
     * @param idListino
     *            the idListino to set
     */
    public void setIdListino(Integer idListino) {
        this.idListino = idListino;
    }

    /**
     * @param idUnitaMisura
     *            the id unità di musura to set
     */
    public void setIdUnitaMisura(Integer idUnitaMisura) {
        this.idUnitaMisura = idUnitaMisura;
    }

    /**
     * @param idVersioneListino
     *            the idVersioneListino to set
     */
    public void setIdVersioneListino(Integer idVersioneListino) {
        this.idVersioneListino = idVersioneListino;
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

    /**
     * @param ultimoCosto
     *            the ultimoCosto to set
     */
    public void setUltimoCosto(BigDecimal ultimoCosto) {
        this.ultimoCosto = ultimoCosto;
    }

}
