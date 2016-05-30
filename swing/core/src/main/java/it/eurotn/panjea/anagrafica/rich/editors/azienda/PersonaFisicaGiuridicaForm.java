/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * @author Aracno
 * @version 1.0, 15-mag-2006
 * 
 */
public class PersonaFisicaGiuridicaForm extends PanjeaAbstractForm {

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 * @param formId
	 *            id del form
	 */
	public PersonaFisicaGiuridicaForm(final FormModel formModel, final String formId) {
		super(formModel, formId);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"70dlu,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow", "3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("azienda.personaFisica.nome", 1);
		builder.addPropertyAndLabel("azienda.personaFisica.cognome", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.personaFisica.sesso");
		builder.nextRow();

		builder.addHorizontalSeparator("estremiDiNascitaSeparator", 7);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.personaFisica.dataNascita");
		builder.nextRow();

		DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf.createDatiGeograficiBinding(
				"azienda.personaFisica.datiGeograficiNascita", "right:70dlu");
		builder.addBinding(bindingDatiGeografici, 1, 10, 7, 1);
		builder.nextRow();

		builder.addHorizontalSeparator("estremiDiResidenzaSeparator", 7);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.personaFisica.viaResidenza");
		builder.nextRow();

		DatiGeograficiBinding bindingDatiGeograficiResidenza = (DatiGeograficiBinding) bf.createDatiGeograficiBinding(
				"azienda.personaFisica.datiGeograficiResidenza", "right:70dlu");
		builder.addBinding(bindingDatiGeograficiResidenza, 1, 16, 7, 1);
		builder.nextRow();

		return builder.getPanel();
	}

}
