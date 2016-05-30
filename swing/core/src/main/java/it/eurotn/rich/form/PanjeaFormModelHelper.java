package it.eurotn.rich.form;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.support.BeanPropertyAccessStrategy;
import org.springframework.binding.support.ClassPropertyAccessStrategy;
import org.springframework.binding.value.support.ValueHolder;

/**
 *
 * Utility per creare un formModel.
 *
 * @author giangi
 * @version 1.0, 30/nov/2010
 *
 */
public abstract class PanjeaFormModelHelper {

    /**
     * Classe di utility==>costruttore privato.
     */
    private PanjeaFormModelHelper() {
    }

    /**
     * Crea un form model con BeanPropertyAccessStrategy invece che il default ClassPropertyAccessStrategy.
     *
     * @param beanObject
     *            beanObject
     * @param buffered
     *            buffered
     * @param formId
     *            formId
     * @return FormModel
     */
    public static FormModel createBeanFormModel(Object beanObject, boolean buffered, String formId) {
        BeanPropertyAccessStrategy accessProperty = new BeanPropertyAccessStrategy(beanObject);

        PanjeaFormModel formModel = new PanjeaFormModel(accessProperty, buffered);
        formModel.setId(formId + "Model");
        return formModel;
    }

    /**
     *
     * @param beanObject
     *            bean per inizializzare il form
     * @param buffered
     *            indica se il form bufferizza i valori.
     * @param formId
     *            id del form
     * @return form che accetta valori a null per le proprietà nested
     */
    public static FormModel createFormModel(Object beanObject, boolean buffered, String formId) {
        return createFormModel(beanObject, buffered, formId,
                new ClassPropertyAccessStrategy(new ValueHolder(beanObject), false, false));
    }

    /**
     *
     * @param beanObject
     *            bean per inizializzare il form
     * @param buffered
     *            indica se il form bufferizza i valori.
     * @param formId
     *            id del form
     * @param classPropertyAccessStrategy
     *            strategy
     * @return form che accetta valori a null per le proprietà nested
     */
    public static FormModel createFormModel(Object beanObject, boolean buffered, String formId,
            ClassPropertyAccessStrategy classPropertyAccessStrategy) {

        PanjeaFormModel formModel = new PanjeaFormModel(classPropertyAccessStrategy, buffered);
        formModel.setId(formId + "Model");
        return formModel;
    }
}
