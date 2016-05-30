package it.eurotn.rich.form.builder.support;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormComponentInterceptor;
import org.springframework.richclient.form.builder.support.ConfigurableFormComponentInterceptorFactory;

public class DirtyIndicatorInterceptorFactory extends ConfigurableFormComponentInterceptorFactory {

    /**
     * Costruttore.
     * 
     */
    public DirtyIndicatorInterceptorFactory() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    protected FormComponentInterceptor createInterceptor(FormModel formModel) {
        return new DirtyIndicatorInterceptor(formModel);
    }

}
