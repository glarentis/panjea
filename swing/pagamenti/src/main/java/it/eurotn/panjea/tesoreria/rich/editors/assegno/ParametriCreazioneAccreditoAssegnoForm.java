/**
 * 
 */
package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 */
public class ParametriCreazioneAccreditoAssegnoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriCreazioneAccreditoAssegnoForm";
	public static final String FORM_MODEL_ID = "parametriCreazioneAccreditoAssegnoFormModel";

	/**
	 * Costruttore.
	 */
	public ParametriCreazioneAccreditoAssegnoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneAreaChiusure(), false, FORM_MODEL_ID),
				FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:p,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref, 10dlu, left:pref,4dlu,left:pref",
				"default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("dataDocumento", 1);

		// rapporto bancario azienda
		builder.addLabel("rapportoBancarioAzienda", 9);
		Binding bindingBanca = bf.createBoundSearchText("rapportoBancarioAzienda", new String[] { "numero",
				"descrizione" }, new String[] {}, new String[] {});
		SearchPanel searchBanca = (SearchPanel) bindingBanca.getControl();
		searchBanca.getTextFields().get("numero").setColumns(5);
		searchBanca.getTextFields().get("descrizione").setColumns(14);
		builder.addBinding(bindingBanca, 11);
		builder.nextRow();

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ParametriCreazioneAreaChiusure parametri = new ParametriCreazioneAreaChiusure();
		parametri.setTipoAreaPartita(new TipoAreaPartita());
		parametri.getTipoAreaPartita().getTipoDocumento().setTipoEntita(TipoEntita.BANCA);
		parametri.setRapportoBancarioAzienda(null);
		return parametri;
	}

}
