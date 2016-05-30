package it.eurotn.panjea.magazzino.rich.forms.tipodocumentobase;

import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class TipoDocumentoBaseMagazzinoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "tipoDocumentoBaseMagazzinoForm";

	/**
	 * Costruttore.
	 */
	public TipoDocumentoBaseMagazzinoForm() {
		super(PanjeaFormModelHelper.createFormModel(new TipoDocumentoBaseMagazzino(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		SearchPanel searchPanel = (SearchPanel) builder.add(
				bf.createBoundSearchText("tipoAreaMagazzino", new String[] { "tipoDocumento.codice",
						"tipoDocumento.descrizione" }), "align=left")[1];
		searchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
		searchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(18);
		builder.row();
		builder.add("tipoOperazione", "align=left");
		builder.row();

		return builder.getForm();
	}
}
