package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloCivilistica;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloGruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.rich.settings.support.PanjeaTableMemento;

import java.math.BigDecimal;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.settings.Settings;

/**
 * 
 * @author Leonardo
 * @version 1.0, 23/ott/07
 * 
 */
public class PoliticheCalcoloCivilisticheWizardPage extends AbstractPoliticheCalcoloWizardPage {

	/**
	 * 
	 * @author Leonardo
	 */
	public class PoliticheCalcoloCivilisticheTreeTableModel extends DefaultTreeTableModel {

		private static final String TABLE_MODEL_ID = "politicheCalcoloCivilisticheTreeTableModel";

		/**
		 * Costruttore.
		 * 
		 * @param node
		 *            root node
		 */
		public PoliticheCalcoloCivilisticheTreeTableModel(final TreeTableNode node) {
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
				return Boolean.class;// maggiore utilizzo
			case 5:
				return Double.class;// % maggiore utilizzo
			case 6:
				return Boolean.class;// minore utilizzo
			case 7:
				return Double.class;// % minore utilizzo
			case 8:
				return BigDecimal.class;// tot. ordinario
			case 9:
				return BigDecimal.class;// tot anticipato
			default:
				throw new UnsupportedOperationException("La colonna " + column + " non esiste");
			}
		}

		@Override
		public int getColumnCount() {
			return 10;
		}

