/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto.sedi;

import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.command.JECCommandGroup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.form.support.DefaultFormModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

/**
 * Crea tutti i componenti per gestire l'inserimento, la visualizzazione e la cancellazione delle categorie sedi del
 * contratto.
 * 
 * @author fattazzo
 */
public class CategorieSediContrattoComponent extends AbstractControlFactory {

	/**
	 * Command per aggiungere al contratto tutte le categorie sede magazzino.
	 * 
	 * @author Leonardo
	 */
	private class AddAllCategorieSedeCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "addAllCategorieSedeToContrattoCommand";
		private static final String CONFIRM_COMMAND = "addAllCategorieSedeToContrattoCommand.confirm.message";

		/**
		 * Costruttore di default.
		 */
		public AddAllCategorieSedeCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(ContrattoSediPage.PAGE_ID + ".controller");
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			int result = RcpSupport.showConfirmationDialog((Component) null, getMessage(CONFIRM_COMMAND),
					new Object[] {}, JOptionPane.OK_CANCEL_OPTION);
			if (result == 0) {
				contratto.setCategorieSediMagazzino(new ArrayList<CategoriaSedeMagazzino>());
				contratto.setSediMagazzino(new ArrayList<SedeMagazzinoLite>());
				contratto.setTutteCategorieSedeMagazzino(true);
				Contratto contrattoSalvato = contrattoBD.salvaContratto(contratto, false);
				contratto = contrattoSalvato;

				CategoriaSedeMagazzino tutteCategorie = getCategoriaSedeMagazzinoTutte();
				categorieAggiunte.clear();
				categorieAggiunte.put(tutteCategorie.getId(), tutteCategorie);
				((DefaultListModel) listCategorie.getModel()).removeAllElements();
				((DefaultListModel) listCategorie.getModel()).addElement(tutteCategorie);
				categorieSediValueHolder.refresh();

				getContrattoChangedPublisher().publish(contratto);
			}
		}
	}

	/**
	 * Command per aggiungere una categoria sede al contratto.
	 * 
	 * @author Leonardo
	 */
	private class AddCategoriaSedeCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "addCategoriaSedeToContrattoCommand";

		/**
		 * Costruttore di default.
		 */
		public AddCategoriaSedeCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(ContrattoSediPage.PAGE_ID + ".controller");
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			CategoriaSedeMagazzino categoriaSedeMagazzino = (CategoriaSedeMagazzino) categoriaSedeFormModel
					.getValueModel("categoriaSedeMagazzino").getValue();

			if (categoriaSedeMagazzino != null && categoriaSedeMagazzino.getId() != null) {
				if (categorieAggiunte.get(categoriaSedeMagazzino.getId()) == null) {

					CategoriaSedeMagazzino tutteCategorie = getCategoriaSedeMagazzinoTutte();
					categorieAggiunte.remove(tutteCategorie.getId());
					((DefaultListModel) listCategorie.getModel()).removeElement(tutteCategorie);

					contratto.setTutteCategorieSedeMagazzino(false);
					Contratto contrattoSalvato = contrattoBD.associaCategoriaSedeContratto(contratto,
							categoriaSedeMagazzino);
					contratto = contrattoSalvato;
					((DefaultListModel) listCategorie.getModel()).addElement(categoriaSedeMagazzino);
					categorieSediValueHolder.refresh();

					categorieAggiunte.put(categoriaSedeMagazzino.getId(), categoriaSedeMagazzino);
					getContrattoChangedPublisher().publish(contratto);
				}
			}
		}
	}

	/**
	 * Classe usata solo per poter creare un formmodel per la classe categoria sede magazzino e poterla bindare con una
	 * combobox.
	 * 
	 * @author fattazzo
	 */
	public class CategoriaSedeMagazzinoWrapper {

		private CategoriaSedeMagazzino categoriaSedeMagazzino;

		/**
		 * Costruttore default.
		 */
		public CategoriaSedeMagazzinoWrapper() {
			categoriaSedeMagazzino = new CategoriaSedeMagazzino();
		}

		/**
		 * @return the categoriaSedeMagazzino
		 */
		public CategoriaSedeMagazzino getCategoriaSedeMagazzino() {
			return categoriaSedeMagazzino;
		}

		/**
		 * @param categoriaSedeMagazzino
		 *            the categoriaSedeMagazzino to set
		 */
		public void setCategoriaSedeMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino) {
			this.categoriaSedeMagazzino = categoriaSedeMagazzino;
		}

	}

	private class RemoveCategoriaSedeCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "removeCategoriaSedeCommand";

		/**
		 * Costruttore di default.
		 */
		public RemoveCategoriaSedeCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(ContrattoSediPage.PAGE_ID + ".controller");
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			CategoriaSedeMagazzino categoriaSedeMagazzino = (CategoriaSedeMagazzino) listCategorie.getSelectedValue();
			if (categoriaSedeMagazzino.getId() != null) {
				Contratto contrattoSalvato = null;
				if (categoriaSedeMagazzino.getId().intValue() != -1) {
					contrattoSalvato = contrattoBD.rimuoviCategoriaSedeContratto(contratto, categoriaSedeMagazzino);
				} else {
					contratto.setTutteCategorieSedeMagazzino(false);
					contrattoSalvato = contrattoBD.salvaContratto(contratto, false);
				}
				contratto = contrattoSalvato;
				((DefaultListModel) listCategorie.getModel()).removeElement(categoriaSedeMagazzino);
				categorieSediValueHolder.refresh();

				categorieAggiunte.remove(categoriaSedeMagazzino.getId());
				getContrattoChangedPublisher().publish(contratto);
			}
		}
	}

	private ContrattoChangedPublisher contrattoChangedPublisher = null;
	public static final String PAGE_COMPONENT_ID = "categorieSediContrattoComponent";
	public static final String TUTTE_LE_CATEGORIE_DESCRIZIONE = "categoria.tutteCategorieDescrizione";
	private final IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);
	private JList listCategorie = null;
	private FormModel categoriaSedeFormModel = null;
	private RefreshableValueHolder categorieSediValueHolder = null;
	private RemoveCategoriaSedeCommand removeCategoriaSedeCommand = null;
	private Contratto contratto = null;
	private IContrattoBD contrattoBD = null;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private Map<Integer, CategoriaSedeMagazzino> categorieAggiunte = null;
	private CategoriaSedeMagazzino categoriaSedeMagazzinoTutte = null;
	private AddCategoriaSedeCommand addCategoriaSedeCommand;
	private AddAllCategorieSedeCommand addAllCategorieSedeCommand;

	public CategorieSediContrattoComponent(IMagazzinoAnagraficaBD magazzinoAnagraficaBD, IContrattoBD contrattoBD) {
		super();
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		this.contrattoBD = contrattoBD;
		this.categorieAggiunte = new HashMap<Integer, CategoriaSedeMagazzino>();
	}

	private JComponent createAddAllCategorieSedeButton() {
		return getAddAllCategorieSedeCommand().createButton();
	}

	private JComponent createAddCategoriaSedeButton() {
		return getAddCategoriaSedeCommand().createButton();
	}

	private JComponent createAddCategoriaSedeControl() {
		categoriaSedeFormModel = FormModelHelper.createFormModel(new CategoriaSedeMagazzinoWrapper(), false);

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) Application
				.services().getService(BindingFactoryProvider.class)).getBindingFactory(categoriaSedeFormModel);
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		categorieSediValueHolder = new RefreshableValueHolder(new Closure() {
			@Override
			public Object call(Object argument) {
				return magazzinoAnagraficaBD.caricaCategorieSediMagazzino("descrizione", null);
			}
		});
		categorieSediValueHolder.refresh();
		builder.add(bf.createBoundComboBox("categoriaSedeMagazzino", categorieSediValueHolder, "descrizione"));
		builder.row();
		builder.getLayoutBuilder().cell(createAddAllCategorieSedeButton(), "align=right");
		builder.getLayoutBuilder().cell(createAddCategoriaSedeButton(), "align=right");
		builder.row();
		builder.row();
		builder.addSeparator("");

		JPanel panelAddSede = getComponentFactory().createPanel(new BorderLayout());
		panelAddSede.add(builder.getForm(), BorderLayout.CENTER);
		return panelAddSede;
	}

	@Override
	protected JComponent createControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(createAddCategoriaSedeControl(), BorderLayout.NORTH);
		panel.add(createListCategorieSediControl(), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Crea la lista di categorie sedi.
	 * 
	 * @return {@link JComponent}
	 */
	private JComponent createListCategorieSediControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());

		listCategorie = getComponentFactory().createList();
		listCategorie.setOpaque(false);
		listCategorie.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listCategorie.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (listCategorie.getSelectedIndex() == -1) {
					removeCategoriaSedeCommand.setEnabled(false);
				} else {
					removeCategoriaSedeCommand.setEnabled(true);
				}
			}
		});
		listCategorie.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 8106838870650173510L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super
						.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				label.setText(((CategoriaSedeMagazzino) value).getDescrizione());
				label.setIcon(iconSource.getIcon(CategoriaSedeMagazzino.class.getName()));
				label.setOpaque(false);
				return label;
			}
		});
		panel.add(getComponentFactory().createScrollPane(listCategorie), BorderLayout.CENTER);

		AbstractCommand[] commandBar = new AbstractCommand[] { getRemoveCategoriaSedeCommand() };
		JComponent buttonBarPanel = ((JECCommandGroup) getCommandGroup(commandBar)).createToolBar();
		buttonBarPanel.setBorder(GuiStandardUtils.createTopAndBottomBorder(3));
		panel.add(buttonBarPanel, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * Cambia lo stato readOnly del formModel.
	 * 
	 * @param readOnly
	 */
	public void disableComponents(boolean readOnly) {
		((DefaultFormModel) categoriaSedeFormModel).setReadOnly(readOnly);
	}

	private AddAllCategorieSedeCommand getAddAllCategorieSedeCommand() {
		if (addAllCategorieSedeCommand == null) {
			addAllCategorieSedeCommand = new AddAllCategorieSedeCommand();
			new PanjeaFormGuard((ValidatingFormModel) categoriaSedeFormModel, addAllCategorieSedeCommand);
		}
		return addAllCategorieSedeCommand;
	}

	private AddCategoriaSedeCommand getAddCategoriaSedeCommand() {
		if (addCategoriaSedeCommand == null) {
			addCategoriaSedeCommand = new AddCategoriaSedeCommand();
			new PanjeaFormGuard((ValidatingFormModel) categoriaSedeFormModel, addCategoriaSedeCommand);
		}
		return addCategoriaSedeCommand;
	}

	private CategoriaSedeMagazzino getCategoriaSedeMagazzinoTutte() {
		if (categoriaSedeMagazzinoTutte == null) {
			categoriaSedeMagazzinoTutte = new CategoriaSedeMagazzino();
			categoriaSedeMagazzinoTutte.setId(-1);
			String descrizioneTutteLeCategorie = RcpSupport.getMessage(TUTTE_LE_CATEGORIE_DESCRIZIONE);
			categoriaSedeMagazzinoTutte.setDescrizione(descrizioneTutteLeCategorie);
		}
		return categoriaSedeMagazzinoTutte;
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

	public RemoveCategoriaSedeCommand getRemoveCategoriaSedeCommand() {
		if (removeCategoriaSedeCommand == null) {
			removeCategoriaSedeCommand = new RemoveCategoriaSedeCommand();
			removeCategoriaSedeCommand.setEnabled(false);
		}
		return removeCategoriaSedeCommand;
	}

	public void loadData(Contratto contrattoCorrente) {
		DefaultListModel listModelCat = new DefaultListModel();
		this.contratto = contrattoCorrente;
		categorieAggiunte.clear();
		if (contratto.isTutteCategorieSedeMagazzino()) {
			CategoriaSedeMagazzino tutteCategorie = getCategoriaSedeMagazzinoTutte();
			categorieAggiunte.put(tutteCategorie.getId(), tutteCategorie);
			listModelCat.addElement(tutteCategorie);
		} else {
			if (contrattoCorrente.getCategorieSediMagazzino() != null) {
				for (CategoriaSedeMagazzino categoria : this.contratto.getCategorieSediMagazzino()) {
					categorieAggiunte.put(categoria.getId(), categoria);
					listModelCat.addElement(categoria);
				}
			}
		}
		listCategorie.setModel(listModelCat);
		disableComponents(contratto.isNew());
	}

}
