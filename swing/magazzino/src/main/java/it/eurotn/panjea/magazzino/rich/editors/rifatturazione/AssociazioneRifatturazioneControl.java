package it.eurotn.panjea.magazzino.rich.editors.rifatturazione;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.list.renderer.SedeMagazzinoLiteListCellRenderer;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.CheckBoxList;

/**
 * Crea i componenti necessari per permettere l'associazione fra sede magazzino
 * e sede di rifatturazione.
 * 
 * @author fattazzo
 * 
 */
public class AssociazioneRifatturazioneControl extends AbstractControlFactory {

	private class AssociaSediPerRifatturazioneCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "associaSediPerRifatturazioneCommand";

		/**
		 * Costruttore.
		 */
		public AssociaSediPerRifatturazioneCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(GestioneRifatturazionePage.PAGE_ID + ".controller");
			RcpSupport.configure(this);
			setEnabled(false);
		}

		@Override
		protected void doExecuteCommand() {

			// associo le sedi solamente se ce ne sono di selezionate
			if (sediDaAssociare.getCheckBoxListSelectedIndices().length > 0) {
				SedeMagazzinoLite sedeDiRifat = (SedeMagazzinoLite) sediDiRifatturazione.getSelectedValue();
				List<SedeMagazzinoLite> sedidaAss = new ArrayList<SedeMagazzinoLite>();

				for (int i = 0; i < sediDaAssociare.getCheckBoxListSelectedValues().length; i++) {
					SedeMagazzinoLite sede = (SedeMagazzinoLite) sediDaAssociare.getCheckBoxListSelectedValues()[i];
					sedidaAss.add(sede);
				}

				magazzinoAnagraficaBD.associaSediMagazzinoPerRifatturazione(sedeDiRifat, sedidaAss);

				for (SedeMagazzinoLite sedeMagazzinoLite : sedidaAss) {
					((DefaultListModel) sediDaAssociare.getModel()).removeElement(sedeMagazzinoLite);
				}
				tablePageEditor.refreshData();
			}
		}

	}

	private static final String BOX_SEDI_DI_RIFATTURAZIONE_TITLE = "boxSediDiRifatturazioneTitle.label";
	private static final String BOX_SEDI_DA_ASSOCIARE_TITLE = "boxSediDaAssociareTitle.label";

	private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private JList sediDiRifatturazione;
	private final CheckBoxList sediDaAssociare = new CheckBoxList();
	private AssociaSediPerRifatturazioneCommand associaSediPerRifatturazioneCommand;
	private final AbstractTablePageEditor<?> tablePageEditor;

	/**
	 * Costruttore.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            {@link IMagazzinoAnagraficaBD}
	 * @param abstractTablePageEditor
	 *            abstractTablePageEditor
	 */
	public AssociazioneRifatturazioneControl(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD,
			final AbstractTablePageEditor<?> abstractTablePageEditor) {
		super();
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		this.tablePageEditor = abstractTablePageEditor;
	}

	@Override
	protected JComponent createControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		JPanel sediPanel = getComponentFactory().createPanel(new GridLayout(0, 2));

		sediPanel.add(createSediDiRifatturazioneControl());
		sediPanel.add(createSediDaAssociareControl());

		rootPanel.add(sediPanel, BorderLayout.CENTER);

		return rootPanel;
	}

	/**
	 * Crea i controlli relativi alle sedi magazzino da associare.
	 * 
	 * @return controlli creati
	 */
	private JComponent createSediDaAssociareControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.setBorder(BorderFactory.createTitledBorder(getMessage(BOX_SEDI_DA_ASSOCIARE_TITLE)));

		// avendo un clienteLite in ClienteWrapper devo filtrare i record per
		// cliente quindi ho su ClienteWrapper il
		// tipoEntita da passare alla searchObject
		final ValidatingFormModel clienteFormModel = (ValidatingFormModel) PanjeaFormModelHelper.createFormModel(
				new ClienteWrapper(), false, "ClienteWrapper");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) Application
				.services().getService(BindingFactoryProvider.class)).getBindingFactory(clienteFormModel);
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		Binding bindingEntita = bf.createBoundSearchText("cliente",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }, EntitaLite.class);
		SearchPanel searchPanelEntita = (SearchPanel) bindingEntita.getControl();
		searchPanelEntita.getTextFields().get("codice").setColumns(5);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(18);
		builder.add(bindingEntita);
		builder.row();
		clienteFormModel.getValueModel("cliente").addValueChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				DefaultListModel model = new DefaultListModel();
				model.removeAllElements();

				ClienteLite cliente = (ClienteLite) clienteFormModel.getValueModel("cliente").getValue();

				if (cliente != null && cliente.getId() != null) {

					for (SedeMagazzinoLite sedeMagazzino : magazzinoAnagraficaBD
							.caricaSediRifatturazioneNonAssociate(cliente)) {
						model.addElement(sedeMagazzino);
					}
				}

				sediDaAssociare.setModel(model);
				sediDaAssociare.setCellRenderer(new SedeMagazzinoLiteListCellRenderer());
			}
		});
		rootPanel.add(builder.getForm(), BorderLayout.NORTH);
		sediDaAssociare.setOpaque(false);
		rootPanel.add(getComponentFactory().createScrollPane(sediDaAssociare), BorderLayout.CENTER);

		JPanel buttonPanel = getComponentFactory().createPanel(new BorderLayout());
		buttonPanel.add(getAssociaSediPerRifatturazioneCommand().createButton(), BorderLayout.EAST);
		rootPanel.add(buttonPanel, BorderLayout.PAGE_END);

		return rootPanel;
	}

	/**
	 * Crea i controlli relativi alle sedi magazzino di rifatturazione.
	 * 
	 * @return controlli creati
	 */
	private JComponent createSediDiRifatturazioneControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.setBorder(BorderFactory.createTitledBorder(getMessage(BOX_SEDI_DI_RIFATTURAZIONE_TITLE)));

		DefaultListModel model = new DefaultListModel();
		model.removeAllElements();
		for (SedeMagazzinoLite sedeMagazzino : magazzinoAnagraficaBD.caricaSediMagazzinoDiRifatturazione()) {
			model.addElement(sedeMagazzino);
		}

		sediDiRifatturazione = new JList(model);
		sediDiRifatturazione.setCellRenderer(new SedeMagazzinoLiteListCellRenderer());
		sediDiRifatturazione.setOpaque(false);
		sediDiRifatturazione.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				getAssociaSediPerRifatturazioneCommand().setEnabled(sediDiRifatturazione.getSelectedIndex() != -1);
			}
		});
		rootPanel.add(getComponentFactory().createScrollPane(sediDiRifatturazione), BorderLayout.CENTER);

		return rootPanel;
	}

	/**
	 * @return the associaSediPerRifatturazioneCommand
	 */
	public AssociaSediPerRifatturazioneCommand getAssociaSediPerRifatturazioneCommand() {
		if (associaSediPerRifatturazioneCommand == null) {
			associaSediPerRifatturazioneCommand = new AssociaSediPerRifatturazioneCommand();
		}

		return associaSediPerRifatturazioneCommand;
	}
}
