/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.CloseAction;

/**
 * Genera le righe contabili basandosi sulla struttura contabile.
 * 
 * @author Leonardo
 */
public class RigheContabiliBuilder {

	private static Logger logger = Logger.getLogger(RigheContabiliBuilder.class);
	private IContabilitaBD contabilitaBD = null;
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;
	private AreaContabileFullDTO areaContabileFullDTO;
	private RigheContabiliTableModel righeContabiliTableModel;

	/**
	 * @param contabilitaAnagraficaBD
	 *            bd della contabilità
	 * @param contabilitaBD
	 *            bd della contabilità
	 * @param areaContabileFullDTO
	 *            area contabile da gestire
	 * @param righeContabiliTableModel
	 *            table model delle righe
	 */
	public RigheContabiliBuilder(final IContabilitaAnagraficaBD contabilitaAnagraficaBD,
			final IContabilitaBD contabilitaBD, final AreaContabileFullDTO areaContabileFullDTO,
			final RigheContabiliTableModel righeContabiliTableModel) {
		super();
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
		this.contabilitaBD = contabilitaBD;
		this.areaContabileFullDTO = areaContabileFullDTO;
		this.righeContabiliTableModel = righeContabiliTableModel;
	}

	/**
	 * Verifica la condizione per la generazione delle righe contabili.
	 * 
	 * @return true se posso generarle o false altrimenti
	 */
	private boolean checkGeneraRigheContabili() {
		logger.debug("--> Enter checkGeneraRigheContabili");
		// controllo che non ci siano delle righe contabili
		// verifico che i controlli della tablepage siano creati altrimenti viene generata
		// una NPE quando cerco la tabella per inserire i dati

		if (areaContabileFullDTO.getAreaContabile().isNew()
				|| (righeContabiliTableModel != null && !righeContabiliTableModel.getObjects().isEmpty())) {
			return false;
		}

		if (!areaContabileFullDTO.isAreaIvaEnable()
				|| (areaContabileFullDTO.isAreaIvaEnable() && areaContabileFullDTO.isAreaIvaValid())) {
			logger.debug("--> Exit checkGeneraRigheContabili true");
			return true;
		}
		logger.debug("--> Exit checkGeneraRigheContabili false");
		return false;
	}

	/**
	 * genera le righe contabili.
	 * 
	 * @return lista delle righe generate. lista vuota se non serve generarle.
	 */
	public List<RigaContabile> generaRigheContabili() {
		logger.debug("--> Enter generaRigheContabili");
		if (!checkGeneraRigheContabili()) {
			return Collections.emptyList();
		}

		List<ControPartita> contropartite = contabilitaAnagraficaBD.caricaControPartiteConImporto(areaContabileFullDTO
				.getAreaContabile());
		// se ci sono contropartite
		if (contropartite.size() > 0) {
			// mostro il dialogo delle contropartite dove inserire gli importi per la generazione delle righe contabili
			ControPartiteAreaContabilePage controPartiteAreaContabilePage = new ControPartiteAreaContabilePage(
					contropartite);
			ControPartiteAreaContabileDialog dialog = new ControPartiteAreaContabileDialog(
					controPartiteAreaContabilePage);
			dialog.setCloseAction(CloseAction.HIDE);
			dialog.showDialog();
			// se ho confermato il dialogo ho le contropartite compilate per generare le righe contabili
			if (dialog.isFinish()) {
				contropartite = dialog.getControPartiteCompilate();
			} else {
				contropartite = new ArrayList<ControPartita>();
			}
			dialog = null;
		}
		List<RigaContabile> righeContabili = contabilitaBD.creaRigheContabili(areaContabileFullDTO.getAreaContabile(),
				contropartite);
		return righeContabili;
	}

}
