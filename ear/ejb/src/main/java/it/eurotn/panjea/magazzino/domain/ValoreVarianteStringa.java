package it.eurotn.panjea.magazzino.domain;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaTipoVariante;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;

/**
 * @author fattazzo
 */
@Entity
@Audited
@DiscriminatorValue("VS")
public class ValoreVarianteStringa extends ValoreVariante implements IDescrizioneFactory {

    private static final long serialVersionUID = 6873907366559414271L;

    /**
     * @uml.property name="valoriLingua"
     */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @MapKey(name = "codiceLingua")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Map<String, DescrizioneLinguaTipoVariante> valoriLingua;

    /**
     * @uml.property name="valoreLinguaAziendale"
     */
    private String valoreLinguaAziendale; // valore in lingua aziendale

    /**
     * Aggiunge una descrizione in lungua alla variante.
     * 
     * @param lingua
     *            descrizione in lingua
     */
    public void addValore(DescrizioneLinguaTipoVariante lingua) {
        if (valoriLingua == null) {
            valoriLingua = new HashMap<String, DescrizioneLinguaTipoVariante>();
        }
        valoriLingua.put(lingua.getCodiceLingua(), lingua);
    }

    @Override
    public IDescrizioneLingua createDescrizioneLingua() {
        return new DescrizioneLinguaTipoVariante();
    }

    @Override
    public String getValore() {
        String lingua = Locale.getDefault().getLanguage();
        String valore = null;
        if (valoriLingua != null && valoriLingua.containsKey(lingua)) {
            valore = valoriLingua.get(lingua).getDescrizione();
        }
        if (valore == null) {
            return valoreLinguaAziendale;
        }
        return valore;
    }

    /**
     * @return valoreLinguaAziendale
     * @uml.property name="valoreLinguaAziendale"
     */
    public String getValoreLinguaAziendale() {
        return valoreLinguaAziendale;
    }

    /**
     * @return valoriLingua
     * @uml.property name="valoriLingua"
     */
    public Map<String, DescrizioneLinguaTipoVariante> getValoriLingua() {
        return valoriLingua;
    }

    /**
     * @param valoreLinguaAziendale
     *            the valoreLinguaAziendale to set
     * @uml.property name="valoreLinguaAziendale"
     */
    public void setValoreLinguaAziendale(String valoreLinguaAziendale) {
        this.valoreLinguaAziendale = valoreLinguaAziendale;
    }

    /**
     * @param valoriLingua
     *            the valoriLingua to set
     * @uml.property name="valoriLingua"
     */
    public void setValoriLingua(Map<String, DescrizioneLinguaTipoVariante> valoriLingua) {
        this.valoriLingua = valoriLingua;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     * 
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("ValoreVarianteStringa[ ").append(super.toString()).append(" valoriLingua = ")
                .append(this.valoriLingua).append(" valoreLinguaAziendale = ").append(this.valoreLinguaAziendale)
                .append(" ]");

        return retValue.toString();
    }

}
