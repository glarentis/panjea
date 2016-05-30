package it.eurotn.panjea.ordini.rich.forms.righeordine;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class RigaArticolo2Form extends PanjeaAbstractForm {

	public static final String FORM_ID = "rigaArticolo2Form";

	protected static Logger logger = Logger.getLogger(RigaArticolo2Form.class);

	private boolean isContabilitaPluginEnabled;

	/**
	 * costruttore.
	 * 
	 * @param formModel
	 *            formmodel da utilizzare
	 */
	public RigaArticolo2Form(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);

		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();

		SearchPanel searchPanel = (SearchPanel) builder.add(bf.createBoundSearchText("codiceIva",
				new String[] { "codice" }))[1];
		searchPanel.getTextFields().get("codice").setColumns(6);
		if (isContabilitaPluginEnabled()) {
			builder.add(bf.createBoundSearchText("categoriaContabileArticolo", new String[] { "codice" }));
		}

		builder.row();
		return builder.getForm();
	}

	/**
	 * 
	 * @return true se il plugin della contabiltà è abilitato
	 */
	public boolean isContabilitaPluginEnabled() {
		return isContabilitaPluginEnabled;
	}

	/**
	 * setter for contabilitaPluginEnabled.
	 * 
	 * @param contabilitaPluginEnabled
	 *            true se plugin abilitato
	 */
	public void setContabilitaPluginEnabled(boolean contabilitaPluginEnabled) {
		this.isContabilitaPluginEnabled = contabilitaPluginEnabled;
	}

}
