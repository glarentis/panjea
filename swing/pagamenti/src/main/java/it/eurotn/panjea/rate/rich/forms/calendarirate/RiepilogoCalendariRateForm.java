package it.eurotn.panjea.rate.rich.forms.calendarirate;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.rich.bd.CalendariRateBD;
import it.eurotn.panjea.rate.rich.bd.ICalendariRateBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class RiepilogoCalendariRateForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "riepilogoCalendariRateForm";

	private JLabel nessunCalendarioLabel;
	public static final String NESSUN_CALENDARIO_KEY = FORM_ID + ".nessunCalendario.label";
	public static final String SEPARATOR_CALENDARI_RATE = FORM_ID + ".separatorCalendariRateEntita";

	private JPanel calendariRatePanel;
	private JideTableWidget<CalendarioRate> calendariRateTable;

	protected ICalendariRateBD calendariRateBD;

	/**
	 * Costruttore di default.
	 * 
	 */
	public RiepilogoCalendariRateForm() {
		super(PanjeaFormModelHelper.createFormModel(new String(), false, FORM_ID + "Model"), FORM_ID);

		this.calendariRateBD = RcpSupport.getBean(CalendariRateBD.BEAN_ID);

		initComponents();
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,fill:pref:grow", "default,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());

		builder.addHorizontalSeparator(SEPARATOR_CALENDARI_RATE, 2);

		builder.setLabelAttributes("r,t");
		builder.setComponentAttributes("r,t");

		builder.addComponent(calendariRatePanel, 1, 2);

		return builder.getPanel();
	}

	/**
	 * Carica i calendari rate.
	 * 
	 * @param object
	 *            object
	 * @return calendari caricati
	 */
	protected List<CalendarioRate> getCalendariRate(Object object) {

		List<CalendarioRate> result = null;

		if (object instanceof Cliente) {

			Cliente cliente = (Cliente) object;

			if (cliente.getId() != null) {
				ClienteLite clienteLite = new ClienteLite();
				clienteLite.setId(cliente.getId());
				clienteLite.setVersion(cliente.getVersion());
				result = calendariRateBD.caricaCalendariRateCliente(clienteLite);
			}
		}

		return result;
	}

	/**
	 * Inizializza tutti i componenti.
	 */
	private void initComponents() {

		nessunCalendarioLabel = getComponentFactory().createLabel(NESSUN_CALENDARIO_KEY);
		nessunCalendarioLabel.setName("nessunCalendarioEntitaLabel");

		calendariRatePanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		calendariRatePanel.setBorder(BorderFactory.createEmptyBorder());

		calendariRateTable = new JideTableWidget<CalendarioRate>("calendariRateEntitaTable", new String[] {
				"descrizione", "categorieRateToString" }, CalendarioRate.class);
		calendariRateTable.setPropertyCommandExecutor(new ActionCommandExecutor() {

			@Override
			public void execute() {
				CalendarioRate calSelected = calendariRateTable.getSelectedObject();

				if (calSelected != null) {
					LifecycleApplicationEvent event = new OpenEditorEvent(calendariRateBD.caricaCalendarioRate(
							calSelected, true));
					Application.instance().getApplicationContext().publishEvent(event);
				}
			}
		});
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);

		calendariRatePanel.removeAll();

		List<CalendarioRate> calendari = getCalendariRate(formObject);

		if (calendari != null && !calendari.isEmpty()) {
			calendariRatePanel.add(calendariRateTable.getComponent());
			calendariRateTable.setRows(calendari);
		} else {
			calendariRatePanel.add(nessunCalendarioLabel);
		}

	}

}
