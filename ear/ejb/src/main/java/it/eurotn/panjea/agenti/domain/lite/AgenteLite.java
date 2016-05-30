package it.eurotn.panjea.agenti.domain.lite;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

@Entity
@Audited
@DiscriminatorValue("A")
public class AgenteLite extends EntitaLite {

    public static final String TIPO = "A";

    private static final long serialVersionUID = -1486668492693374850L;

    private static int ordine = 3;

    @Transient
    private BigDecimal importoProvvigione;

    private Boolean fatturazioneAgente;

    /**
     * 
     * @return codice per l'esportazione di aton.
     */
    public String getCodiceAton() {
        NumberFormat format = new DecimalFormat("000");
        return format.format(getCodice());
    }

    /**
     * @return the importoProvvigione
     */
    public BigDecimal getImportoProvvigione() {
        return importoProvvigione;
    }

    /**
     * @return ordine di visualizzazione
     * @uml.property name="ordine"
     */
    @Override
    public int getOrdine() {
        return ordine;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    /**
     * @return Returns the fatturazioneAgente.
     */
    public Boolean isFatturazioneAgente() {
        if (fatturazioneAgente == null) {
            return Boolean.FALSE;
        }
        return fatturazioneAgente;
    }

    /**
     * metodo fake per l'esportazione con BeanIO.
     * 
     * @param codice
     *            codice
     */
    public void setCodiceAton(String codice) {
        throw new UnsupportedOperationException("Non usare, metodo fake per l'esportazione con BeanIO");
    }

    /**
     * @param fatturazioneAgente
     *            The fatturazioneAgente to set.
     */
    public void setFatturazioneAgente(Boolean fatturazioneAgente) {
        this.fatturazioneAgente = fatturazioneAgente;
    }

    /**
     * @param importoProvvigione
     *            the importoProvvigione to set
     */
    public void setImportoProvvigione(BigDecimal importoProvvigione) {
        this.importoProvvigione = importoProvvigione;
    }
}
