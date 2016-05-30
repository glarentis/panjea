package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Generata se manca una categoria contabile sul deposito/sede/articolo durante la contabilizzazione.
 *
 * @author giangi
 */
public class CategoriaContabileAssenteException extends Exception implements PanjeaPropertyException {
    private static final long serialVersionUID = 702159479335311380L;
    public static final String ENTITA_ARTICOLO = "articolo";
    public static final String ENTITA_DEPOSITO = "deposito";
    public static final String ENTITA_SEDEMAGAZZINO = "sede";
    /**
     * @uml.property name="entita"
     */
    private String entita; // Articolo/Sede/Deposito
    /**
     * @uml.property name="chiaveEntita"
     */
    private String chiaveEntita;

    /**
     *
     * Costruttore.
     *
     * @param chiaveEntita
     *            chiave entita
     * @param entita
     *            entita
     */
    public CategoriaContabileAssenteException(final String chiaveEntita, final String entita) {
        super();
        this.chiaveEntita = chiaveEntita;
        this.entita = entita;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CategoriaContabileAssenteException)) {
            return false;
        }
        CategoriaContabileAssenteException contabileAssenteException2 = (CategoriaContabileAssenteException) obj;
        return chiaveEntita.equals(contabileAssenteException2.getChiaveEntita())
                && entita.equals(contabileAssenteException2.getEntita());
    }

    /**
     * @return the chiaveEntita
     * @uml.property name="chiaveEntita"
     */
    public String getChiaveEntita() {
        return chiaveEntita;
    }

    /**
     * @return the entita
     * @uml.property name="entita"
     */
    public String getEntita() {
        return entita;
    }

    @Override
    public String[] getPropertyVaules() {
        return new String[] { getEntita(), getChiaveEntita() };
    }

    @Override
    public int hashCode() {
        String hashStr = this.getClass().getName() + ":" + this.getChiaveEntita().hashCode()
                + this.getEntita().hashCode();
        return hashStr.hashCode();
    }
}
