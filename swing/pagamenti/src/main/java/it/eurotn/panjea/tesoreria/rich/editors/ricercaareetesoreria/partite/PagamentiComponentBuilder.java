package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.rich.converter.ImportoConverter;
import it.eurotn.panjea.pagamenti.service.interfaces.FlussoCBIDownload;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.GeneraBonificoFornitoreCommand;
import it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.GeneraFlussoCommand;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.component.PartitaAreaTesoreriaComponentBuilder;
import it.eurotn.rich.command.JECCommandGroup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.pane.CollapsiblePane;

public class PagamentiComponentBuilder extends AbstractControlFactory {

	private class ExpandCommand extends ActionCommand {

		private static final String ESPANDI_COMMAND = ".expandCommand";
		private static final String EXPAND_STATE = "expand";
		private static final String COLLAPSE_STATE = "collapse";
		private final CommandFaceDescriptor expandDescriptor;
		private final CommandFaceDescriptor collapseDescriptor;

		private boolean collapse;
		private final JPanel panel;

		/**
		 * Costruttore.
		 * 
		 * @param collapse
		 *            stato inizale del comando
		 * @param panel
		 *            pannello da gestire
		 */
		public ExpandCommand(final boolean collapse, final JPanel panel) {
			super(ESPANDI_COMMAND);
			RcpSupport.configure(this);
			this.collapse = collapse;
			this.panel = panel;

			Icon toExpandIcon = RcpSupport.getIcon(EXPAND_STATE + ".icon");
			Icon toCollapseIcon = RcpSupport.getIcon(COLLAPSE_STATE + ".icon");
			collapseDescriptor = new CommandFaceDescriptor(null, toExpandIcon, null);
			expandDescriptor = new CommandFaceDescriptor(null, toCollapseIcon, null);
			if (collapse) {
				setFaceDescriptor(collapseDescriptor);
			} else {
				setFaceDescriptor(expandDescriptor);

			}
		}

		@Override
		protected void doExecuteCommand() {
			collapse = !collapse;
			for (Component component : panel.getComponents()) {
				if (component instanceof CollapsiblePane) {
					((CollapsiblePane) component).collapse(collapse);
				}
			}
			if (getFaceDescriptor().equals(collapseDescriptor)) {
				setFaceDescriptor(expandDescriptor);
			} else {
				setFaceDescriptor(collapseDescriptor);
			}
		}
	}

	private final AreaPagamenti areaPagamenti;
	private final ITesoreriaBD tesoreriaBD;
	private GeneraBonificoFornitoreCommand generaBonificoFornitoreCommand;
	private GeneraFlussoCommand generaFlussoCommand;

	private final FlussoCBIDownload flussoCBIDownload;
	private final PartitaAreaTesoreriaComponentBuilder componentBuilder = new PartitaAreaTesoreriaComponentBuilder();

	private JPanel rootPanel;

	/**
	 * Costruttore.
	 * 
	 * @param areaTesoreria
	 *            area tesoreria da gestire
	 */
	public PagamentiComponentBuilder(final AreaTesoreria areaTesoreria) {
		super();
		this.areaPagamenti = (AreaPagamenti) areaTesoreria;
		this.tesoreriaBD = RcpSupport.getBean("tesoreriaBD");
		this.flussoCBIDownload = RcpSupport.getBean("flussoCBIDownload");
	}

	@Override
	protected JComponent createControl() {

		rootPanel = getComponentFactory().createPanel(new VerticalLayout(5));

		Set<Pagamento> pagamenti = areaPagamenti.getPagamenti();

		// aggiungo il pannello riassuntivo con totale pagamenti e importo documento
		rootPanel.add(createPagamentoHeaderPanel(pagamenti.size(), areaPagamenti));

		for (Pagamento pagamento : pagamenti) {
			rootPanel.add(componentBuilder.getPartitaAreaTesoreriaComponent(pagamento));
		}

		return rootPanel;
	}

