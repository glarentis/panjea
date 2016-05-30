package it.eurotn.panjea.corrispettivi.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Leonardo
 */
public class TotaliCodiceIvaDTO implements Serializable {

    private static final long serialVersionUID = -2979471972821425746L;

    private String codiceIva;
    private String descrizioneRegistro;

    private BigDecimal totale;

    private boolean filler;

    /**
     * Costruttore.
     *
     * @param codiceIva
     *            codiceIva
     * @param descrizioneRegistro
     *            descrizioneRegistro
     * @param totale
     *            totale
     */
    public TotaliCodiceIvaDTO(final String codiceIva, final String descrizioneRegistro, final BigDecimal totale) {
        super();
        this.codiceIva = codiceIva;
        this.descrizioneRegistro = descrizioneRegistro;
        this.totale = totale;
        this.filler = Boolean.FALSE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getCodiceIva() == null) {
            return false;
        }
        if (!(obj instanceof TotaliCodiceIvaDTO)) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        return (this.getCodiceIva().equals(((TotaliCodiceIvaDTO) obj).getCodiceIva()));
    }

    /**
     * @return the codice
     */
    public String getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return the descrizioneRegistro
     */
    public String getDescrizioneRegistro() {
        return descrizioneRegistro;
    }

    /**
     * @return the totale
     */
    public BigDecimal getTotale() {
        return totale;
    }

    @Override
    public int hashCode() {
        if (codiceIva != null) {
            String hashStr = this.getClass().getName() + ":" + codiceIva;
            return hashStr.hashCode();
        }
        return super.hashCode();
    }

    /**
     * @return the filler
     */
    public boolean isFiller() {
        return filler;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    public void setCodiceIva(String codiceIva) {
        this.codiceIva = codiceIva;
    }

    /**
     * @param descrizioneRegistro
     *            the descrizioneRegistro to set
     */
    public void setDescrizioneRegistro(String descrizioneRegistro) {
        this.descrizioneRegistro = descrizioneRegistro;
    }

    /**
     * @param filler
     *            the filler to set
     */
    public void setFiller(boolean filler) {
        this.filler = filler;
    }

    /**
     * @param totale
     *            the totale to set
     */
    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TotaliCodiceIvaDTO[");
        buffer.append(" totale = ").append(totale);
        buffer.append(" codiceIva = ").append(codiceIva);
        buffer.append(" descrizioneRegistro = ").append(descrizioneRegistro);
        buffer.append("]");
        return buffer.toString();
    }

}
