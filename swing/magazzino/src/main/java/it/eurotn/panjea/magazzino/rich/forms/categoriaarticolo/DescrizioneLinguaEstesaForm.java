/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.TableFormBuilder;

import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 *
 *
 * @author adriano
 * @version 1.0, 13/nov/2008
 *
 */
public class DescrizioneLinguaEstesaForm extends PanjeaAbstractForm {

    private static Logger logger = Logger.getLogger(DescrizioneLinguaEstesaForm.class);

    public static final String FORM_ID = "DescrizioneLinguaEstesaForm";
    public static final String FORMMODEL_ID = "DescrizioneLinguaEstesaFormModel";

    private HierarchicalFormModel childFormModel;

    /**
     * Costruttore.
     *
     * @param parentFormModel
     *            form model della categoria
     * @param descrizioneLingua
     *            descrizione lingua
     */
    public DescrizioneLinguaEstesaForm(final HierarchicalFormModel parentFormModel,
            final IDescrizioneLingua descrizioneLingua) {
        super(parentFormModel, FORM_ID);
        childFormModel = FormModelHelper.createFormModel(descrizioneLingua, false);
        parentFormModel.addChild(childFormModel);
    }

    @Override
    protected JComponent createFormControl() {
        logger.debug("--> Enter createFormControl");

        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) getService(
                BindingFactoryProvider.class)).getBindingFactory(this.childFormModel);
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");
        Binding binding = bf.createBoundHTMLEditor("descrizione");
        builder.getLayoutBuilder().cell(binding.getControl(), "valign=fill rowSpec=90dlu");
        logger.debug("--> Exit createFormControl");
        return builder.getForm();
    }

    /**
     * @return Returns the childFormModel.
     */
    public HierarchicalFormModel getChildFormModel() {
        return childFormModel;
    }

}
