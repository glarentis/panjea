/**
 * 
 */
package it.eurotn.panjea.auvend.rich.forms;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form per l'oggetto {@link LetturaFlussoAuVend}.
 * 
 * @author adriano
 * @version 1.0, 02/gen/2009
 * 
 */
public class LetturaFlussoAuVendForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "letturaFlussoAuVendForm";

	private static Logger logger = Logger.getLogger(LetturaFlussoAuVendForm.class);

	private AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 */
	public LetturaFlussoAuVendForm() {
		super(PanjeaFormModelHelper.createFormModel(new LetturaFlussoAuVend(), false, FORM_ID), FORM_ID);
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
		Azienda azienda = new Azienda();
		azienda.setId(aziendaCorrente.getId());

		SearchPanel searchPanelDeposito = (SearchPanel) builder.add(
				bf.createBoundSearchText("deposito", new String[] { "codice", "descrizione" }), "align=left")[1];
		searchPanelDeposito.getTextFields().get("codice").setColumns(5);
		searchPanelDeposito.getTextFields().get("descrizione").setColumns(20);
		builder.row();
		builder.add("ultimaLetturaFlussoCarichi", "align=left");
		builder.getLayoutBuilder().cell();
		builder.row();
		builder.add("ultimaLetturaFlussoFatture", "align=left");
		builder.getLayoutBuilder().cell();
		builder.row();
		logger.debug("--> Exit createFormControl");
		return builder.getForm();
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

}
