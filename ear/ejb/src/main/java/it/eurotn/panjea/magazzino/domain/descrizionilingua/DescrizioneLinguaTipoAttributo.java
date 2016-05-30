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
@Table(name = "maga_descrizioni_lingua_tipo_attributo")
public class DescrizioneLinguaTipoAttributo extends EntityBase implements IDescrizioneLingua {

    private static final long serialVersionUID = 6822065835437432619L;

    private String codiceLingua;

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

}
