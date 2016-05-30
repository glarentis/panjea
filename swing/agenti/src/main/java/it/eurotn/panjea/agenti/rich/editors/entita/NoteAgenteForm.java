package it.eurotn.panjea.agenti.rich.editors.entita;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class NoteAgenteForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "noteAgenteForm";

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore di default.
	 * 
	 */
	public NoteAgenteForm() {
		super(PanjeaFormModelHelper.createFormModel(new Agente(), false, FORM_ID), FORM_ID);
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.getBeanId());
	}

	@Override
	protected JComponent createFormControl() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("1dlu,left:250dlu,10dlu,left:default:grow",
				"2dlu,default,2dlu,fill:pref:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.setComponentAttributes("f, f");

		builder.nextRow();
		builder.setRow(2);

		builder.addHorizontalSeparator("note", 1, 4);
		builder.nextRow();

		Binding noteBinding = bf.createBoundHTMLEditor("note");
		builder.addBinding(noteBinding, 2, 4, 3, 1);

		return new JScrollPane(builder.getPanel());
	}

	@Override
	public void setFormObject(Object formObject) {

		if (formObject != null && ((Entita) formObject).getId() != null) {
			formObject = anagraficaBD.caricaEntita(((Entita) formObject).getEntitaLite(), false);
		}

		super.setFormObject(formObject);
	}

}
