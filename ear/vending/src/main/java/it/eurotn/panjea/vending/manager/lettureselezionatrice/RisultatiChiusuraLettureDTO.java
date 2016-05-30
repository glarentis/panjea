package it.eurotn.panjea.vending.manager.lettureselezionatrice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.list.UnmodifiableList;

import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;

public class RisultatiChiusuraLettureDTO implements Serializable {

    private static final long serialVersionUID = 6503540097330894386L;

    private List<LetturaSelezionatrice> lettureCassaGenerate = new ArrayList<>();

    private List<LetturaSelezionatrice> lettureRifornimentoAssociato = new ArrayList<>();
    private List<LetturaSelezionatrice> lettureRifornimentoNonAssociato = new ArrayList<>();

    private List<LetturaSelezionatrice> lettureRifornimentoCreato = new ArrayList<>();
    private List<LetturaSelezionatrice> lettureRifornimentoNonCreato = new ArrayList<>();

    private List<LetturaSelezionatrice> lettureNonValide = new ArrayList<>();

    /**
     * Costruttore.
     */
    public RisultatiChiusuraLettureDTO() {
        super();
    }

    /**
     * @param lettura
     *            lettura da aggiungere
     */
    public void addLetturaCassaGenerata(LetturaSelezionatrice lettura) {
        lettureCassaGenerate.add(lettura);
    }

    /**
     * @param lettura
     *            lettura da aggiungere
     */
    public void addLetturaNonValida(LetturaSelezionatrice lettura) {
        lettureNonValide.add(lettura);
    }

    /**
     * @param lettura
     *            lettura da aggiungere
     */
    public void addLetturaRifornimentoAssociato(LetturaSelezionatrice lettura) {
        lettureRifornimentoAssociato.add(lettura);
    }

    /**
     * @param lettura
     *            lettura da aggiungere
     */
    public void addLetturaRifornimentoCreato(LetturaSelezionatrice lettura) {
        lettureRifornimentoCreato.add(lettura);
    }

    /**
     * @param lettura
     *            lettura da aggiungere
     */
    public void addLetturaRifornimentoNonAssociato(LetturaSelezionatrice lettura) {
        lettureRifornimentoNonAssociato.add(lettura);
    }

    /**
     * @param lettura
     *            lettura da aggiungere
     */
    public void addLetturaRifornimentoNonCreato(LetturaSelezionatrice lettura) {
        lettureRifornimentoNonCreato.add(lettura);
    }

    /**
     * @return the lettureCassaGenerate
     */
    public List<LetturaSelezionatrice> getLettureCassaGenerate() {
        return new UnmodifiableList<LetturaSelezionatrice>(lettureCassaGenerate);
    }

    /**
     * @return the lettureNonValide
     */
    public List<LetturaSelezionatrice> getLettureNonValide() {
        return new UnmodifiableList<LetturaSelezionatrice>(lettureNonValide);
    }

    /**
     * @return the lettureRifornimentoAssociato
     */
    public List<LetturaSelezionatrice> getLettureRifornimentoAssociato() {
        return new UnmodifiableList<LetturaSelezionatrice>(lettureRifornimentoAssociato);
    }

    /**
     * @return the lettureRifornimentoCreato
     */
    public List<LetturaSelezionatrice> getLettureRifornimentoCreato() {
        return new UnmodifiableList<LetturaSelezionatrice>(lettureRifornimentoCreato);
    }

    /**
     * @return the lettureRifornimentoNonAssociato
     */
    public List<LetturaSelezionatrice> getLettureRifornimentoNonAssociato() {
        return new UnmodifiableList<LetturaSelezionatrice>(lettureRifornimentoNonAssociato);
    }

    /**
     * @return the lettureRifornimentoNonCreato
     */
    public List<LetturaSelezionatrice> getLettureRifornimentoNonCreato() {
        return new UnmodifiableList<LetturaSelezionatrice>(lettureRifornimentoNonCreato);
    }

    /**
     * @return totale delle letture presenti
     */
    public int getTotaleLetture() {
        return lettureCassaGenerate.size() + lettureRifornimentoAssociato.size()
                + lettureRifornimentoNonAssociato.size() + lettureRifornimentoCreato.size()
                + lettureRifornimentoNonCreato.size() + lettureNonValide.size();
    }

}
