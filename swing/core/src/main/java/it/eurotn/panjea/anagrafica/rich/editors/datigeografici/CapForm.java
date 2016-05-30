package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.datigeografici.SuddivisioniAmministrativeControlController;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class CapForm extends IdentificativoLocalitaForm {

	private static final String FORM_ID = "capForm";
	private LocalitaInserimentoForm inserimentoForm = null;

	/**
	 * Costruttore.
	 * 
	 * @param datiGeograficiBD
	 *            datiGeograficiBD
	 */
	public CapForm(final IDatiGeograficiBD datiGeograficiBD) {
		super(new Cap(), FORM_ID, datiGeograficiBD);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:default,4dlu,82dlu,default:grow",
				"10dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,80dlu,2dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		suddivisioniAmministrativeControlController = new SuddivisioniAmministrativeControlController();

		((JTextField) builder.addPropertyAndLabel("descrizione", 1, 2, 2)[1]).setColumns(25);
		builder.nextRow();

		JLabel lvl1Label = builder.addLabel("livelloAmministrativo1", 1);
		SearchPanel lvl1SearchPanel = (SearchPanel) builder.addBinding(bf.createBoundSearchText(
				"livelloAmministrativo1", new String[] { "nome" }, new String[] { "nazione" },
				new String[] { Nazione.class.getName() }), 3, 4, 2, 1);
		lvl1SearchPanel.getTextFields().get("nome").setColumns(25);
		suddivisioniAmministrativeControlController.setLvl1Label(lvl1Label);
		suddivisioniAmministrativeControlController.setLvl1SearchPanel(lvl1SearchPanel);
		builder.nextRow();

		JLabel labelLvl2 = builder.addLabel("livelloAmministrativo2", 1);
		SearchPanel searchPanel2 = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("livelloAmministrativo2", new String[] { "nome" }, new String[] { "nazione",
						"livelloAmministrativo1" }, new String[] { Nazione.class.getName(),
						LivelloAmministrativo1.class.getName() }), 3, 6, 2, 1);
		searchPanel2.getTextFields().get("nome").setColumns(25);
		suddivisioniAmministrativeControlController.setLvl2Label(labelLvl2);
		suddivisioniAmministrativeControlController.setLvl2SearchPanel(searchPanel2);
		builder.nextRow();

		JLabel labelLvl3 = builder.addLabel("livelloAmministrativo3", 1);
		SearchPanel searchPanel3 = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("livelloAmministrativo3", new String[] { "nome" }, new String[] { "nazione",
						"livelloAmministrativo1", "livelloAmministrativo2" }, new String[] { Nazione.class.getName(),
						LivelloAmministrativo1.class.getName(), LivelloAmministrativo2.class.getName() }), 3, 8, 2, 1);
		searchPanel3.getTextFields().get("nome").setColumns(25);
		suddivisioniAmministrativeControlController.setLvl3Label(labelLvl3);
		suddivisioniAmministrativeControlController.setLvl3SearchPanel(searchPanel3);
		builder.nextRow();

		JLabel labelLvl4 = builder.addLabel("livelloAmministrativo4", 1);
		SearchPanel searchPanel4 = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("livelloAmministrativo4", new String[] { "nome" }, new String[] { "nazione",
						"livelloAmministrativo1", "livelloAmministrativo2", "livelloAmministrativo3" },
						new String[] { Nazione.class.getName(), LivelloAmministrativo1.class.getName(),
								LivelloAmministrativo2.class.getName(), LivelloAmministrativo3.class.getName() }), 3,
				10, 2, 1);
		searchPanel4.getTextFields().get("nome").setColumns(25);
		suddivisioniAmministrativeControlController.setLvl4Label(labelLvl4);
		suddivisioniAmministrativeControlController.setLvl4SearchPanel(searchPanel4);
		builder.nextRow();

		inserimentoForm = new LocalitaInserimentoForm();
		DefaultBeanTableModel<Localita> localitaTableModel = new LocalitaTableModel();
		Binding localitaBinding = bf.createTableBinding("localita", 110, localitaTableModel, inserimentoForm);

		builder.addLabel("localita", 1, 12, "r,t");
		builder.addBinding(localitaBinding, 3, 12, 2, 1);
		builder.nextRow();

		initPropertyChangeReferences();
		getFormModel().getFormObjectHolder().addValueChangeListener(new ObjectChangedListener());

		return builder.getPanel();
	}

	@Override
	public void setDatiGeografici(DatiGeografici datiGeografici) {
		super.datiGeografici = datiGeografici;
		inserimentoForm.setFormObject(datiGeografici);
	}

	@Override
	public void setFormObject(Object formObject) {
		// ricarico il Cap perchè le città sono lazy
		if (formObject instanceof Cap) {
			Cap cap = (Cap) formObject;
			if (cap.getId() != null) {
				formObject = getDatiGeograficiBD().caricaCap(cap.getId());
			}
		}
		super.setFormObject(formObject);
	}

}
