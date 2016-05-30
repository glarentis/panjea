package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class CodiceIvaCorrispettivoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "codiceIvaCorrispettivoForm";

	/**
	 * Costruttore.
	 * 
	 * @param codiceIvaCorrispettivo
	 *            {@link CodiceIvaCorrispettivo}
	 */
	public CodiceIvaCorrispettivoForm(final CodiceIvaCorrispettivo codiceIvaCorrispettivo) {
		super(PanjeaFormModelHelper.createFormModel(codiceIvaCorrispettivo, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		SearchTextBinding searchTextBinding = (SearchTextBinding) bf.createBoundSearchText("codiceIva",
				new String[] { "codice" }, new String[] {}, new String[] {});
		SearchPanel searchPanelIva = (SearchPanel) builder.add(searchTextBinding, "align=left")[1];
		searchPanelIva.getTextFields().get("codice").setColumns(5);
		builder.row();
		((JTextField) builder.add("ordinamento", "align=left")[1]).setColumns(5);
		builder.row();

		return builder.getForm();
	}
}
