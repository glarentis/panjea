/**
 *
 */
package it.eurotn.panjea.ordini.rich.editors.produzione.evasione;

import it.eurotn.panjea.magazzino.rich.search.TipoAreaMagazzinoSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 */
public class ParametriEvasioneProduzioneForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriEvasioneProduzioneForm";
	public static final String FORMMODEL_ID = "parametriEvasioneProduzioneFormModel";

	/**
	 * Costruttore.
	 */
	public ParametriEvasioneProduzioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriEvasioneProduzione(), false, FORMMODEL_ID), FORM_ID);
		// Creo il value model fittizio per la search del tipo documento di
		// evasione
		ValueModel tipoAreaProduzioneValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				tipoAreaProduzioneValueModel), Boolean.class, true, null);
		getFormModel().add(TipoAreaMagazzinoSearchObject.PARAMETRO_TIPO_AREA_PRODUZIONE, tipoAreaProduzioneValueModel,
				metaData);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");

		builder.addLabel("tipoAreaMagazzino", 1);
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaMagazzino", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" },
				new String[] { TipoAreaMagazzinoSearchObject.PARAMETRO_TIPO_AREA_PRODUZIONE },
				new String[] { TipoAreaMagazzinoSearchObject.PARAMETRO_TIPO_AREA_PRODUZIONE });
		SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 3);
		tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
		tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

}
