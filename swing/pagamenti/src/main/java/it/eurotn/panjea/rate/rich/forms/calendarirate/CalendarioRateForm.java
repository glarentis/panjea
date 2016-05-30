package it.eurotn.panjea.rate.rich.forms.calendarirate;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.partite.rich.bd.PartiteBD;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class CalendarioRateForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "calendarioRateForm";

	private IPartiteBD partiteBD;

	/**
	 * Costruttore.
	 * 
	 */
	public CalendarioRateForm() {
		super(PanjeaFormModelHelper.createFormModel(new CalendarioRate(), false, FORM_ID), FORM_ID);

		partiteBD = RcpSupport.getBean(PartiteBD.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:default, 4dlu, fill:default",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,80dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());

		builder.setLabelAttributes("r,c");

		builder.addPropertyAndLabel("descrizione", 1, 2);

		builder.addLabel("categorieRate", 1, 4, "r,t");
		builder.addBinding(bf.createBoundCheckBoxList("categorieRate",
				partiteBD.caricaCategorieRata("descrizione", null), "descrizione",
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION), 3, 4);

		builder.addPropertyAndLabel("calendarioAziendale", 1, 6);

		ClienteInserimentoForm inserimentoForm = new ClienteInserimentoForm(getFormModel());
		DefaultBeanTableModel<ClienteLite> clienteTableModel = new ClienteTableModel();
		Binding clienteBinding = bf.createTableBinding("clienti", 160, clienteTableModel, inserimentoForm);

		builder.addLabel("clienti", 1, 8, "r,t");
		builder.addBinding(clienteBinding, 3, 8);

		return builder.getPanel();
	}

}
