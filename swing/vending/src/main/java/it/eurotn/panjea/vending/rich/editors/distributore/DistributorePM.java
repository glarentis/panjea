package it.eurotn.panjea.vending.rich.editors.distributore;

import java.io.Serializable;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.TipoModello;

public class DistributorePM implements Serializable, IDefProperty {

    private static final long serialVersionUID = -1431350136290710562L;

    // sorgente che ha generato il PM. Null significa che è stato generato dall'esterno per aprire
    // l'editor altrimenti
    // viene avvalorato con l'id della pagina che lo ha generato
    private String source;

    private TipoModello tipoModello;
    private Modello modello;
    private Distributore distributore;

    /**
     * Costruttore.
     */
    public DistributorePM() {
        super();
        this.source = null;
        this.tipoModello = null;
        this.modello = null;
        this.distributore = null;
    }

    /**
     * Costruttore.
     *
     * @param tipoModello
     *            tipo modello
     * @param modello
     *            modello
     * @param distributore
     *            distributore
     * @param source
     *            sorgente che ha generato il PM
     */
    public DistributorePM(final TipoModello tipoModello, final Modello modello, final Distributore distributore,
            final String source) {
        super();
        this.source = source;
        this.tipoModello = tipoModello;
        this.modello = modello;
        this.distributore = distributore;
    }

    /**
     * @return the distributore
     */
    public Distributore getDistributore() {
        return distributore;
    }

    @Override
    public String getDomainClassName() {
        return DistributorePM.class.getSimpleName();
    }

    @Override
    public Integer getId() {
        return ObjectUtils.defaultIfNull(distributore, new Distributore()).getId();
    }

    /**
     * @return the modello
     */
    public Modello getModello() {
        return modello;
    }

    /**
     * @return the source
     */
    public final String getSource() {
        return source;
    }

    /**
     * @return the tipoModello
     */
    public final TipoModello getTipoModello() {
        return tipoModello;
    }

    @Override
    public Integer getVersion() {
        return ObjectUtils.defaultIfNull(distributore, new Distributore()).getVersion();
    }

    @Override
    public boolean isNew() {
        return false;
    }

}
