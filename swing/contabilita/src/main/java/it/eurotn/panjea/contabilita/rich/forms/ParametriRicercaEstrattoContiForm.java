package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConti;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.image.IconSource;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.SearchableUtils;

/**
 * Form per visualizzare i parametri ricerca estratto conto.
 * 
 * @author leonardo
 */
public class ParametriRicercaEstrattoContiForm extends PanjeaAbstractForm {

	private final class ContoTreeSelectionListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (treeFill) {
				return;
			}

			TreePath[] selected = treeSottoConti.getCheckBoxTreeSelectionModel().getSelectionPaths();

			List<SottoConto> listSottoContoSelezionati = new ArrayList<SottoConto>();

			if (selected != null) {
				for (TreePath treePath : selected) {
					visitSelectedNode((DefaultMutableTreeNode) treePath.getLastPathComponent(),
							listSottoContoSelezionati);
				}
			}
			getValueModel("sottoConti").setValue(listSottoContoSelezionati);
		}

		/**
		 * Scorre ricorsivamente la tree check di tipi documento aggiungendo ad una lista tutti i figli trovati.
		 * 
		 * @param node
		 *            il nodo da cui partire
		 * @param listSelected
		 *            lista di elementi per riferimento a cui aggiungo tutti gli elementi
		 */
		private void visitSelectedNode(DefaultMutableTreeNode node, List<SottoConto> listSelected) {
			if (node.getUserObject() instanceof SottoConto) {
				listSelected.add((SottoConto) node.getUserObject());
			} else {
				Enumeration<?> children = node.children();
				while (children.hasMoreElements()) {
					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
					visitSelectedNode(childNode, listSelected);
				}
			}
		}
	}

	private final class PianoDeiContiTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;

		private Color selectionColor;
		private Color nonSelectionColor;

		{
			selectionColor = UIManager.getColor("TextField.selectionBackground");
			nonSelectionColor = UIManager.getColor("Panel.background");
		}

		@Override
		public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3,
				boolean arg4, int arg5, boolean arg6) {
			JComponent component = (JComponent) super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5,
					arg6);
			component.setOpaque(true);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) arg1;

			if (node.getUserObject() instanceof Mastro) {
				Mastro mastro = (Mastro) node.getUserObject();
				setText(mastro.getCodice() + " - " + mastro.getDescrizione());
			} else if (node.getUserObject() instanceof Conto) {
				Conto conto = (Conto) node.getUserObject();
				setText(conto.getCodice() + " - " + conto.getDescrizione());
			} else if (node.getUserObject() instanceof SottoConto) {
				SottoConto sottoConto = (SottoConto) node.getUserObject();
				setText(sottoConto.getCodice() + " - " + sottoConto.getDescrizione());
			} else {
				setText("Piano dei conti");// node.getUserObject().toString());
			}
			if (node.getUserObject() != null) {
				IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
				setIcon(iconSource.getIcon(node.getUserObject().getClass().getName()));
			}

			setBackground(new Color(nonSelectionColor.getRGB()));
			if (arg2) {
				setBackground(new Color(selectionColor.getRGB()));
			}
			return component;
		}
	}

	private static final String FORM_ID = "parametriRicercaEstrattoContoForm";
	private AziendaCorrente aziendaCorrente = null;
	private boolean treeFill;
	private IContabilitaAnagraficaBD contabilitaBD = null;

	private ParametriRicercaEstrattoConti parametriRicercaEstrattoConto = null;
	private CheckBoxTree treeSottoConti = null;
	private List<SottoConto> sottoContiPresenti;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaEstrattoContiForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaEstrattoConti(), false, FORM_ID), FORM_ID);

		this.getFormModel().addPropertyChangeListener("readOnly", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (treeSottoConti != null) {
					boolean readOnly = ((Boolean) evt.getNewValue()).booleanValue();

					treeSottoConti.setCheckBoxEnabled(!readOnly);

					if (!readOnly) {
						treeSottoConti.setBackground((Color) UIManager.getDefaults().get("TextField.background"));
					} else {
						treeSottoConti.setBackground(UIManager.getDefaults().getColor("TextField.inactiveBackground"));
					}
				}
			}
		});
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:40dlu,4dlu,left:default, 20dlu,left:default,4dlu,left:50 dlu, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default, fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);
		((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);

		builder.nextRow();
		builder.setComponentAttributes("l, c");
		builder.addPropertyAndLabel("dataRegistrazione");
		// builder.addPropertyAndLabel("AData", 5);
		builder.nextRow();

		// CheckBox per lo stato
		Binding bindingStato = bf.createBoundEnumCheckBoxList("statiAreaContabile", StatoAreaContabile.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addBinding(bindingStato, 1, 12, 5, 1);

		builder.nextRow();
		builder.setComponentAttributes("f, f");
		builder.addComponent(createListaSottoConti(""), 9, 2, 4, 12);
		builder.nextRow();

		ComboBoxBinding bindingStampante = (ComboBoxBinding) bf.createBoundComboBox("stampante", getStampanti());
		builder.addLabel("stampante", 1, 8);
		builder.addBinding(bindingStampante, 3, 8);

		// Aggiunta dei vari listener
		// dataCompetenzaParametriChangeListener = new
		// DataCompetenzaParametriChangeListener();
		// dataCompetenzaParametriChangeListener.addDateValueModel(getValueModel("dataRegistrazione"));
		// addFormValueChangeListener("annoCompetenza",
		// dataCompetenzaParametriChangeListener);
		getNewFormObjectCommand().execute();
		return builder.getPanel();
	}

	/**
	 * 
	 * @param rootNodeLabel
	 *            root per il nodo del treeviwew dei documenti
	 * @return tree dei tipiDocumenti
	 */
	public JComponent createListaSottoConti(String rootNodeLabel) {

		// root su cui aggiungere i mastri
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();

		Mastro currentMastro = new Mastro();
		Conto currentConto = new Conto();

		DefaultMutableTreeNode nodeMastro = null;
		DefaultMutableTreeNode nodeConto = null;
		DefaultMutableTreeNode nodeSottoConto = null;

		sottoContiPresenti = contabilitaBD.ricercaSottoContiOrdinati();

		// scorro i sottoconti
		for (SottoConto sottoConto : sottoContiPresenti) {

			if (!currentMastro.equals(sottoConto.getConto().getMastro())) {
				if (sottoConto.getConto().getMastro().getCodice().equals(Mastro.DEFAULT_CODICE)) {
					continue;
				}
				currentMastro = sottoConto.getConto().getMastro();
				nodeMastro = new DefaultMutableTreeNode(currentMastro);
				rootNode.add(nodeMastro);
			}

			// se old conto ? uguale a current conto del sottoconto corrente
			// allora:
			if (!currentConto.equals(sottoConto.getConto())) {
				if (sottoConto.getConto().isDefault()) {
					continue;
				}
				currentConto = sottoConto.getConto();
				nodeConto = new DefaultMutableTreeNode(currentConto);
				nodeMastro.add(nodeConto);
			}

			if (!sottoConto.isDefault()) {
				nodeSottoConto = new DefaultMutableTreeNode(sottoConto);
				nodeConto.add(nodeSottoConto);
			}
		}

		Mastro mainMastro = new Mastro();
		mainMastro.setCodice("");
		mainMastro.setDescrizione("Piano dei conti");
		DefaultMutableTreeNode mainNode = new DefaultMutableTreeNode(mainMastro);
		mainNode.add(rootNode);

		TreeModel treeModel = new DefaultTreeModel(mainNode);
		treeSottoConti = new CheckBoxTree(treeModel) {

			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(700, 300);
			}
		};
		treeSottoConti.setRootVisible(false);
		treeSottoConti.setShowsRootHandles(true);
		treeSottoConti.getCheckBoxTreeSelectionModel().setSelectionPath(new TreePath(rootNode.getPath()));
		treeSottoConti.setCellRenderer(new PianoDeiContiTreeCellRenderer());
		treeSottoConti.getCheckBoxTreeSelectionModel().addTreeSelectionListener(new ContoTreeSelectionListener());
		treeSottoConti.setOpaque(false);
		SearchableUtils.installSearchable(treeSottoConti);
		JScrollPane pannello = getComponentFactory().createScrollPane(treeSottoConti);
		return pannello;
	}

	@Override
	protected Object createNewObject() {
		logger.debug("--> createNewObject per parametri estratto conto");
		parametriRicercaEstrattoConto = new ParametriRicercaEstrattoConti();
		parametriRicercaEstrattoConto.setAnnoCompetenza(this.aziendaCorrente.getAnnoContabile());
		parametriRicercaEstrattoConto.getDataRegistrazione().setDataIniziale(
				this.aziendaCorrente.getDataInizioEsercizio());
		parametriRicercaEstrattoConto.getDataRegistrazione().setDataFinale(this.aziendaCorrente.getDataFineEsercizio());
		parametriRicercaEstrattoConto.getStatiAreaContabile().add(AreaContabile.StatoAreaContabile.CONFERMATO);
		parametriRicercaEstrattoConto.getStatiAreaContabile().add(AreaContabile.StatoAreaContabile.VERIFICATO);
		parametriRicercaEstrattoConto.setSottoConti(sottoContiPresenti);
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		if (printService != null) {
			parametriRicercaEstrattoConto.setStampante(printService.getName());
		}
		return parametriRicercaEstrattoConto;
	}

	/**
	 * @return Returns the aziendaCorrente.
	 */
	public AziendaCorrente getAziendaCorrente() {
		return aziendaCorrente;
	}

	/**
	 * @return Returns the contabilitaBD.
	 */
	public IContabilitaAnagraficaBD getContabilitaBD() {
		return contabilitaBD;
	}

	/**
	 * @return lista delle stampanti installate
	 */
	private List<String> getStampanti() {
		List<String> stampanti = new ArrayList<String>();
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
		for (PrintService printService2 : printServices) {
			stampanti.add(printService2.getName());
		}
		return stampanti;
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param contabilitaBD
	 *            The contabilitaBD to set.
	 */
	public void setContabilitaBD(IContabilitaAnagraficaBD contabilitaBD) {
		this.contabilitaBD = contabilitaBD;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(true);
	}

}
