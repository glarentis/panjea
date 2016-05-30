/**
 * 
 */
package it.eurotn.panjea.auvend.rich.forms;

import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

/**
 * Form per l'oggetto {@link TipoDocumentoBaseAuVend}.
 * 
 * @author adriano
 * @version 1.0, 30/dic/2008
 * 
 */
public class TipoDocumentoBaseAuVendForm extends PanjeaAbstractForm {

	private Logger logger = Logger.getLogger(TipoDocumentoBaseAuVendForm.class);
	private static final String FORM_ID = "tipoDocumentoBaseAuVendForm";

	/**
	 * Costruttore.
	 * 
	 * @param tipoDocumentoBaseAuVend
	 *            tipo documento base
	 */
	public TipoDocumentoBaseAuVendForm(final TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
		super(PanjeaFormModelHelper.createFormModel(tipoDocumentoBaseAuVend, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		IAuVendBD auVendBD = RcpSupport.getBean(IAuVendBD.BEAN_ID);

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		builder.add("tipoOperazione");
		builder.row();
		builder.add(bf.createBoundSearchText("tipoAreaMagazzino", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" }));
		builder.row();
		builder.add(bf.createBoundComboBox("causaleAuvend", auVendBD.caricaCausaliNonAssociateAuvend()));
		builder.row();
		builder.add("depositoCaricatore");
		return builder.getForm();
	}

}
