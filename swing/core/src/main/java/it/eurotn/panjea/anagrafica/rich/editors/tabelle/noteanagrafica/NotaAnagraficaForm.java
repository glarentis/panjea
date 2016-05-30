package it.eurotn.panjea.anagrafica.rich.editors.tabelle.noteanagrafica;

import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class NotaAnagraficaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "notaAnagraficaForm";

	/**
	 * Costruttore.
	 * 
	 */
	public NotaAnagraficaForm() {
		super(PanjeaFormModelHelper.createFormModel(new NotaAnagrafica(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,50dlu,f:d:g", "4dlu,default,4dlu,default,f:default:g");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.setRow(2);

		builder.addPropertyAndLabel("codice", 1);
		builder.nextRow();

		builder.addLabel("descrizione", 1);
		Binding descrizioneBinding = bf.createBoundHTMLEditor("descrizione");
		builder.addBinding(descrizioneBinding, 3, 4, 2, 2);

		return builder.getPanel();
	}

}
