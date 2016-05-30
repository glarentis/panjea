/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * @author Aracno
 * @version 1.0, 27-nov-2006
 * 
 */
public class DatiAttivitaAziendaForm extends PanjeaAbstractForm {

	private static final String SEPARATOR_DATE_ATTIVITA = "aziendaFormModel.separator.date.attivita";

	private final PluginManager pluginManager;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 * @param formId
	 *            id del form
	 */
	public DatiAttivitaAziendaForm(final FormModel formModel, final String formId) {
		super(formModel, formId);

		this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow", "3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		Binding bindingFormaGiuridica = bf.createBoundSearchText("azienda.formaGiuridica", new String[] {
				FormaGiuridica.PROP_SIGLA, FormaGiuridica.PROP_DESCRIZIONE });
		builder.addLabel("azienda.formaGiuridica", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bindingFormaGiuridica, 3);
		searchPanel.getTextFields().get(FormaGiuridica.PROP_SIGLA).setColumns(4);
		searchPanel.getTextFields().get(FormaGiuridica.PROP_DESCRIZIONE).setColumns(18);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("azienda." + Azienda.PROP_CODICE_ATTIVITA_PREVALENTE, 1)[1])
				.setColumns(10);
		((JTextField) builder.addPropertyAndLabel("azienda." + Azienda.PROP_DESCRIZIONE_ATTIVITA_PREVALENTE, 5)[1])
				.setColumns(30);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("azienda." + Azienda.PROP_NUMERO_ISCRIZIONE_TRIBUNALE, 1)[1])
				.setColumns(10);
		((JTextField) builder.addPropertyAndLabel("azienda." + Azienda.PROP_TRIBUNALE_ISCRIZIONE, 5)[1]).setColumns(30);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("azienda." + Azienda.PROP_NUMERO_ISCRIZIONE_CCIAA, 1)[1])
				.setColumns(10);
		((JTextField) builder.addPropertyAndLabel("azienda." + Azienda.PROP_CCIAA_ISCRIZIONE, 5)[1]).setColumns(30);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("azienda." + Azienda.PROP_CODICE_SIA)[1]).setColumns(5);
		builder.nextRow();

		if (pluginManager.isPresente(PluginManager.PLUGIN_CONAI)) {
			((JTextField) builder.addPropertyAndLabel("azienda.codiceSocioConai", 1)[1]).setColumns(10);
			builder.addPropertyAndLabel("azienda.conaiTipoIscrizione", 5);
			builder.nextRow();
		}

		builder.addHorizontalSeparator(getMessage(SEPARATOR_DATE_ATTIVITA), 7);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda." + Azienda.PROP_DATA_INIZIO_ATTIVITA, 1);
		builder.nextRow();

		builder.addLabel("azienda." + Azienda.PROP_DATA_INIZIO_ESERCIZIO, 1);
		builder.addBinding(bf.createBoundCalendar("azienda." + Azienda.PROP_DATA_INIZIO_ESERCIZIO, "dd/MM", "##/##"), 3);
		builder.nextRow();

		if (pluginManager.isPresente(PluginManager.PLUGIN_PAGAMENTI)) {
			PanjeaAbstractForm riepilogoCalendariForm = RcpSupport.getBean("repilogoCalendariRateAziendaForm");
			riepilogoCalendariForm.setFormObject(getFormObject());

			builder.addComponent(riepilogoCalendariForm.getControl(), 1, 20, 7, 1);
			builder.nextRow();
		}

		return builder.getPanel();
	}
}
