package it.eurotn.panjea.magazzino.domain.descrizionilingua;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_descrizioni_lingua_categoria_estesa")
public class DescrizioneLinguaCategoriaEstesa extends EntityBase implements IDescrizioneLingua {

    private static final long serialVersionUID = -3928752354931200824L;

    /**
     * @uml.property name="codiceLingua"
     */
    private String codiceLingua;
    /**
     * @uml.property name="descrizione"
     */
    private String descrizione;

    @Override
    public String getCodiceLingua() {
        return codiceLingua;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void setCodiceLingua(String codiceLingua) {
        this.codiceLingua = codiceLingua;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     * 
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("DescrizioneLinguaCategoriaEstesa[ ").append(super.toString()).append(" codiceLingua = ")
                .append(this.codiceLingua).append(" descrizione = ").append(this.descrizione).append(" ]");

        return retValue.toString();
    }

}
