package it.eurotn.panjea.ordini.rich.editors.produzione;

import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.DefaultExpandableRow;
import com.jidesoft.grid.Row;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeTable;

public class ModificaDocumentiEvasioneDialog extends ConfirmationDialog {

	private class InserisciComponenteButton extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public InserisciComponenteButton() {
			super("inserisciComponenteButton");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			Row rigaSelezionata = righeTable.getRowAt(righeTable.getActualRowAt(righeTable.getSelectedRow()));
			// bloccare o forse se non presente aggiungerlo al root ?
			if (rigaSelezionata == null || !(rigaSelezionata instanceof RigaDistintaEvasaRow)) {
				return;
			}

			RigaDistintaEvasaRow rigaDistinta = (RigaDistintaEvasaRow) rigaSelezionata;

			if (rigaDistinta.getRigaArticolo() instanceof RigaArticoloDistinta) {

				RicercaAvanzataArticoliCommand ricercaCommand = new RicercaAvanzataArticoliCommand();
				ricercaCommand.execute();
				if (ricercaCommand.getArticoliSelezionati().isEmpty()) {
					return;
				}
				List<ArticoloRicerca> articoliDaInserire = ricercaCommand.getArticoliSelezionati();
				RigaArticoloComponente rigaComponente = magazzinoDocumentoBD.aggiungiRigaComponente(articoliDaInserire
						.get(0).getId(), 0, rigaDistinta.getRigaArticolo());
				rigaDistinta.addRigaComponente(rigaComponente);
			}
		}

	}

	private ParametriRicercaAreaMagazzino parametri;

	private IOrdiniDocumentoBD ordiniDocumentoBD;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private TreeTable righeTable;

	/**
	 * Costruttore.
	 */
	public ModificaDocumentiEvasioneDialog() {
		super("Visualizzazione/Modifica dei documenti generati", "_");
		setPreferredSize(new Dimension(800, 600));
		magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
		ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		righeTable = new TreeTable();
		righeTable.expandAll();
		righeTable.setRowHeight(25);
		righeTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		rootPanel.add(new JScrollPane(righeTable), BorderLayout.CENTER);
		rootPanel.add(new InserisciComponenteButton().createButton(), BorderLayout.SOUTH);
		return rootPanel;
	}

	@Override
	protected String getCancelCommandId() {
		return "cancelCommand";
	}

	@Override
	protected String getFinishCommandId() {
		return "okCommand";
	}

	/**
	 * Recupera dal model le righe da salvare raggruppandole per area.
	 * 
	 * @param model
	 *            model
	 * @return la mappa di righe componenti modificate per areaMagazzino
	 */
	private Map<AreaMagazzino, List<RigaArticolo>> getRigheDaSalvare(RigheEvaseTableModel model) {
		Map<AreaMagazzino, List<RigaArticolo>> righePerArea = new HashMap<AreaMagazzino, List<RigaArticolo>>();

		for (DefaultExpandableRow row : model.getRows()) {
			if (row instanceof RigaComponenteRow && ((RigaComponenteRow) row).isQtaChanged()) {
				RigaArticolo rigaComponente = ((RigaComponenteRow) row).getRigaComponente();
				FormuleRigaArticoloCalculator calculator = new FormuleRigaArticoloCalculator();
				try {
					RigaArticolo rigaComponenteCalcolata = (RigaArticolo) calculator.calcola(rigaComponente);
					rigaComponente.setQtaMagazzino(rigaComponenteCalcolata.getQtaMagazzino());
				} catch (FormuleTipoAttributoException e) {
					logger.error("Errore nel calcolare la riga componente", e);
					throw new RuntimeException(e);
				}

				AreaMagazzino areaMagazzino = rigaComponente.getAreaMagazzino();

				List<RigaArticolo> list = righePerArea.get(areaMagazzino);
				if (list != null) {
					list.add(rigaComponente);
				} else {
					List<RigaArticolo> righeDaSalvare = new ArrayList<>();
					righeDaSalvare.add(rigaComponente);
					righePerArea.put(areaMagazzino, righeDaSalvare);
				}
			}
		}
		return righePerArea;
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();

		RigheEvaseTableModel model = new RigheEvaseTableModel();
		if (parametri != null) {
			List<RigaArticoloDistinta> righeCreate = ordiniDocumentoBD.caricaRigheDistintaMagazzino(parametri);
			// le righe sono ordinate per area. Creo un nodo parent per ogni area
			int idAreaCorrente = -1;
			AreaRigaEvasaRow areaRow = null;
			for (RigaArticoloDistinta rigaDistinta : righeCreate) {

				if (idAreaCorrente == -1 || idAreaCorrente != rigaDistinta.getAreaMagazzino().getId()) {
					areaRow = new AreaRigaEvasaRow(rigaDistinta.getAreaMagazzino());
					model.addRow(areaRow);
					idAreaCorrente = rigaDistinta.getAreaMagazzino().getId();
				}

				areaRow.addChild(new RigaDistintaEvasaRow(rigaDistinta));
			}
		}
		righeTable.setModel(model);
		righeTable.expandAll();

		parametri = null;

		TableUtils.autoResizeAllColumns(righeTable, null, true, true);
		righeTable.requestFocusInWindow();
	}

	@Override
	protected void onConfirm() {
		if (righeTable.getCellEditor() != null) {
			righeTable.getCellEditor().stopCellEditing();
		}
		RigheEvaseTableModel model = (RigheEvaseTableModel) TableModelWrapperUtils.getActualTableModel(
				righeTable.getModel(), RigheEvaseTableModel.class);
		// mi servono le righe per area per sincronizzare il datawarehouse magazzino per le aree magazzino delle righe
		// modificate
		Map<AreaMagazzino, List<RigaArticolo>> righeDaSalvarePerArea = getRigheDaSalvare(model);
		salvaRigheMagazzinoNoCheck(righeDaSalvarePerArea);
	}

	/**
	 * Salva le righe magazzino, raggruppo per le aree perchè al salvataggio devo attivare l'interceptor del datawarehouse
	 * che risincronizzi le righe magazzino, altrimenti il DW non viene aggiornato.
	 * 
	 * @param righePerArea
	 *            righePerArea
	 */
	private void salvaRigheMagazzinoNoCheck(Map<AreaMagazzino, List<RigaArticolo>> righePerArea) {
		if (!righePerArea.isEmpty()) {
			ordiniDocumentoBD.salvaRigheMagazzinoNoCheck(righePerArea);
		}
	}

	/**
	 * @param parametri
	 *            the parametri to set
	 */
	public void setParametri(ParametriRicercaAreaMagazzino parametri) {
		this.parametri = parametri;
	}
}
