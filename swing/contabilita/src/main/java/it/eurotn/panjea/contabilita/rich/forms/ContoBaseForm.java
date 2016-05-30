package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form per la gestione del <code>ContoBase</code>.
 * 
 * @author fattazzo
 * @version 1.0, 27/ago/07
 * 
 */
public class ContoBaseForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(ContoBaseForm.class);

	private static final String FORM_ID = "contoBaseForm";

	/**
	 * Costruttore.
	 * 
	 * @param contoBase
	 *            {@link ContoBase}
	 */
	public ContoBaseForm(final ContoBase contoBase) {
		super(PanjeaFormModelHelper.createFormModel(contoBase, false, FORM_ID), FORM_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(20);
		builder.row();
		SearchPanel searchPanel = (SearchPanel) builder.add("sottoConto", "align=left")[1];
		SearchTextField contoAvereSearch = searchPanel.getTextFields().get("descrizione");
		contoAvereSearch.setColumns(12);
		builder.row();
		builder.add("tipoContoBase", "align=left");
		builder.row();
		logger.debug("--> Exit createFormControl");
		return builder.getForm();
	}

}
