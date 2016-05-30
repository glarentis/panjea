package it.eurotn.panjea.anagrafica.rich.forms;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoINPS;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class AltriDatiEntitaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "altriDatiEntitaForm";

	private final PluginManager pluginManager;

	public static final int COLUMN_SIZE = 120;

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore di default.
	 *
	 * @param pluginManager
	 *            manager dei plugin
	 * @param entita
	 *            entita da modificare
	 */
	public AltriDatiEntitaForm(final PluginManager pluginManager, final Entita entita) {
		super(PanjeaFormModelHelper.createFormModel(entita, false, FORM_ID + "Model"), FORM_ID);
		this.pluginManager = pluginManager;
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
	}

	private void buildRitenutaAccontoControl(FormLayoutFormBuilder builder, PanjeaSwingBindingFactory bf) {
		builder.addHorizontalSeparator("Dati per ritenute d'acconto", 4);
		builder.nextRow();

		builder.addLabel("datiRitenutaAcconto.prestazione", 1, 14);
		ComboBoxBinding bindingPrestazione = (ComboBoxBinding) bf.createBoundComboBox(
				"datiRitenutaAcconto.prestazione", new ValueHolder(anagraficaBD.caricaPrestazioni()), "descrizione");
		Prestazione prestazioneEmpty = new Prestazione();
		prestazioneEmpty.setDescrizione(" Nessuna");
		bindingPrestazione.setEmptySelectionValue(prestazioneEmpty);
		builder.addBinding(bindingPrestazione, 3, 14, 2, 1);
		builder.nextRow();

		builder.addLabel("datiRitenutaAcconto.causaleRitenutaAcconto");
		ComboBoxBinding bindingCausale = (ComboBoxBinding) bf.createBoundComboBox(
				"datiRitenutaAcconto.causaleRitenutaAcconto",
				new ValueHolder(anagraficaBD.caricaCausaliRitenuteAcconto()), "descrizione");
		CausaleRitenutaAcconto causaleRitenutaAccontoEmpty = new CausaleRitenutaAcconto();
		causaleRitenutaAccontoEmpty.setDescrizione(" Nessuna");
		bindingCausale.setEmptySelectionValue(causaleRitenutaAccontoEmpty);
		builder.addBinding(bindingCausale, 3, 16, 2, 1);
		builder.nextRow();

		builder.addLabel("datiRitenutaAcconto.percFondoProfessionisti", 1, 18);
		Binding percentualeAliquotaBinding = bf
				.createBoundPercentageText("datiRitenutaAcconto.percFondoProfessionisti");
		builder.addBinding(percentualeAliquotaBinding, 3, 18);
		builder.nextRow();

		builder.addLabel("datiRitenutaAcconto.contributoPrevidenziale");
		ComboBoxBinding bindingContributo = (ComboBoxBinding) bf.createBoundComboBox(
				"datiRitenutaAcconto.contributoPrevidenziale", new ValueHolder(anagraficaBD.caricaContributiINPS()),
				"codice");
		ContributoPrevidenziale contributoPrevidenzialeEmpty = new ContributoINPS();
		contributoPrevidenzialeEmpty.setCodice(" Nessuno");
		bindingContributo.setEmptySelectionValue(contributoPrevidenzialeEmpty);
		builder.addBinding(bindingContributo, 3, 20, 2, 1);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(COLUMN_SIZE + "dlu, 4dlu, fill:70dlu, fill:100dlu", "6dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());

		builder.setLabelAttributes("r,c");
		builder.setRow(2);

		builder.addPropertyAndLabel("escludiSpesometro", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("riepilogativo", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("splitPayment", 1);

		if (pluginManager.isPresente("panjeaFido") && getFormObject() instanceof Cliente) {
			builder.nextRow();
			builder.addPropertyAndLabel("fido", 1);
		}

		if (pluginManager.isPresente("panjeaPagamenti")
				&& (getFormObject() instanceof Cliente || getFormObject() instanceof Fornitore)) {
			builder.nextRow();
			builder.addPropertyAndLabel("raggruppaEffetti", 1);
		}

		if (getFormModel().getFormObject().getClass().getName().equals(Fornitore.class.getName())) {
			builder.nextRow();

			if (pluginManager.isPresente(PluginManager.PLUGIN_CONTABILITA)) {
				buildRitenutaAccontoControl(builder, bf);
			}
		}

		return builder.getPanel();
	}
}
