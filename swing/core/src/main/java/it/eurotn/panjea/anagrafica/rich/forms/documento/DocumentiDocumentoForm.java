/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.forms.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.anagrafica.rich.search.DocumentoSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
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
public class DocumentiDocumentoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "documentiDocumentoForm";

	/**
	 * Costruttore.
	 */
	public DocumentiDocumentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaDocumento(), false, FORM_ID));
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,fill:pref:grow", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.addLabel("documento", 1);
		builder.addBinding(getDocumentoBinding(bf), 2);
		return builder.getPanel();
	}

	/**
	 * @return the documento to get
	 */
	public Documento getDocumento() {
		return ((ParametriRicercaDocumento) getFormObject()).getDocumento();
	}

	/**
	 * Crea e restituisce il SearchTextBinding per l' entita aggiungendo il pulsante per la richiesta della situazione
	 * rate.
	 * 
	 * @param bf
	 *            il binding factory
	 * @return Binding
	 */
	private Binding getDocumentoBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingEntita = bf.createBoundSearchText("documento", new String[] { "codice", "dataDocumento" },
				new String[] { "entita" }, new String[] { DocumentoSearchObject.ENTITA_KEY });
		SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
		searchPanel.getTextFields().get("codice").setColumns(6);
		searchPanel.getTextFields().get("dataDocumento").setColumns(10);
		return bindingEntita;
	}

}
