package it.eurotn.panjea.tesoreria.rich.editors.preference;

import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.preferences.GeneralSettingsPM;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.TesoreriaSettings;
import it.eurotn.panjea.tesoreria.manager.LettoreAssegniManager;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class TesoreriaSettingsForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "tesoreriaSettingsForm";
	private static final String FORM_MODEL_ID = "tesoreriaSettingsFormModel";
	private LettoreAssegniManager lettoreAssegniManager;
	private IDocumentiBD documentiBD;

	/**
	 * COSTRUTTORE.
	 */
	public TesoreriaSettingsForm() {
		super(PanjeaFormModelHelper.createFormModel(new TesoreriaSettings(), false, FORM_MODEL_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:50dlu,10dlu,right:pref,350dlu,left:pref:grow",
				"4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.nextRow();
		builder.addLabel("letturaAssegni");
		builder.addBinding(bf.createBoundCheckBox(GeneralSettingsPM.LETTURA_ASSEGNI), 3);
		builder.nextRow();
		getValueModel(GeneralSettingsPM.LETTURA_ASSEGNI).addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String eventName;
				Boolean letturaAssegni = (Boolean) evt.getNewValue();
				if (letturaAssegni) {
					lettoreAssegniManager.start();
					eventName = LettoreAssegniManager.ENABLE_LETTORE_ASSEGNI_EVENT;

				} else {
					lettoreAssegniManager.stop();
					eventName = LettoreAssegniManager.DISABLE_LETTORE_ASSEGNI_EVENT;
				}

				LifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(eventName, this);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		});

		// se il sistema non è windows lascio in sola lettura la proprietà perchè non ci sono le librerie da
		// caricare
		String osName = System.getProperty("os.name");

		// verifico che il sistema operativo sia windows altrimenti non ho le librerie da caricare
		if (!(osName.toLowerCase().contains("windows"))) {
			getFormModel().getFieldMetadata(GeneralSettingsPM.LETTURA_ASSEGNI).setEnabled(false);
		}

		builder.addHorizontalSeparator("Calcolo fido", 6);
		builder.nextRow();
		builder.addBinding(bf.createBoundShuttleList("tipiDocumentoFido",
				documentiBD.caricaTipiDocumento(null, null, false), "descrizione"), 1, 8, 6, 1);
		return builder.getPanel();
	}

	/**
	 * @return Returns the lettoreAssegniManager.
	 */
	public LettoreAssegniManager getLettoreAssegniManager() {
		return lettoreAssegniManager;
	}

	/**
	 * 
	 * @param documentiBD
	 *            bd documenti
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	}

	/**
	 * @param lettoreAssegniManager
	 *            The lettoreAssegniManager to set.
	 */
	public void setLettoreAssegniManager(LettoreAssegniManager lettoreAssegniManager) {
		this.lettoreAssegniManager = lettoreAssegniManager;
	}
}