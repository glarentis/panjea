package it.eurotn.panjea.anagrafica.rich.editors.entita;

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

/**
 * Form di {@link Entita}.
 */
public class NoteEntitaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "noteEntitaForm";

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore di default.
	 * 
	 * @param entita
	 *            l'entita da modificare
	 */
	public NoteEntitaForm(final Entita entita) {
		super(PanjeaFormModelHelper.createFormModel(entita, false, FORM_ID), FORM_ID);
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.getBeanId());
	}

	@Override
	protected JComponent createFormControl() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("1dlu,left:250dlu,10dlu,left:default:grow",
				"2dlu,default,2dlu,90dlu,2dlu,default,2dlu,90dlu,2dlu,default,2dlu,90dlu,2dlu,default,2dlu,90dlu");
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
		builder.nextRow();

		builder.addHorizontalSeparator("noteContabilita", 1, 4);
		builder.nextRow();

		Binding noteContabiliBinding = bf.createBoundHTMLEditor("noteContabilita");
		builder.addBinding(noteContabiliBinding, 2, 8, 3, 1);
		builder.nextRow();

		builder.addHorizontalSeparator("noteMagazzino", 1, 4);
		builder.nextRow();

		Binding noteMagazzinoBinding = bf.createBoundHTMLEditor("noteMagazzino");
		builder.addBinding(noteMagazzinoBinding, 2, 12, 3, 1);
		builder.nextRow();

		return new JScrollPane(builder.getPanel());
	}

	@Override
	public void setFormObject(Object formObject) {

		if (formObject != null) {
			formObject = anagraficaBD.caricaEntita(((Entita) formObject).getEntitaLite(), false);
		}

		super.setFormObject(formObject);
	}
}
