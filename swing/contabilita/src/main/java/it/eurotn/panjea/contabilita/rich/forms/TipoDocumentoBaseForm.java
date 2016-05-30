/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Classe form per la gestione di {@link TipoDocumentoBase}.
 * 
 * @author adriano
 * @version 1.0, 27/ago/07
 * 
 */
public class TipoDocumentoBaseForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(TipoDocumentoBaseForm.class);

	private static final String FORM_ID = "tipoDocumentoBaseForm";

	/**
	 * Costruttore.
	 * 
	 * @param tipoDocumentoBase
	 *            {@link TipoDocumentoBase}
	 */
	public TipoDocumentoBaseForm(final TipoDocumentoBase tipoDocumentoBase) {
		super(PanjeaFormModelHelper.createFormModel(tipoDocumentoBase, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		SearchPanel searchPanelTipoAreaContabile = (SearchPanel) builder.add(
				bf.createBoundSearchText("tipoAreaContabile", new String[] { "tipoDocumento.codice",
						"tipoDocumento.descrizione" }), "align=left")[1];
		searchPanelTipoAreaContabile.getTextFields().get("tipoDocumento.codice").setColumns(5);
		searchPanelTipoAreaContabile.getTextFields().get("tipoDocumento.descrizione").setColumns(25);
		builder.row();
		builder.add("tipoOperazione", "align=left");
		builder.row();
		logger.debug("--> Exit createFormControl");

		return builder.getForm();
	}

}
