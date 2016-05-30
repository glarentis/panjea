package it.eurotn.panjea.magazzino.rich.forms.tipomezzotrasporto;

import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author Leonardo
 */
public class TipoMezzoTrasportoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "tipoMezzoForm";

	/**
	 * Costruttore di default, inizializza un nuovo tipo mezzo di trasporto.
	 */
	public TipoMezzoTrasportoForm() {
		super(PanjeaFormModelHelper.createFormModel(new TipoMezzoTrasporto(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(30);
		builder.row();

		SearchPanel searchPanel = (SearchPanel) builder.add(
				bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" }, true), "align=left")[1];
		searchPanel.getTextFields().get("codice").setColumns(20);
		searchPanel.getTextFields().get("descrizione").setColumns(35);
		builder.row();

		return builder.getForm();
	}

}
