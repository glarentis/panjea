package it.eurotn.panjea.rate.rich.forms.calendarirate;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.rich.bd.CalendariRateBD;
import it.eurotn.panjea.rate.rich.bd.ICalendariRateBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.rules.DomainAttributeRequired;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.validation.support.RulesValidator;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.Rules;
import org.springframework.rules.closure.Closure;
import org.springframework.rules.support.DefaultRulesSource;

import com.jgoodies.forms.layout.FormLayout;

public class ClienteInserimentoForm extends AbstractFocussableForm {

	public static final String FORM_ID = "ClienteInserimentoForm";
	public static final String FORMMODEL_ID = "ClienteInserimentoFormModel";

	private FormModel parentFormModel;

	private RefreshableValueHolder calendarioValueModel;

	private ICalendariRateBD calendariRateBD;

	/**
	 * Costruttore.
	 * 
	 * @param parentFormModel
	 *            parentFormModel
	 * 
	 */
	public ClienteInserimentoForm(final FormModel parentFormModel) {
		super(PanjeaFormModelHelper.createFormModel(new ClienteLitePM(), false, FORMMODEL_ID), FORM_ID);
		this.parentFormModel = parentFormModel;

		this.calendariRateBD = RcpSupport.getBean(CalendariRateBD.BEAN_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text dell'entità mi selezioni solo clienti e
		// fornitori
		List<TipoEntita> tipiEntita = new ArrayList<TipoEntita>();
		tipiEntita.add(TipoEntita.CLIENTE);

		ValueModel tipiEntitaValueModel = new ValueHolder(tipiEntita);
		DefaultFieldMetadata tipiEntitaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaValueModel), List.class, true, null);
		getFormModel().add("tipiEntita", tipiEntitaValueModel, tipiEntitaData);

		DefaultRulesSource rulesSource = new DefaultRulesSource();
		rulesSource.addRules(new Rules(ClienteLitePM.class) {
			@Override
			protected void initRules() {
				add("clienti", new DomainAttributeRequired("domainAttributeConstraint"));
			}
		});

		getFormModel().setValidator(new RulesValidator(getFormModel(), rulesSource));
		getFormModel().validate();
	}

	@Override
	protected JComponent createFormControl() {
		calendarioValueModel = new RefreshableValueHolder(new Closure() {

			@Override
			public Object call(Object arg0) {
				List<ClienteLite> clienti = new ArrayList<ClienteLite>();

				CalendarioRate calendarioRate = (CalendarioRate) parentFormModel.getFormObject();
				if (calendarioRate != null) {
					if (calendarioRate.getClienti() != null) {
						clienti.addAll(calendarioRate.getClienti());
					}

					clienti.addAll(calendariRateBD.caricaClientiNonDisponibiliPerCalendario(calendarioRate
							.getCategorieRate()));
				}
				return clienti;
			}
		}, false);

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:160dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("l,c");

		Binding bindingCliente = bf.createBoundSearchText("clienti", new String[] { "codice",
				"anagrafica.denominazione" }, new String[] { "tipiEntita" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bindingCliente);
		setFocusControl(searchPanel.getTextFields().get("codice").getTextField());

		parentFormModel.getValueModel("clienti").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				calendarioValueModel.refresh();
			}
		});
		parentFormModel.getValueModel("categorieRate").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				calendarioValueModel.refresh();
			}
		});
		parentFormModel.getFormObjectHolder().addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				calendarioValueModel.refresh();
			}
		});

		return builder.getPanel();
	}

}
