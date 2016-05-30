package it.eurotn.panjea.magazzino.domain.descrizionilingua;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public interface IDescrizioneLingua {

    /**
     * @return codiceLingua
     * @uml.property name="codiceLingua"
     */
    String getCodiceLingua();

    /**
     * @return descrizione in lingua
     * @uml.property name="descrizione"
     */
    String getDescrizione();

    /**
     * @param codiceLingua
     *            the codiceLingua to set
     * @uml.property name="codiceLingua"
     */
    void setCodiceLingua(String codiceLingua);

    /**
     * @param descrizione
     *            the descrizione to set
     * @uml.property name="descrizione"
     */
    void setDescrizione(String descrizione);
}
