package it.eurotn.panjea.magazzino.domain.descrizionilingua;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;

/**
 * Classe di dominio che rappresenta le informazioni della Categoria dipendenti dalla lingua es: descrizione in lingua
 * della {@link Categoria} , maschera per la generazione della descrizione articolo.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_informazioni_lingua_categoria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "categoria")
public class InformazioneLinguaCategoria extends EntityBase implements IDescrizioneLingua {

    private static final long serialVersionUID = -8600469794461593121L;

    /**
     * @uml.property name="codiceLingua"
     */
    private String codiceLingua;

    /**
     * @uml.property name="descrizione"
     */
    private String descrizione;

    /**
     * @uml.property name="mascheraDescrizioneArticolo"
     */
    private String mascheraDescrizioneArticolo;

    @Override
    public String getCodiceLingua() {
        return codiceLingua;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * definizione della maschera da utilizzare per la generazione della descrizione dell'articolo nella lingua di
     * codiceLingua la maschera permette di comporre la descrizione utilizzando i codici di {@link TipoAttributo}
     * all'interno di {@link Categoria} delimitati dal segno minore e maggiore es: colore <codice tipo attributo colore>
     * taglia <codice tipo attributo taglia>.
     * 
     * @return Returns the mascheraDescrizioneArticolo.
     * @uml.property name="mascheraDescrizioneArticolo"
     */
    public String getMascheraDescrizioneArticolo() {
        return mascheraDescrizioneArticolo;
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
     * @param mascheraDescrizioneArticolo
     *            The mascheraDescrizioneArticolo to set.
     * @uml.property name="mascheraDescrizioneArticolo"
     */
    public void setMascheraDescrizioneArticolo(String mascheraDescrizioneArticolo) {
        this.mascheraDescrizioneArticolo = mascheraDescrizioneArticolo;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     * 
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("InformazioneLinguaCategoria[ ").append(super.toString()).append(" codiceLingua = ")
                .append(this.codiceLingua).append(" descrizione = ").append(this.descrizione)
                .append(" mascheraDescrizioneArticolo = ").append(this.mascheraDescrizioneArticolo).append(" ]");

        return retValue.toString();
    }

}
