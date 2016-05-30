package it.eurotn.panjea.magazzino.util;

import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class RigaNotaDTO extends RigaMagazzinoDTO {

    private static final long serialVersionUID = -2048470156215494085L;

    /**
     * @uml.property name="nota"
     */
    private String nota;

    private boolean rigaAutomatica;

    /**
     * Costruttore.
     */
    public RigaNotaDTO() {
        super();
    }

    /**
     * @return the nota
     * @uml.property name="nota"
     */
    public String getNota() {
        return nota;
    }

    @Override
    public RigaMagazzino getRigaMagazzino() {
        RigaMagazzino riga = new RigaNota();
        PanjeaEJBUtil.copyProperties(riga, this);
        return riga;
    }

    /**
     * @return the rigaAutomatica
     */
    public boolean isRigaAutomatica() {
        return rigaAutomatica;
    }

    /**
     * @param nota
     *            the nota to set
     * @uml.property name="nota"
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @param rigaAutomatica
     *            the rigaAutomatica to set
     */
    public void setRigaAutomatica(boolean rigaAutomatica) {
        this.rigaAutomatica = rigaAutomatica;
    }

}
