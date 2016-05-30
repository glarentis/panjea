/**
 * 
 */
package it.eurotn.rich.binding;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.rich.components.ImportoTextField;

import java.util.Map;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

/**
 * Binder per la creazione di {@link ImportoBinding}.
 * 
 * @author adriano
 * @version 1.0, 30/mag/08
 */
public class ImportoBinder extends AbstractBinder {

    public static final String IMPORTO_RIFERIMENTO_PATH_KEY = "importoRiferimentoPath";

    public static final String DATA_CAMBIO_PATH = "dataCambioPath";

    public static final String NUMERO_DECIMALI_PATH_KEY = "numeroDecimaliPath";

    /**
     * Costruttore.
     */
    public ImportoBinder() {
        super(Importo.class, new String[] { IMPORTO_RIFERIMENTO_PATH_KEY, DATA_CAMBIO_PATH, NUMERO_DECIMALI_PATH_KEY });
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected JComponent createControl(Map context) {
        ImportoTextField component = new ImportoTextField();
        return component;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        logger.debug("--> Enter doBind");
        ImportoBinding importoBinding = new ImportoBinding(formModel, formPropertyPath, getRequiredSourceClass(),
                (ImportoTextField) control);
        importoBinding.applyContext(context);

        logger.debug("--> Exit doBind");
        return importoBinding;
    }
}
