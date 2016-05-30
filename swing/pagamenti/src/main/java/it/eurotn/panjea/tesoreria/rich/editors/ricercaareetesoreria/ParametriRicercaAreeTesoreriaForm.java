/**
 * 
 */
package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.partite.rich.search.TipoAreaPartitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form per presentare i {@link ParametriRicercaAreeTesoreria}.
 * 
 * @author Leonardo
 */
public class ParametriRicercaAreeTesoreriaForm extends PanjeaAbstractForm implements Focussable {

	private static Logger logger = Logger.getLogger(ParametriRicercaAreeTesoreriaForm.class);
	public static final String FORM_ID = "parametriRicercaAreePartiteForm";
	private AziendaCorrente aziendaCorrente = null;

	private JComponent focusComponent;

	/**
	 * Costruttore.
	 * 
	 * @param aziendaCorrente
	 *            azienda corrente
	 */
	public ParametriRicercaAreeTesoreriaForm(final AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaAreeTesoreria(), false, FORM_ID), FORM_ID);
		this.aziendaCorrente = aziendaCorrente;
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();

		FormLayout layout = new FormLayout(
				"left:pref, 4dlu, left:pref, 10dlu,right:pref, 4dlu,left:pref, 10dlu,left:pref", "default,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r,c");
		builder.nextRow();
		focusComponent = builder.addPropertyAndLabel("periodo")[1];
		builder.nextRow();

		builder.addLabel("tipoAreaPartita", 1, 2);
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaPartita", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" }, new String[] { "escludiTipiAreaPartiteDistinta" },
				new String[] { TipoAreaPartitaSearchObject.PARAM_ESCLUDI_TIPIAREAPARTITE_DISTINTA });
		SearchPanel searchTextTipoAreaCont = (SearchPanel) builder.addBinding(bindingTipoDoc, 3, 2);
		searchTextTipoAreaCont.getTextFields().get("tipoDocumento.codice").setColumns(5);
		searchTextTipoAreaCont.getTextFields().get("tipoDocumento.descrizione").setColumns(18);
		builder.nextRow();

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria = new ParametriRicercaAreeTesoreria();
		parametriRicercaAreeTesoreria.getPeriodo().setDataIniziale(aziendaCorrente.getDataInizioEsercizio());
		parametriRicercaAreeTesoreria.getPeriodo().setDataFinale(aziendaCorrente.getDataFineEsercizio());
		return parametriRicercaAreeTesoreria;
	}

	@Override
	public void grabFocus() {
		focusComponent.requestFocusInWindow();
	}

}
