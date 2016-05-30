package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.SedeAnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_sedi_magazzino")
@NamedQueries({
        @NamedQuery(name = "SedeMagazzinoLite.caricaSediMagazzinoByEntita", query = " select sede from SedeMagazzinoLite as sede where sede.sedeEntita.entita.id = :paramEntitaId and sede.sedeEntita.abilitato = true and sede.sedeEntita.tipoSede.tipoSede<2 ") })
public class SedeMagazzinoLite extends EntityBase {

    private static final long serialVersionUID = 306329592949597273L;

    /**
     * @uml.property name="sedeEntita"
     * @uml.associationEnd
     */
    @OneToOne
    private SedeEntitaLite sedeEntita;

    /**
     * @uml.property name="sedeDiRifatturazione"
     */
    private boolean sedeDiRifatturazione;

    /**
     * @uml.property name="calcoloSpese"
     */
    private boolean calcoloSpese;

    /**
     * @uml.property name="sedePerRifatturazione"
     * @uml.associationEnd
     */
    @OneToOne
    private SedeMagazzinoLite sedePerRifatturazione;

    {
        sedeEntita = new SedeEntitaLite();
        sedeEntita.setSede(new SedeAnagraficaLite());
    }

    /**
     * Costruttore.
     */
    public SedeMagazzinoLite() {
        this.sedePerRifatturazione = new SedeMagazzinoLite(null);
    }

    /**
     * HACK costruttore utilizzato per istanziare la sedeMagazzinoRifatturazione senza incorrere in un loop infinito.
     * 
     * @param sedeMagazzinoRifatturazione
     *            sedeMagazzinoRifatturazione
     */
    public SedeMagazzinoLite(final SedeMagazzinoLite sedeMagazzinoRifatturazione) {
        this.sedePerRifatturazione = sedeMagazzinoRifatturazione;
    }

    /**
     * @return the sedeEntita
     * @uml.property name="sedeEntita"
     */
    public SedeEntitaLite getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the sedePerRifatturazione
     * @uml.property name="sedePerRifatturazione"
     */
    public SedeMagazzinoLite getSedePerRifatturazione() {
        return sedePerRifatturazione;
    }

    /**
     * @return the calcoloSpese
     * @uml.property name="calcoloSpese"
     */
    public boolean isCalcoloSpese() {
        return calcoloSpese;
    }

    /**
     * @return the sedeDiRifatturazione
     * @uml.property name="sedeDiRifatturazione"
     */
    public boolean isSedeDiRifatturazione() {
        return sedeDiRifatturazione;
    }

    /**
     * @param calcoloSpese
     *            the calcoloSpese to set
     * @uml.property name="calcoloSpese"
     */
    public void setCalcoloSpese(boolean calcoloSpese) {
        this.calcoloSpese = calcoloSpese;
    }

    /**
     * @param sedeDiRifatturazione
     *            the sedeDiRifatturazione to set
     * @uml.property name="sedeDiRifatturazione"
     */
    public void setSedeDiRifatturazione(boolean sedeDiRifatturazione) {
        this.sedeDiRifatturazione = sedeDiRifatturazione;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     * @uml.property name="sedeEntita"
     */
    public void setSedeEntita(SedeEntitaLite sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param sedePerRifatturazione
     *            the sedePerRifatturazione to set
     * @uml.property name="sedePerRifatturazione"
     */
    public void setSedePerRifatturazione(SedeMagazzinoLite sedePerRifatturazione) {
        this.sedePerRifatturazione = sedePerRifatturazione;
    }

}
