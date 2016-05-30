package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ProRataSetting;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ContabilitaSettingsForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "contabilitaSettingsForm";
	private static final String FORM_MODEL_ID = "contabilitaSettingsFormModel";

	/**
	 * 
	 */
	public ContabilitaSettingsForm() {
		super(PanjeaFormModelHelper.createFormModel(new ContabilitaSettings(), false, FORM_MODEL_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:50dlu,10dlu,right:pref,4dlu,fill:50dlu,left:pref:grow", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addHorizontalSeparator("Gestione contabile", 7);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setHorizontalAlignment(JTextField.RIGHT);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("annoInizioCalcoloSaldo")[1])
				.setHorizontalAlignment(JTextField.RIGHT);
		builder.nextRow();

		builder.addPropertyAndLabel("calcoloCorrispettivi");
		builder.nextRow();

		builder.addPropertyAndLabel("usaContoEffettiAttivi");
		builder.nextRow();

		if (PanjeaSwingUtil.hasPermission("modificaBloccoDocumenti")) {
			builder.addPropertyAndLabel("dataBloccoContabilita");
			builder.nextRow();
		}

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
			builder.addPropertyAndLabel("dataControlloCentriDiCosto");
			builder.nextRow();
		}

		builder.addHorizontalSeparator("Gestione iva", 7);
		builder.nextRow();

		builder.addPropertyAndLabel("tipoPeriodicita");
		builder.addLabel("percTrimestrale", 5);
		builder.addBinding(bf.createBoundPercentageText("percTrimestrale"), 7);
		builder.nextRow();

		builder.addPropertyAndLabel("minimaleIVA");
		builder.nextRow();

		builder.addLabel("codiceIvaPerVentilazione");
		builder.addBinding(bf.createBoundSearchText("codiceIvaPerVentilazione", new String[] { "codice" }), 3);
		builder.nextRow();

		if (PanjeaSwingUtil.hasPermission("modificaBloccoDocumenti")) {
			builder.addPropertyAndLabel("dataBloccoIva");
			builder.nextRow();
		}

		builder.addHorizontalSeparator("Gestione pro rata", 7);
		ProRataSettingTableModel proRataSettingTableModel = new ProRataSettingTableModel();
		TableEditableBinding<ProRataSetting> proRataSettingsBinding = new TableEditableBinding<ProRataSetting>(
				getFormModel(), "proRataSettings", Set.class, proRataSettingTableModel);
		proRataSettingsBinding.getControl().setPreferredSize(new Dimension(300, 150));
		builder.addBinding(proRataSettingsBinding, 1, 28, 7, 1);
		builder.nextRow();

		// Non sò perchè mi ritrovo la proprietà enable e quindi la tabella modificabile senza premere modifica. Forzo
		// enable a false
		getFormModel().getFieldMetadata("proRataSettings").setEnabled(false);

		return builder.getPanel();
	}
}
