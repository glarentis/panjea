package it.eurotn.panjea.magazzino.rich.forms.contratto;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form per la GUI di {@link Contratto}.
 * 
 * @author adriano
 * @version 1.0, 18/giu/08
 * 
 */
public class ContrattoForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(ContrattoForm.class);
	private static final String FORM_ID = "contrattoForm";

	/**
	 * Costruttore.
	 * 
	 */
	public ContrattoForm() {
		super(PanjeaFormModelHelper.createFormModel(new Contratto(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(20);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(40);
		builder.row();
		builder.add("dataInizio", "align=left");
		builder.add("dataFine", "align=left");
		builder.row();

		builder.add(bf.createBoundSearchText("codiceValuta", null, ValutaAzienda.class), "align=left");

		((JTextField) builder.add("numeroDecimali", "align=left")[1]).setColumns(5);
		builder.getLayoutBuilder().cell();
		builder.getLayoutBuilder().cell();
		builder.row();

		builder.getLayoutBuilder().cell();
		builder.row();
		// getFormModel().addPropertyChangeListener(this);
		logger.debug("--> Exit createFormControl");
		return builder.getForm();
	}

}
