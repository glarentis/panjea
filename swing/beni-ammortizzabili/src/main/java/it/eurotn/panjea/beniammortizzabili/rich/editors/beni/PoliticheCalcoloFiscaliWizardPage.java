package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloGruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.rich.settings.support.PanjeaTableMemento;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 *
 * @author Leonardo
 * @version 1.0, 20/ott/07
 */
public class PoliticheCalcoloFiscaliWizardPage extends AbstractPoliticheCalcoloWizardPage {

	private class DoubleClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2
					&& getSimulazioneTreeTable().getTreeSelectionModel().getLeadSelectionPath() != null) {
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) getSimulazioneTreeTable()
						.getTreeSelectionModel().getLeadSelectionPath().getLastPathComponent();
				PoliticaCalcolo politicaCalcolo = (PoliticaCalcolo) node.getUserObject();

				// controllo prima se esiste un'area contabile sulla politica
				AreaContabileLite areaContabile = null;
				if (politicaCalcolo.getAreaContabile() != null) {
					areaContabile = politicaCalcolo.getAreaContabile();
				} else if (politicaCalcolo instanceof PoliticaCalcoloGruppo
						&& getSimulazione().getAreaContabile() != null) {
					// in questo caso è stata generata un'area contabile unica quindi la prendo dalla simulazione
					areaContabile = getSimulazione().getAreaContabile();
				}

				if (areaContabile != null) {
					LifecycleApplicationEvent event = new OpenEditorEvent(areaContabile);
					Application.instance().getApplicationContext().publishEvent(event);
				}
			}
		}
	}

	private class MyTreeDirtyCellRendererFiscale extends MyTreeDirtyCellRenderer {

		private static final long serialVersionUID = 599865280000093499L;

		private Icon iconaAC;

		/**
		 * Costruttore.
		 */
		public MyTreeDirtyCellRendererFiscale() {
			iconaAC = RcpSupport.getIcon(AreaContabile.class.getName());
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;

			JPanel panel = new JPanel(new BorderLayout());
			panel.add(c, BorderLayout.CENTER);

			PoliticaCalcolo politicaCalcolo = (PoliticaCalcolo) node.getUserObject();
			if (politicaCalcolo != null) {
				// setto l'icona dell'area contabile se la politica calcolo ne ha una legata
				if (politicaCalcolo.getAreaContabile() != null
						|| (politicaCalcolo instanceof PoliticaCalcoloGruppo && politicaCalcolo.getSimulazione()
								.getAreaContabile() != null)) {
					panel.add(new JLabel(iconaAC), BorderLayout.WEST);
				}
			}

			return panel;
		}
	}

	public class PoliticheCalcoloFiscaliTreeTableModel extends DefaultTreeTableModel {

		/**
		 * Costruttore.
		 *
		 * @param node
		 *            root node
		 */
		public PoliticheCalcoloFiscaliTreeTableModel(final TreeTableNode node) {
			super(node);
		}

		@Override
		public Class<?> getColumnClass(int column) {
			switch (column) {
			case 0:
				return TreeTableModel.class;// tree
			case 1:
				return BigDecimal.class; // impoprto soggetto ad ammortamento
			case 2:
				return Boolean.class;// amm. ordinario
			case 3:
				return Double.class;// % amm. ordinario
			case 4:
				return Boolean.class;// amm. anticipato
			case 5:
				return Double.class;// % amm. anticipato
			case 6:
				return Boolean.class;// amm. accelerato
			case 7:
				return Double.class;// % amm. accelerato
			case 8:
				return Boolean.class;// amm. ridotto
			case 9:
				return Double.class;// % amm. ridotto
			case 10:
				return BigDecimal.class;// tot. ordinario
			case 11:
				return BigDecimal.class;// tot anticipato
			default:
				throw new UnsupportedOperationException("La colonna " + column + " non esiste");
			}
		}

		@Override
		public int getColumnCount() {
			return 12;
		}

		@Override
		public String getColumnName(final int column) {
			switch (column) {
			case 0:
				return getMessage(TABLE_MODEL_ID + ".descrizioneEntita");
			case 1:
				return getMessage(TABLE_MODEL_ID + "." + BeneAmmortizzabileLite.PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO);
			case 2:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_AMMORTAMENTO_ORDINARIO);
			case 3:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_PERC_AMMORTAMENTO_ORDINARIO);
			case 4:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_AMMORTAMENTO_ANTICIPATO);
			case 5:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_PERC_AMMORTAMENTO_ANTICIPATO);
			case 6:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_AMMORTAMENTO_ACCELERATO);
			case 7:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_PERC_AMMORTAMENTO_ACCELERATO);
			case 8:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_AMMORTAMENTO_RIDOTTO);
			case 9:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_PERC_AMMORTAMENTO_RIDOTTO);
			case 10:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_TOTALE_ORDINARIO);
			case 11:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloFiscale.PROP_TOTALE_ANTICIPATO);
			default:
				return "errore";
			}
		}

		@Override
		public Object getValueAt(Object node, int column) {

			Object object = ((DefaultMutableTreeTableNode) node).getUserObject();
			if (object == null) {
				return "";
			}
			PoliticaCalcolo politicaCalcolo = (PoliticaCalcolo) object;

			switch (column) {
			case 0:
				return politicaCalcolo.getDescrizioneEntitaPoliticaCalcolo();
			case 1:
				if (politicaCalcolo instanceof PoliticaCalcoloBene) {
					PoliticaCalcoloBene politicaCalcoloBene = (PoliticaCalcoloBene) politicaCalcolo;
					return politicaCalcoloBene.getImportoSoggettoAmmortamento();
				} else {
					return null;
				}
			case 2:
				return politicaCalcolo.getPoliticaCalcoloFiscale().isAmmortamentoOrdinario();
			case 3:
				return politicaCalcolo.getPoliticaCalcoloFiscale().getPercAmmortamentoOrdinario();
			case 4:
				return politicaCalcolo.getPoliticaCalcoloFiscale().isAmmortamentoAnticipato();
			case 5:
				return politicaCalcolo.getPoliticaCalcoloFiscale().getPercAmmortamentoAnticipato();
			case 6:
				return politicaCalcolo.getPoliticaCalcoloFiscale().isAmmortamentoAccelerato();
			case 7:
				return politicaCalcolo.getPoliticaCalcoloFiscale().getPercAmmortamentoAccelerato();
			case 8:
				return politicaCalcolo.getPoliticaCalcoloFiscale().isAmmortamentoRidotto();
			case 9:
				return politicaCalcolo.getPoliticaCalcoloFiscale().getPercAmmortamentoRidotto();
			case 10:
				return politicaCalcolo.getPoliticaCalcoloFiscale().getTotaleOrdinario();
			case 11:
				return politicaCalcolo.getPoliticaCalcoloFiscale().getTotaleAnticipato();
			case 12:
				return politicaCalcolo.isDirty();
			default:
				return "errore";
			}
		}

		@Override
		public boolean isCellEditable(Object node, int column) {
			Object object = ((DefaultMutableTreeTableNode) node).getUserObject();
			PoliticaCalcolo politicaCalcolo = (PoliticaCalcolo) object;
			if (politicaCalcolo instanceof PoliticaCalcoloGruppo) {
				// in origine era if ((arg1 == 0) || (arg1 == 1) || (arg1 == 2) || (arg1 == 4) || (arg1 == 9) || (arg1
				// == 10)) {return false;}else{return true;}
				if ((column > 1) && (column < 10)) {
					return true;
				}
				return false;
			} else {
				if (politicaCalcolo instanceof PoliticaCalcoloSpecie) {
					// in origine era if ((arg1 == 0) || (arg1 == 2) || (arg1 == 4) || (arg1 == 9) || (arg1 == 10))
					// {return false;}else{return true};
					if ((column > 1) && (column < 10)) {
						return true;
					}
					return false;
				} else {
					if ((column > 1) && (column < 10)) {
						return true;
					}
					return false;
				}
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.jdesktop.swingx.treetable.DefaultTreeTableModel#setValueAt(java.lang.Object, java.lang.Object, int)
		 */
		@Override
		public void setValueAt(Object value, Object node, int column) {
			super.setValueAt(value, node, column);

			Object object = ((DefaultMutableTreeTableNode) node).getUserObject();
			PoliticaCalcolo politicaCalcolo = (PoliticaCalcolo) object;
			politicaCalcolo.setDirty(true);
			switch (column) {
			case 2:
				politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario((Boolean) value);
				break;
			case 3:
				politicaCalcolo.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario((Double) value);
				break;
			case 4:
				politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato((Boolean) value);
				break;
			case 5:
				politicaCalcolo.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato((Double) value);
				break;
			case 6:
				politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoAccelerato((Boolean) value);
				if ((Boolean) value) {
					politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoRidotto(!(Boolean) value);
				}
				break;
			case 7:
				politicaCalcolo.getPoliticaCalcoloFiscale().setPercAmmortamentoAccelerato((Double) value);
				break;
			case 8:
				politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoRidotto((Boolean) value);
				if ((Boolean) value) {
					politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoAccelerato(!(Boolean) value);
				}
				break;
			case 9:
				politicaCalcolo.getPoliticaCalcoloFiscale().setPercAmmortamentoRidotto((Double) value);
				break;
			case 12:
				politicaCalcolo.setDirty((Boolean) value);
				break;
			default:
				break;
			}
		}

	}

	private static final String PAGE_ID = "politicheCalcoloFiscaliWizardPage";

	private static final String TABLE_MODEL_ID = "politicheCalcoloFiscaliTreeTableModel";

	/**
	 * Costruttore.
	 *
	 * @param simulazione
	 *            simulazione di riferimento
	 */
	public PoliticheCalcoloFiscaliWizardPage(final Simulazione simulazione) {
		super(PAGE_ID, simulazione);
	}

	@Override
	protected void applyValueChangedForColumn(PoliticaCalcolo politicaCalcolo, int selColumn, Object value) {
		switch (selColumn) {
		case 2:
			politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(((Boolean) value).booleanValue());
			break;
		case 3:
			politicaCalcolo.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(((Double) value).doubleValue());
			break;
		case 4:
			politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(((Boolean) value).booleanValue());
			break;
		case 5:
			politicaCalcolo.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(((Double) value).doubleValue());
			break;
		case 6:
			if (((Boolean) value).booleanValue()) {
				politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoRidotto(!((Boolean) value).booleanValue());
			}
			politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoAccelerato(((Boolean) value).booleanValue());
			break;
		case 7:
			politicaCalcolo.getPoliticaCalcoloFiscale().setPercAmmortamentoAccelerato(((Double) value).doubleValue());
			break;
		case 8:
			if (((Boolean) value).booleanValue()) {
				politicaCalcolo.getPoliticaCalcoloFiscale()
				.setAmmortamentoAccelerato(!((Boolean) value).booleanValue());
			}
			politicaCalcolo.getPoliticaCalcoloFiscale().setAmmortamentoRidotto(((Boolean) value).booleanValue());
			break;
		case 9:
			politicaCalcolo.getPoliticaCalcoloFiscale().setPercAmmortamentoRidotto(((Double) value).doubleValue());
			break;
		default:
			break;
		}
	}

	@Override
	protected void configureTreeTable(JXTreeTable treeTable) {
		treeTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		treeTable.getColumnModel().getColumn(1).setPreferredWidth(120);
		treeTable.getColumnModel().getColumn(10).setPreferredWidth(120);
		treeTable.getColumnModel().getColumn(11).setPreferredWidth(120);

		treeTable.addMouseListener(new DoubleClickListener());
		treeTable.setTreeCellRenderer(new MyTreeDirtyCellRendererFiscale());
	}

	@Override
	public TreeTableModel createTreeTableModel(DefaultMutableTreeTableNode rootNode) {
		return new PoliticheCalcoloFiscaliTreeTableModel(rootNode);
	}

	@Override
	public void restoreState(Settings settings) {
		PanjeaTableMemento tableMemento = new PanjeaTableMemento(getSimulazioneTreeTable(),
				PoliticheCalcoloFiscaliWizardPage.PAGE_ID + ".treeTable");
		tableMemento.restoreState(settings);
	}

	@Override
	public void saveState(Settings settings) {
		PanjeaTableMemento tableMemento = new PanjeaTableMemento(getSimulazioneTreeTable(),
				PoliticheCalcoloFiscaliWizardPage.PAGE_ID + ".treeTable");
		tableMemento.saveState(settings);
	}

}