	/**
	 * Crea l'header panel dei pagamenti.
	 * 
	 * @param numeroPagamenti
	 *            numero di pagamenti
	 * @param paramAreaTesoreria
	 *            area tesoreria
	 * @return pannello creato
	 */
	private JComponent createPagamentoHeaderPanel(int numeroPagamenti, AreaTesoreria paramAreaTesoreria) {
		JPanel headerPanel = getComponentFactory().createPanel(new BorderLayout());

		JPanel commandPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		commandPanel.setMinimumSize(new Dimension(100, 30));
		commandPanel.setPreferredSize(new Dimension(100, 30));

		JECCommandGroup commandGroup = new JECCommandGroup();
		commandGroup.add(getGeneraBonificoFornitoreCommand());
		commandGroup.add(getGeneraFlussoCommand());
		commandPanel.add(commandGroup.createToolBar());

		boolean bonificoFornitore = isBonificoFornitoreDaPresentare(paramAreaTesoreria);

		getGeneraBonificoFornitoreCommand().setAreaPagamenti((AreaPagamenti) paramAreaTesoreria);
		getGeneraFlussoCommand().setAreaChiusure((AreaChiusure) paramAreaTesoreria);

		getGeneraBonificoFornitoreCommand().setVisible(bonificoFornitore);
		getGeneraFlussoCommand().setVisible(bonificoFornitore);

		// aggiungo un pannello finto perchè il pannello header degli effetti contiene anche i pulsanti di generazione
		// distinta e flusso e quindi la lista dei pagamenti rispetto a quella degli effetti risulterebbe sfalsata
		JPanel emptyPanel = getComponentFactory().createPanel();
		emptyPanel.setMinimumSize(new Dimension(100, 30));
		emptyPanel.setPreferredSize(new Dimension(100, 30));
		headerPanel.add(emptyPanel, BorderLayout.NORTH);

		headerPanel.add(commandPanel, BorderLayout.NORTH);

		headerPanel.add(new ExpandCommand(true, rootPanel).createButton(), BorderLayout.LINE_START);
		headerPanel.add(new JLabel("Numero rate: " + numeroPagamenti), BorderLayout.CENTER);
		JLabel labelTotale = new JLabel("Totale: "
				+ ObjectConverterManager.toString(areaPagamenti.getDocumento().getTotale()));
		labelTotale.setToolTipText(ObjectConverterManager.toString(areaPagamenti.getDocumento().getTotale(),
				Importo.class, ImportoConverter.HTML_CONVERTER_CONTEXT));
		headerPanel.add(labelTotale, BorderLayout.EAST);
		GuiStandardUtils.attachBorder(headerPanel);
		return headerPanel;
	}

	/**
	 * @return the contabilizzaPagamentoCommand
	 */
	public GeneraBonificoFornitoreCommand getGeneraBonificoFornitoreCommand() {
		if (generaBonificoFornitoreCommand == null) {
			generaBonificoFornitoreCommand = new GeneraBonificoFornitoreCommand(tesoreriaBD);
			generaBonificoFornitoreCommand.setVisible(false);
		}
		return generaBonificoFornitoreCommand;
	}

	/**
	 * @return the generaFlussoCommand
	 */
	public GeneraFlussoCommand getGeneraFlussoCommand() {
		if (generaFlussoCommand == null) {
			generaFlussoCommand = new GeneraFlussoCommand(flussoCBIDownload, tesoreriaBD);
			generaFlussoCommand.setVisible(false);
		}
		return generaFlussoCommand;
	}

	/**
	 * Verifica se il tipo area partita è GESTIONE DISTINTA, PASSIVA, con tipo pagamento chiusura BONIFICO.
	 * 
	 * @param paramAreaTesoreria
	 *            l'area tesoreria
	 * @return true o false
	 */
	private boolean isBonificoFornitoreDaPresentare(AreaTesoreria paramAreaTesoreria) {
		boolean isBonificoFornitore = false;

		AreaPagamenti ap = (AreaPagamenti) paramAreaTesoreria;

		if (ap.isPresentazioneBonificoFornitore()) {
			List<AreaTesoreria> caricaAreeCollegate = tesoreriaBD.caricaAreeCollegate(paramAreaTesoreria);
			if (caricaAreeCollegate.size() == 0) {
				isBonificoFornitore = true;
			}
		}
		return isBonificoFornitore;
	}
}
