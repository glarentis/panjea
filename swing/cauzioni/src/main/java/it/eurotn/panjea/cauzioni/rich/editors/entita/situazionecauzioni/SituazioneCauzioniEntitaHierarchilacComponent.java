package it.eurotn.panjea.cauzioni.rich.editors.entita.situazionecauzioni;

import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.components.ShadowBorder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.factories.Borders;
import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.JideLabel;

public class SituazioneCauzioniEntitaHierarchilacComponent implements HierarchicalTableComponentFactory {

	private class TableChildComponent extends JPanel {

		private class OpenAreaMagazzinoEditor extends ApplicationWindowAwareCommand {

			private static final String COMMAND_ID = "openAreaMagazzinoCommand";

			private IMagazzinoDocumentoBD magazzinoDocumentoBD;

			/**
			 * Costruttore.
			 */
			public OpenAreaMagazzinoEditor() {
				super(COMMAND_ID);
				RcpSupport.configure(this);
				this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
			}

			@Override
			protected void doExecuteCommand() {
				MovimentazioneCauzioneDTO movimentazioneCauzioneDTO = tableWidget.getSelectedObject();
				if (movimentazioneCauzioneDTO == null) {
					return;
				}
				AreaMagazzino areaMagazzino = new AreaMagazzino();
				areaMagazzino.setId(movimentazioneCauzioneDTO.getIdAreaMagazzino());

				AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD
						.caricaAreaMagazzinoFullDTO(areaMagazzino);
				LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}

		private static final long serialVersionUID = -85380947682090389L;

		private JideTableWidget<MovimentazioneCauzioneDTO> tableWidget;

		private OpenAreaMagazzinoEditor openAreaMagazzinoEditor;

		/**
		 * Costruttore.
		 * 
		 * @param righeMovimentazione
		 *            righe movimentazione articolo
		 * 
		 */
		public TableChildComponent(final List<MovimentazioneCauzioneDTO> righeMovimentazione) {
			super(new BorderLayout());
			setBorder(BorderFactory.createCompoundBorder(Borders.DIALOG_BORDER, new ShadowBorder()));

			tableWidget = new JideTableWidget<MovimentazioneCauzioneDTO>("movimentazioneCauzioniTable",
					new MovimentazioneCauzioniTableModel("movimentazioneEntitaCauzioniTableModel"));
			tableWidget.setRows(righeMovimentazione);
			tableWidget.getComponent().setPreferredSize(new Dimension(200, 200));
			openAreaMagazzinoEditor = new OpenAreaMagazzinoEditor();
			tableWidget.setPropertyCommandExecutor(openAreaMagazzinoEditor);

			JideLabel titleLabel = new JideLabel("<html><b>MOVIMENTAZIONE ARTICOLO</b></html>");
			titleLabel.setOrientation(SwingConstants.VERTICAL);
			titleLabel.setClockwise(false);
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

			add(tableWidget.getComponent(), BorderLayout.CENTER);
			add(titleLabel, BorderLayout.WEST);
		}

		/**
		 * @return the tableWidget
		 */
		public JideTableWidget<MovimentazioneCauzioneDTO> getTableWidget() {
			return tableWidget;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Component createChildComponent(HierarchicalTable arg0, Object values, int arg2) {

		List<MovimentazioneCauzioneDTO> righe = (List<MovimentazioneCauzioneDTO>) values;

		JPanel rootPanel = new TableChildComponent(righe);

		return rootPanel;
	}

	@Override
	public void destroyChildComponent(HierarchicalTable arg0, Component component, int arg2) {

		TableChildComponent tableChildComponent = (TableChildComponent) component;

		tableChildComponent.removeAll();
		tableChildComponent.getTableWidget().dispose();
		tableChildComponent = null;
	}
}
