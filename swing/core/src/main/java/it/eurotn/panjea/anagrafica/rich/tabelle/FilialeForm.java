/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author Leonardo
 * 
 */
public class FilialeForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "filialeForm";

	/**
	 * Costruttore.
	 * 
	 * @param filiale
	 *            filiale
	 */
	public FilialeForm(final Filiale filiale) {
		super(PanjeaFormModelHelper.createFormModel(filiale, false, FORM_ID + "Model"), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(6);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(45);
		builder.row();
		((JTextField) builder.add("indirizzo", "align=left")[1]).setColumns(35);
		builder.row();
		((SearchPanel) (builder.add(bf.createBoundSearchText("cap", new String[] { "descrizione" }), "align=left")[1]))
				.getTextFields().get("descrizione").setColumns(6);
		builder.row();
		SearchPanel searchPanelBanca = (SearchPanel) builder.add(
				bf.createBoundSearchText("banca", new String[] { Banca.PROP_CODICE, Banca.PROP_DESCRIZIONE }),
				"align=left")[1];
		searchPanelBanca.getTextFields().get(Banca.PROP_CODICE).setColumns(6);
		searchPanelBanca.getTextFields().get(Banca.PROP_DESCRIZIONE).setColumns(25);
		builder.row();

		return builder.getForm();
	}
}
