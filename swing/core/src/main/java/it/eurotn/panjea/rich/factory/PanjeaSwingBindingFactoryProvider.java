package it.eurotn.panjea.rich.factory;

import org.springframework.binding.form.ConfigurableFormModel;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.BindingFactory;
import org.springframework.richclient.form.binding.swing.SwingBindingFactoryProvider;

public class PanjeaSwingBindingFactoryProvider extends SwingBindingFactoryProvider {

    /**
     * Produce a BindingFactory using the provided form model.
     * 
     * @param formModel
     *            Form model on which to construct the BindingFactory
     * @return BindingFactory
     */
    @Override
    public BindingFactory getBindingFactory(FormModel formModel) {
        return new PanjeaSwingBindingFactory((ConfigurableFormModel) formModel);
    }

}
