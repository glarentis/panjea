package it.eurotn.panjea.ordini.rich.forms;

import it.eurotn.panjea.ordini.domain.SedeOrdine;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class SedeOrdineForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "sedeOrdineForm";

	/**
	 * Costruttore.
	 * 
	 */
	public SedeOrdineForm() {
		super(PanjeaFormModelHelper.createFormModel(new SedeOrdine(), false, FORM_ID), FORM_ID);

	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:max(90dlu;p)");

		builder.addSeparator("Evasione ordini");
		builder.row();
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaEvasione", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" });
		SearchPanel tipoDocumentoSearchPanel = (SearchPanel) bindingTipoDoc.getControl();
		tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(6);
		tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(27);

		builder.add(bindingTipoDoc, "align=left colSpan=4");
		builder.row();

		return builder.getForm();
	}
}