		@Override
		public String getColumnName(final int column) {
			switch (column) {
			case 0:
				return getMessage(TABLE_MODEL_ID + ".descrizioneEntita");
			case 1:
				return getMessage(TABLE_MODEL_ID + "." + BeneAmmortizzabileLite.PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO);
			case 2:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloCivilistica.PROP_AMMORTAMENTO_ORDINARIO);
			case 3:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloCivilistica.PROP_PERC_AMMORTAMENTO_ORDINARIO);
			case 4:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloCivilistica.PROP_MAGGIORE_UTILIZZO);
			case 5:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloCivilistica.PROP_PERC_MAGGIORE_UTILIZZO);
			case 6:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloCivilistica.PROP_MINORE_UTILIZZO);
			case 7:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloCivilistica.PROP_PERC_MINORE_UTILIZZO);
			case 8:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloCivilistica.PROP_TOTALE_ORDINARIO);
			case 9:
				return getMessage(TABLE_MODEL_ID + "." + PoliticaCalcoloCivilistica.PROP_TOTALE_ANTICIPATO);
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
					return politicaCalcoloBene.getBene().getImportoSoggettoAdAmmortamento();
				} else {
					return null;
				}
			case 2:
				return politicaCalcolo.getPoliticaCalcoloCivilistica().isAmmortamentoOrdinario();
			case 3:
				return politicaCalcolo.getPoliticaCalcoloCivilistica().getPercAmmortamentoOrdinario();
			case 4:
				return politicaCalcolo.getPoliticaCalcoloCivilistica().isMaggioreUtilizzo();
			case 5:
				return politicaCalcolo.getPoliticaCalcoloCivilistica().getPercMaggioreUtilizzo();
			case 6:
				return politicaCalcolo.getPoliticaCalcoloCivilistica().isMinoreUtilizzo();
			case 7:
				return politicaCalcolo.getPoliticaCalcoloCivilistica().getPercMinoreUtilizzo();
			case 8:
				return politicaCalcolo.getPoliticaCalcoloFiscale().getTotaleOrdinario();
			case 9:
				return politicaCalcolo.getPoliticaCalcoloFiscale().getTotaleAnticipato();
			case 10:
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
				// trovato inizialmente con nessuna colonna editabile
				if ((column == 0) || (column == 1) || (column == 7) || (column == 8)) {
					return false;
				}
				return true;

			} else {
				if (politicaCalcolo instanceof PoliticaCalcoloSpecie) {
					if ((column == 0) || (column == 1) || (column == 7) || (column == 8)) {
						// trovato inizialmente con editabile (columnIndex == 1)
						return false;
					}
					return true;

				} else {
					if ((column > 1) && (column < 7)) {
						return true;
					}
					return false;
				}
			}
		}

		@Override
		public void setValueAt(Object value, Object node, int column) {
			super.setValueAt(value, node, column);

			Object object = ((DefaultMutableTreeTableNode) node).getUserObject();
			PoliticaCalcolo politicaCalcolo = (PoliticaCalcolo) object;
			politicaCalcolo.setDirty(true);
			switch (column) {
			case 2:
				politicaCalcolo.getPoliticaCalcoloCivilistica().setAmmortamentoOrdinario((Boolean) value);
				break;
			case 3:
				politicaCalcolo.getPoliticaCalcoloCivilistica().setPercAmmortamentoOrdinario((Double) value);
				break;
			case 4:
				politicaCalcolo.getPoliticaCalcoloCivilistica().setMaggioreUtilizzo((Boolean) value);
				break;
			case 5:
				politicaCalcolo.getPoliticaCalcoloCivilistica().setPercMaggioreUtilizzo((Double) value);
				break;
			case 6:
				politicaCalcolo.getPoliticaCalcoloCivilistica().setMinoreUtilizzo((Boolean) value);
				break;
			case 7:
				politicaCalcolo.getPoliticaCalcoloCivilistica().setPercMinoreUtilizzo((Double) value);
				break;
			default:
				break;
			}
		}
	}

	private static final String PAGE_ID = "politicheCalcoloCivilisticheWizardPage";

	/**
	 * Costruttore.
	 * 
	 * @param simulazione
	 *            simulazione di riferimento
	 */
	public PoliticheCalcoloCivilisticheWizardPage(final Simulazione simulazione) {
		super(PAGE_ID, simulazione);
	}

	@Override
	protected void applyValueChangedForColumn(PoliticaCalcolo politicaCalcolo, int selColumn, Object value) {
		switch (selColumn) {
		case 2:
			politicaCalcolo.getPoliticaCalcoloCivilistica().setAmmortamentoOrdinario(((Boolean) value).booleanValue());
			break;
		case 3:
			politicaCalcolo.getPoliticaCalcoloCivilistica()
					.setPercAmmortamentoOrdinario(((Double) value).doubleValue());
			break;
		case 4:
			politicaCalcolo.getPoliticaCalcoloCivilistica().setMaggioreUtilizzo(((Boolean) value).booleanValue());
			break;
		case 5:
			politicaCalcolo.getPoliticaCalcoloCivilistica().setPercMaggioreUtilizzo(((Double) value).doubleValue());
			break;
		case 6:
			politicaCalcolo.getPoliticaCalcoloCivilistica().setMinoreUtilizzo(((Boolean) value).booleanValue());
			break;
		case 7:
			politicaCalcolo.getPoliticaCalcoloCivilistica().setPercMinoreUtilizzo(((Double) value).doubleValue());
			break;
		default:
			break;
		}
	}

	@Override
	protected void configureTreeTable(JXTreeTable treeTable) {
		treeTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		treeTable.getColumnModel().getColumn(1).setPreferredWidth(120);
		treeTable.getColumnModel().getColumn(8).setPreferredWidth(120);
		treeTable.getColumnModel().getColumn(9).setPreferredWidth(120);
	}

	@Override
	public TreeTableModel createTreeTableModel(DefaultMutableTreeTableNode rootNode) {
		return new PoliticheCalcoloCivilisticheTreeTableModel(rootNode);
	}

	@Override
	public void restoreState(Settings settings) {
		PanjeaTableMemento tableMemento = new PanjeaTableMemento(getSimulazioneTreeTable(),
				PoliticheCalcoloCivilisticheWizardPage.PAGE_ID + ".treeTable");
		tableMemento.restoreState(settings);
	}

	@Override
	public void saveState(Settings settings) {
		PanjeaTableMemento tableMemento = new PanjeaTableMemento(getSimulazioneTreeTable(),
				PoliticheCalcoloCivilisticheWizardPage.PAGE_ID + ".treeTable");
		tableMemento.saveState(settings);
	}

}
