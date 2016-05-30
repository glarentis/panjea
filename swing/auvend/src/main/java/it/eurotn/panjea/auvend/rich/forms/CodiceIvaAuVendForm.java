/**
 * 
 */
package it.eurotn.panjea.auvend.rich.forms;

import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form per {@link CodiceIvaAuVend}.
 * 
 * @author adriano
 * @version 1.0, 17/feb/2009
 * 
 */
public class CodiceIvaAuVendForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(CodiceIvaAuVendForm.class);

	private static final String FORM_ID = "codiceIvaAuVendForm";

	/**
	 * Costruttore.
	 * 
	 */
	public CodiceIvaAuVendForm() {
		super(PanjeaFormModelHelper.createFormModel(new CodiceIvaAuVend(), false, FORM_ID), FORM_ID);
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
		builder.add(bf.createBoundSearchText("codiceIva", new String[] { "codice", "descrizioneInterna" }));
		builder.row();
		logger.debug("--> Exit createFormControl");
		return builder.getForm();
	}

}
