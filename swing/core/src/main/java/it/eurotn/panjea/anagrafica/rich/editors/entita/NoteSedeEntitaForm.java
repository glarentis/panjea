package it.eurotn.panjea.anagrafica.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form di {@link SedeAnagrafica} per la modifica delle note.
 */
public class NoteSedeEntitaForm extends PanjeaAbstractForm {

	protected static Logger logger = Logger.getLogger(NoteSedeEntitaForm.class);
	protected static final String FORM_ID = "noteSedeEntitaForm";

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore di default.
	 * 
	 * @param sedeEntita
	 *            la sede da modificare
	 */
	public NoteSedeEntitaForm(final SedeEntita sedeEntita) {
		super(PanjeaFormModelHelper.createFormModel(sedeEntita, false, FORM_ID), FORM_ID);
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.getBeanId());
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form dell'anagrafica");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:pref:grow", "4dlu,90dlu,4dlu,10dlu,4dlu,90dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()

		builder.setLabelAttributes("r,c");
		builder.setComponentAttributes("f, f");
		builder.setRow(2);

		Binding noteBinding = bf.createBoundHTMLEditor("note");
		builder.addBinding(noteBinding, 1, 2);
		builder.nextRow();

		builder.addHorizontalSeparator("noteStampa");
		builder.nextRow();

		Binding noteStampaBinding = bf.createBoundHTMLEditor("noteStampa");
		builder.addBinding(noteStampaBinding);
		builder.nextRow();

		return builder.getPanel();
	}

	@Override
	public void setFormObject(Object formObject) {
		if (formObject != null) {
			formObject = anagraficaBD.caricaSedeEntita(((SedeEntita) formObject).getId(), false);
		}

		super.setFormObject(formObject);
	}

}
