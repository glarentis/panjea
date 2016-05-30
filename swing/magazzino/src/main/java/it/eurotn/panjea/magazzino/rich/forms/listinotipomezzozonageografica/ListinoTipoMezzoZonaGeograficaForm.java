package it.eurotn.panjea.magazzino.rich.forms.listinotipomezzozonageografica;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class ListinoTipoMezzoZonaGeograficaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "listinoTipoMezzoZonaGeograficaForm";

	/**
	 * Costruttore di default, inizializza un nuovo tipo mezzo di trasporto.
	 */
	public ListinoTipoMezzoZonaGeograficaForm() {
		super(PanjeaFormModelHelper.createFormModel(new ListinoTipoMezzoZonaGeografica(), false, FORM_ID + "Model"),
				FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		SearchPanel searchTipoMezzo = (SearchPanel) builder.add(
				bf.createBoundSearchText("tipoMezzoTrasporto", new String[] { "codice", "descrizione" }, true),
				"align=left")[1];
		searchTipoMezzo.getTextFields().get("codice").setColumns(10);
		searchTipoMezzo.getTextFields().get("descrizione").setColumns(25);

		builder.row();

		SearchPanel searchZonaGeografica = (SearchPanel) builder.add(
				bf.createBoundSearchText("zonaGeografica", new String[] { "codice", "descrizione" }, true),
				"align=left")[1];
		searchZonaGeografica.getTextFields().get("codice").setColumns(10);
		searchZonaGeografica.getTextFields().get("descrizione").setColumns(25);
		builder.row();

		((JTextField) builder.add("prezzo", "align=left")[1]).setColumns(10);
		builder.row();

		return builder.getForm();
	}
}
