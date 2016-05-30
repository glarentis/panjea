package it.eurotn.panjea.ordini.rich.editors.produzione;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.Calendar;

import javax.swing.JComponent;

import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaProduzioneForm extends PanjeaAbstractForm implements Focussable {

	private AziendaCorrente aziendaCorrente;

	public static final String FORM_ID = "parametriRicercaProduzioneForm";

	public static final String FORMMODEL_ID = "parametriRicercaProduzioneFormModel";

	private JComponent dataComponent;

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaAreaOrdine
	 *            parametri da gestire
	 */
	public ParametriRicercaProduzioneForm(final ParametriRicercaProduzione parametriRicercaAreaOrdine) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaAreaOrdine, false, FORMMODEL_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default,20dlu,right:pref,4dlu,left:default,20dlu,right:pref,4dlu,left:default,20dlu,fill:default:grow",
				"default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");

		dataComponent = (JComponent) builder.addPropertyAndLabel("dataProduzione")[1].getComponent(1);

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		logger.debug("--> Enter createNewObject");
		ParametriRicercaProduzione parametriRicercaAreaOrdine = new ParametriRicercaProduzione();
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, aziendaCorrente.getAnnoMagazzino());
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		parametriRicercaAreaOrdine.getDataProduzione().setDataIniziale(calendar.getTime());
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		parametriRicercaAreaOrdine.getDataProduzione().setDataFinale(calendar.getTime());
		logger.debug("--> Exit createNewObject");
		return parametriRicercaAreaOrdine;
	}

	@Override
	public void grabFocus() {
		dataComponent.requestFocusInWindow();
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

}
