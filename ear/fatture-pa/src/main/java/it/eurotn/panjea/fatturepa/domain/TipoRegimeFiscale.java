package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "ftpa_regi_fisc")
public class TipoRegimeFiscale implements Serializable {

    private static final long serialVersionUID = -7717197426026177086L;

    @Id
    @Column(name = "CODI_REGI", nullable = false)
    private Integer codice;
    @Column(name = "CODI_XXML", nullable = false)
    private String label;

    @Column(name = "DESC_REGI", nullable = false)
    private String descrizione;

    /**
     * @return the codice
     */
    public Integer getCodice() {
        return codice;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

}
