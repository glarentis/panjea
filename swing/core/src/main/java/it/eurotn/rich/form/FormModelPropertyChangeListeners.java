package it.eurotn.rich.form;

import java.beans.PropertyChangeListener;

import org.springframework.binding.form.FormModel;

import it.eurotn.rich.editors.controllers.DefaultController;

/**
 * Estensione dell'interfaccia {@link PropertyChangeListener}. Le classi Property che la implementeranno avranno la
 * possibilità <br>
 * di wrappare il FormModel che verrà settato attraverso il metodo setFormModel <br>
 * Le classi PropertyChange che implementeranno questa interfaccia potranno essere aggiunge <br>
 * alla Map di PropertyChange {@link DefaultController} che provvederà a valorizzare il FormModel <br>
 *
 * @author adriano
 * @version 1.0, 04/set/2008
 */
public interface FormModelPropertyChangeListeners extends PropertyChangeListener {

    /**
     * Setter di {@link FormModel}.
     *
     * @param formModel
     *            the formModel to set
     */
    void setFormModel(FormModel formModel);

}
