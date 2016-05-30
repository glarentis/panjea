package it.eurotn.rich.editors;

import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Interfaccia da implementare per poter inserire un form in un Editor
 *
 *
 * @author Aracno
 * @version 1.0, 11/ott/06
 *
 */
public interface IFormPageEditor extends IPageEditor {

    /**
     *
     * @return AbstractForm contenuto nell'editor
     */
    public PanjeaAbstractForm getForm();

}
