/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto.sedi;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.form.support.DefaultFormModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.tree.TreeSelectionListenerSupport;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.StyledLabelBuilder;
import com.jidesoft.swing.TreeSearchable;

/**
 * Crea tutti i componenti per gestire l'inserimento, la visualizzazione e la cancellazione delle sedi magazzino del
 * contratto.
 * 
 * @author fattazzo
 */
public class SediContrattoComponent extends AbstractControlFactory {

	private class AddEntitaCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "addEntitaToContrattoCommand";

		/**
		 * Costruttore.
		 */
		public AddEntitaCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(ContrattoSediPage.PAGE_ID + ".controller");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			EntitaLite entitaLite = (EntitaLite) sedeFormModel.getValueModel("entita").getValue();

			if (entitaLite != null && entitaLite.getId() != null) {

				SedeMagazzinoLite sedeMagazzinoLite = new SedeMagazzinoLite();
				sedeMagazzinoLite.getSedeEntita().getSede().setDescrizione("Tutte le sedi");
				sedeMagazzinoLite.getSedeEntita().setEntita(entitaLite);
				boolean added = ((SediMagazzinoTreeModel) treeSedi.getModel()).aggiungiSede(sedeMagazzinoLite);

				if (added) {
					Contratto contrattoSalvato = contrattoBD.associaEntitaContratto(contratto, entitaLite);
					contratto = contrattoBD.caricaContratto(contrattoSalvato, true);

					SediMagazzinoAdapter adapter = new SediMagazzinoAdapter();
					treeSedi.setModel(new SediMagazzinoTreeModel(adapter.transform(contratto.getSediMagazzino(),
							contratto.getEntita())));
					getExpandCommand().expandAll();
					updateSediEntitaComponent();
				}
			}
			// notifico alla page che ho cambiato il Contratto
			SediContrattoComponent.this.getContrattoChangedPublisher().publish(contratto);
			sedeFormModel.getValueModel("entita").setValue(new ClienteLite());
			codiceEntitaComponent.requestFocusInWindow();
		}

	}

	/**
	 * Command per aggiungere al {@link Contratto} una sede magazzino.
	 * 
	 * @author Leonardo
	 */
	private class AddSedeCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "addSedeToContrattoCommand";

		/**
		 * Default constructor.
		 */
		public AddSedeCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(ContrattoSediPage.PAGE_ID + ".controller");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			SedeMagazzinoLite sedeMagazzinoLite = (SedeMagazzinoLite) sedeFormModel.getValueModel("sedeMagazzino")
					.getValue();

			if (sedeMagazzinoLite != null && sedeMagazzinoLite.getId() != null) {
				boolean added = ((SediMagazzinoTreeModel) treeSedi.getModel()).aggiungiSede(sedeMagazzinoLite);

				if (added) {
					Contratto contrattoSalvato = contrattoBD.associaSedeContratto(contratto, sedeMagazzinoLite);
					contratto = contrattoSalvato;

					getExpandCommand().expandAll();
					sediMagazzinoValueHolder.refresh();
				}
			}
			// notifico alla page che ho cambiato il Contratto
			SediContrattoComponent.this.getContrattoChangedPublisher().publish(contratto);
			sedeFormModel.getValueModel("entita").setValue(new ClienteLite());
			codiceEntitaComponent.requestFocusInWindow();
		}
	}

	/**
	 * Command per espandere il tree delle entita' visualizzando cosi' le sedi aggiunte.
	 * 
	 * @author Leonardo
	 */
	private class ExpandCommand extends ApplicationWindowAwareCommand {

		private static final String ESPANDI_COMMAND = "sediContrattoExpandCommand";
		private static final String EXPAND_STATE = ".expand";
		private static final String COLLAPSE_STATE = ".collapse";
		private boolean expandTree;
		private final CommandFaceDescriptor expandDescriptor;
		private final CommandFaceDescriptor collapseDescriptor;

		/**
		 * Costruttore di default.
		 */
		public ExpandCommand() {
			super(ESPANDI_COMMAND);
			CommandConfigurer configurer = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);

			setSecurityControllerId(ESPANDI_COMMAND);
			configurer.configure(this);
			expandTree = false;

			String toExpandString = getMessage(ESPANDI_COMMAND + EXPAND_STATE + ".label");
			String toCollapseString = getMessage(ESPANDI_COMMAND + COLLAPSE_STATE + ".label");
			Icon toExpandIcon = getIconSource().getIcon(ESPANDI_COMMAND + EXPAND_STATE + ".icon");
			Icon toCollapseIcon = getIconSource().getIcon(ESPANDI_COMMAND + COLLAPSE_STATE + ".icon");
			collapseDescriptor = new CommandFaceDescriptor(toExpandString, toExpandIcon, null);
			expandDescriptor = new CommandFaceDescriptor(toCollapseString, toCollapseIcon, null);
			setFaceDescriptor(collapseDescriptor);
		}

		/**
		 * Collapse di tutti i nodi.
		 */
		private void collapseAll() {
			int row = treeSedi.getRowCount() - 1;
			while (row >= 0) {
				treeSedi.collapseRow(row);
				row--;
			}
		}

		@Override
		protected void doExecuteCommand() {
			if (!expandTree) {
				expandAll();
				setFaceDescriptor(expandDescriptor);
			} else {
				collapseAll();
				setFaceDescriptor(collapseDescriptor);
			}
			this.expandTree = !expandTree;
		}

		/**
		 * Expand di tutti i nodi.
		 */
		private void expandAll() {
			int row = 0;
			while (row < treeSedi.getRowCount()) {
				treeSedi.expandRow(row);
				row++;
			}
		}

	}

	/**
	 * Command per rimuovere dal contratto la sede aggiunta.
	 * 
	 * @author Leonardo
	 */
	private class RemoveSedeCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "removeSedeToContrattoCommand";

		/**
		 * Costruttore di default.
		 */
		public RemoveSedeCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(ContrattoSediPage.PAGE_ID + ".controller");
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			DefaultMutableTreeNode sedeNode = (DefaultMutableTreeNode) treeSedi.getLastSelectedPathComponent();
			if (sedeNode != null) {
				SedeMagazzinoLite sedeMagazzinoLite = (SedeMagazzinoLite) sedeNode.getUserObject();

				Contratto contrattoSalvato = null;
				if (sedeMagazzinoLite.isNew()) {
					contrattoSalvato = contrattoBD.rimuoviEntitaContratto(contratto, sedeMagazzinoLite.getSedeEntita()
							.getEntita());
					contratto = contrattoBD.caricaContratto(contrattoSalvato, true);

					SediMagazzinoAdapter adapter = new SediMagazzinoAdapter();
					treeSedi.setModel(new SediMagazzinoTreeModel(adapter.transform(contratto.getSediMagazzino(),
							contratto.getEntita())));
				} else {
					contrattoSalvato = contrattoBD.rimuoviSedeContratto(contratto, sedeMagazzinoLite);
					((SediMagazzinoTreeModel) treeSedi.getModel()).rimuoviSede(sedeNode);
					contratto = contrattoSalvato;
				}

				getExpandCommand().expandAll();
				updateSediEntitaComponent();
				SediContrattoComponent.this.getContrattoChangedPublisher().publish(contratto);

			}
		}
	}

	/**
	 * Classe usata solo per poter creare un formmodel per la classe sedemagazzino e poterla bindare con una search
	 * text.
	 * 
	 * @author fattazzo
	 */
	public class SedeMagazzinoWrapper {

		private EntitaLite entita;
		private SedeMagazzinoLite sedeMagazzino;

		/**
		 * Default Constructor.
		 */
		public SedeMagazzinoWrapper() {
			// this.sedeMagazzino = new SedeMagazzinoLite();
			// this.entita = new ClienteLite();
		}

		/**
		 * @return the entita
		 */
		public EntitaLite getEntita() {
			return entita;
		}

		/**
		 * @return the sedeMagazzino
		 */
		public SedeMagazzinoLite getSedeMagazzino() {
			return sedeMagazzino;
		}

		/**
		 * @param entita
		 *            the entita to set
		 */
		public void setEntita(EntitaLite entita) {
			this.entita = entita;
		}

		/**
		 * @param sedeMagazzino
		 *            the sedeMagazzino to set
		 */
		public void setSedeMagazzino(SedeMagazzinoLite sedeMagazzino) {
			this.sedeMagazzino = sedeMagazzino;
		}

	}

	private final class TreeSearchableExtension extends TreeSearchable {
		private TreeSearchableExtension(JTree jtree) {
			super(jtree);

		}

		@Override
		protected String convertElementToString(Object obj) {
			TreePath treePath = (TreePath) obj;
			Object userObject = ((DefaultMutableTreeNode) treePath.getLastPathComponent()).getUserObject();
			String toString = "";
			if (userObject instanceof ClienteLite) {
				ClienteLite clienteLite = (ClienteLite) userObject;
				toString = clienteLite.getCodice() + " - " + clienteLite.getAnagrafica().getDenominazione();
			}
			return toString;
		}
	}

	public static final String PAGE_COMPONENT_ID = "sediContrattoComponent";
	private JTree treeSedi = null;
	private RefreshableValueHolder sediMagazzinoValueHolder = null;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private IContrattoBD contrattoBD = null;
	private RemoveSedeCommand removeSedeCommand = null;
	private FormModel sedeFormModel = null;
	private Contratto contratto = null;

	private ContrattoChangedPublisher contrattoChangedPublisher = null;
	private DefaultOverlayable overlayMainPanel;
	private AddSedeCommand addSedeCommand = null;
	private AddEntitaCommand addEntitaCommand = null;
	private ExpandCommand expandCommand = null;
	private SearchTextField codiceEntitaComponent;

	/**
	 * Costruttore di default.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 * @param contrattoBD
	 *            contrattoBD
	 */
	public SediContrattoComponent(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD, final IContrattoBD contrattoBD) {
		super();
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		this.contrattoBD = contrattoBD;
	}

	/**
	 * 
	 * @return button per inserire l'entità
	 */
	private JComponent createAddEntitaButton() {
		return getAddEntitaCommand().createButton();
	}

	/**
	 * 
	 * @return button per inserire la sede dell'entità.
	 */
	private JComponent createAddSedeButton() {
		return getAddSedeCommand().createButton();
	}

	/**
	 * 
	 * 
	 * @return Crea tutti i controlli necessari per aggiungere una sede.
	 */
	private JComponent createAddSedeControl() {
		sedeFormModel = PanjeaFormModelHelper
				.createFormModel(new SedeMagazzinoWrapper(), false, "sedeMagazzinoWrapper");
		sedeFormModel.getValueModel("entita").addValueChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				sediMagazzinoValueHolder.refresh();
				updateSediEntitaComponent();
			}
		});

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) Application
				.services().getService(BindingFactoryProvider.class)).getBindingFactory(sedeFormModel);
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		SearchPanel searchPanel = (SearchPanel) builder.add(bf.createBoundSearchText("entita", new String[] { "codice",
				"anagrafica.denominazione" }))[1];
		codiceEntitaComponent = searchPanel.getTextFields().get("codice");
		codiceEntitaComponent.setColumns(5);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(18);
		builder.getLayoutBuilder().cell(createAddEntitaButton());
		builder.row();
		sediMagazzinoValueHolder = new RefreshableValueHolder(new Closure() {
			@Override
			public Object call(Object argument) {
				EntitaLite entitaLite = (EntitaLite) sedeFormModel.getValueModel("entita").getValue();

				List<SedeMagazzinoLite> list = new ArrayList<SedeMagazzinoLite>();
				// se l'entita' a' stata selezionata carico le sue sedi
				// magazzino
				if (entitaLite != null && entitaLite.getId() != null) {
					Entita entita = new Cliente();
					entita.setId(entitaLite.getId());
					list = magazzinoAnagraficaBD.caricaSediMagazzinoByEntita(entita);
				}
				return list;
			}
		});
		sediMagazzinoValueHolder.refresh();
		builder.add(bf.createBoundComboBox("sedeMagazzino", sediMagazzinoValueHolder, "sedeEntita.sede.descrizione"));
		builder.getLayoutBuilder().cell(createAddSedeButton());
		builder.row();
		builder.addSeparator("");

		JPanel panelAddSede = getComponentFactory().createPanel(new BorderLayout());
		panelAddSede.add(builder.getForm(), BorderLayout.CENTER);

		return panelAddSede;
	}

	@Override
	protected JComponent createControl() {
		if (overlayMainPanel == null) {
			JPanel mainPanelContent = getComponentFactory().createPanel(new BorderLayout());
			mainPanelContent.add(createAddSedeControl(), BorderLayout.NORTH);
			mainPanelContent.add(createTreeSediControl(), BorderLayout.CENTER);
			overlayMainPanel = new DefaultOverlayable(mainPanelContent);
			overlayMainPanel.addOverlayComponent(StyledLabelBuilder.createStyledLabel("{"
					+ getMessage(CategorieSediContrattoComponent.TUTTE_LE_CATEGORIE_DESCRIZIONE) + ":f:gray}"));
		}
		return overlayMainPanel;
	}

	/**
	 * Crea il pannello contenente il tree.
	 * 
	 * @return {@link JComponent}
	 */
	private JComponent createTreeSediControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		treeSedi = new JTree();
		treeSedi.setOpaque(false);
		treeSedi.setCellRenderer(new SediMagazzinoTreeCellRenderer());
		treeSedi.setRootVisible(false);
		treeSedi.setBackground(UIManager.getColor("JPanel.background"));
		new TreeSearchableExtension(treeSedi);

		treeSedi.addTreeSelectionListener(new TreeSelectionListenerSupport() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				if (selectedNode != null && selectedNode.getUserObject() instanceof SedeMagazzinoLite) {
					removeSedeCommand.setEnabled(true);
				} else {
					removeSedeCommand.setEnabled(false);
				}
			}

		});
		panel.add(getComponentFactory().createScrollPane(treeSedi), BorderLayout.CENTER);

		AbstractCommand[] commandBar = new AbstractCommand[] { getRemoveSedeCommand() };
		JComponent buttonBarPanel = ((JECCommandGroup) getCommandGroup(commandBar)).createToolBar();
		buttonBarPanel.setBorder(GuiStandardUtils.createTopAndBottomBorder(3));
		panel.add(buttonBarPanel, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * @param readOnly
	 *            Cambia lo stato readOnly del formModel.
	 */
	public void disableComponents(boolean readOnly) {
		((DefaultFormModel) sedeFormModel).setReadOnly(readOnly);
	}

	/**
	 * @return the addEntitaCommand
	 */
	public AddEntitaCommand getAddEntitaCommand() {
		if (addEntitaCommand == null) {
			addEntitaCommand = new AddEntitaCommand();
			// per disattivare il command quando disattivo il formModel
			new PanjeaFormGuard((ValidatingFormModel) sedeFormModel, addEntitaCommand);
		}

		return addEntitaCommand;
	}

	public AddSedeCommand getAddSedeCommand() {
		if (addSedeCommand == null) {
			addSedeCommand = new AddSedeCommand();
			// per disattivare il command quando disattivo il formModel
			new PanjeaFormGuard((ValidatingFormModel) sedeFormModel, addSedeCommand);
		}
		return addSedeCommand;
	}

	private CommandGroup getCommandGroup(AbstractCommand[] commandBar) {
		JECCommandGroup commandGroup = new JECCommandGroup();
		if (commandBar != null) {
			for (AbstractCommand element : commandBar) {
				commandGroup.add(element);
			}
		} else {
			commandGroup = null;
		}
		return commandGroup;
	}

	public ContrattoChangedPublisher getContrattoChangedPublisher() {
		if (contrattoChangedPublisher == null) {
			contrattoChangedPublisher = new ContrattoChangedPublisher(PAGE_COMPONENT_ID);
		}
		return contrattoChangedPublisher;
	}

	/**
	 * @return the expandCommand
	 */
	public ExpandCommand getExpandCommand() {
		if (expandCommand == null) {
			expandCommand = new ExpandCommand();
		}

		return expandCommand;
	}

	private AbstractCommand getRemoveSedeCommand() {
		if (removeSedeCommand == null) {
			removeSedeCommand = new RemoveSedeCommand();
			removeSedeCommand.setEnabled(false);
		}
		return removeSedeCommand;
	}

	public void loadData(Contratto contrattoCorrente) {
		SediMagazzinoAdapter adapter = new SediMagazzinoAdapter();
		treeSedi.setModel(new SediMagazzinoTreeModel(adapter.transform(contrattoCorrente.getSediMagazzino(),
				contrattoCorrente.getEntita())));
		getExpandCommand().expandAll();
		this.contratto = contrattoCorrente;
		disableComponents(contratto.isNew() || contratto.isTutteCategorieSedeMagazzino());
		overlayMainPanel.setOverlayVisible(contratto.isTutteCategorieSedeMagazzino());
	}

	private void updateSediEntitaComponent() {
		EntitaLite entitaLite = (EntitaLite) sedeFormModel.getValueModel("entita").getValue();

		getAddSedeCommand().setEnabled(
				entitaLite == null || !((SediMagazzinoTreeModel) treeSedi.getModel()).isEntitaAssociata(entitaLite));

		getAddEntitaCommand().setEnabled(
				entitaLite == null || !((SediMagazzinoTreeModel) treeSedi.getModel()).isEntitaAssociata(entitaLite));
	}

}
