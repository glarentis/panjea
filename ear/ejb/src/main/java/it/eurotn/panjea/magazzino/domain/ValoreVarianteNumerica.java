package it.eurotn.panjea.magazzino.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 */
@Entity
@Audited
@DiscriminatorValue("VN")
public class ValoreVarianteNumerica extends ValoreVariante {

    private static final long serialVersionUID = 7646253224068775898L;

    /**
     * @uml.property name="valore"
     */
    private Double valore;

    @Override
    public Object getValore() {
        return valore;
    }

    /**
     * Metodo utilizzato per bindare la proprietà nel form in quanto utilizzando il getValore della classe il conversion
     * service non riesce a trovare nessun conveter da Object a String.
     * 
     * @return valore
     */
    public Double getValoreDouble() {
        return valore;
    }

    /**
     * @param valore
     *            the valore to set
     * @uml.property name="valore"
     */
    public void setValore(Double valore) {
        this.valore = valore;
        this.setCodice(valore.toString());
    }

    /**
     * Metodo utilizzato per bindare la proprietà nel form in quanto utilizzando il setValore della classe il conversion
     * service non riesce a trovare nessun conveter da Object a String.
     * 
     * @param valoreDouble
     *            the valore to set
     */
    public void setValoreDouble(Double valoreDouble) {
        setValore(valoreDouble);
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     * 
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("ValoreVarianteNumerica[ ").append(super.toString()).append(" valore = ").append(this.valore)
                .append(" ]");

        return retValue.toString();
    }

}
