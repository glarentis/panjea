package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite;

import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.RigaAnticipo;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.component.PartitaAreaTesoreriaComponentBuilder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.List;

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

import com.jidesoft.pane.CollapsiblePane;

public class AnticipiComponentBuilder extends AbstractControlFactory {

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

	private final AreaAnticipo areaAnticipo;

	private JPanel rootPanel;

	private final PartitaAreaTesoreriaComponentBuilder componentBuilder = new PartitaAreaTesoreriaComponentBuilder();

	private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

	/**
	 * Costruttore.
	 * 
	 * @param areaTesoreria
	 *            area tesoreria da gestire
	 */
	public AnticipiComponentBuilder(final AreaTesoreria areaTesoreria) {
		super();
		this.areaAnticipo = (AreaAnticipo) areaTesoreria;
	}

	/**
	 * Crea l'header panel dei anticipi.
	 * 
	 * @param numeroAnticipi
	 *            numero di anticipi
	 * @param paramAreaTesoreria
	 *            area tesoreria
	 * @return pannello creato
	 */
	private JComponent createAnticipiHeaderPanel(int numeroAnticipi, AreaTesoreria paramAreaTesoreria) {
		JPanel headerPanel = getComponentFactory().createPanel(new BorderLayout());

		// aggiungo un pannello finto perchè il pannello header degli effetti contiene anche i pulsanti di generazione
		// distinta e flusso e quindi la lista dei pagamenti rispetto a quella degli effetti risulterebbe sfalsata
		JPanel emptyPanel = getComponentFactory().createPanel();
		emptyPanel.setMinimumSize(new Dimension(100, 30));
		emptyPanel.setPreferredSize(new Dimension(100, 30));
		headerPanel.add(emptyPanel, BorderLayout.NORTH);

		headerPanel.add(new ExpandCommand(true, rootPanel).createButton(), BorderLayout.LINE_START);
		headerPanel.add(new JLabel("Numero anticipi: " + numeroAnticipi), BorderLayout.CENTER);
		headerPanel.add(
				new JLabel("Totale: "
						+ decimalFormat.format(areaAnticipo.getDocumento().getTotale().getImportoInValutaAzienda())),
				BorderLayout.EAST);
		GuiStandardUtils.attachBorder(headerPanel);
		return headerPanel;
	}

	@Override
	protected JComponent createControl() {
		rootPanel = getComponentFactory().createPanel(new VerticalLayout(5));

		List<RigaAnticipo> anticipi = areaAnticipo.getAnticipi();

		// aggiungo il pannello riassuntivo con totale pagamenti e importo documento
		rootPanel.add(createAnticipiHeaderPanel(anticipi.size(), areaAnticipo));

		for (RigaAnticipo anticipo : anticipi) {
			rootPanel.add(componentBuilder.getPartitaAreaTesoreriaComponent(anticipo));
		}

		return rootPanel;
	}

}
