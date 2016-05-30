package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.rulesvalidation.AbstractRigaArticoloRulesValidation;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ParametriRegoleValidazioneRighe implements Serializable {

    private static final long serialVersionUID = -3389662182324387821L;

    /**
     * @uml.property name="areeMagazzino"
     */
    private List<AreaMagazzinoLite> areeMagazzino;

    /**
     * @uml.property name="regole"
     */
    private List<AbstractRigaArticoloRulesValidation> regole;

    /**
     * Costruttore di default.
     */
    public ParametriRegoleValidazioneRighe() {
        super();
        this.areeMagazzino = new ArrayList<AreaMagazzinoLite>();
        this.regole = new ArrayList<AbstractRigaArticoloRulesValidation>();
    }

    /**
     * Aggiunge una lista di regole di validazione alla lista di regole interna.
     * 
     * @param paramRegole
     *            Regole da aggiungere
     */
    public void addAllRegole(final Collection<AbstractRigaArticoloRulesValidation> paramRegole) {
        this.regole.addAll(paramRegole);
    }

    /**
     * Aggiunge una regola di validazione alla lista delle regole interna.
     * 
     * @param regola
     *            Regola da aggiungere
     */
    public void addToRegole(AbstractRigaArticoloRulesValidation regola) {
        this.regole.add(regola);
    }

    /**
     * @return the areeMagazzino
     * @uml.property name="areeMagazzino"
     */
    public List<AreaMagazzinoLite> getAreeMagazzino() {
        return areeMagazzino;
    }

    /**
     * @return the regole
     * @uml.property name="regole"
     */
    public List<AbstractRigaArticoloRulesValidation> getRegole() {
        return regole;
    }

    /**
     * @param areeMagazzino
     *            the areeMagazzino to set
     * @uml.property name="areeMagazzino"
     */
    public void setAreeMagazzino(List<AreaMagazzinoLite> areeMagazzino) {
        this.areeMagazzino = areeMagazzino;
    }

    /**
     * @param regole
     *            the regole to set
     * @uml.property name="regole"
     */
    public void setRegole(List<AbstractRigaArticoloRulesValidation> regole) {
        this.regole = regole;
    }

}
