/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.pm.RigaConto;
import it.eurotn.panjea.contabilita.rich.pm.RigaContoCentroCosto;
import it.eurotn.panjea.contabilita.rich.pm.RigaMastro;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 * @author leonardo
 */
public class SaldoContoTreeTableNode extends DefaultMutableTreeTableNode {

	private static Logger logger = Logger.getLogger(SaldoContoTreeTableNode.class);

	/**
	 * Costruttore.
	 * 
	 * @param conti
	 *            conti
	 */
	public SaldoContoTreeTableNode(final List<SaldoConto> conti) {
		super();
		createTreeNode(conti);
	}

	/**
	 * Costruisce il tree per il treeTableModel, il mastro è il nodo principale seguito da conto e sottoconto di quel
	 * mastro.
	 * 
	 * @param conti
	 *            lista di saldi conti
	 */
	private void createTreeNode(List<SaldoConto> conti) {
		// root su cui aggiungere i mastri

		SaldoConto oldRiga = null;

		RigaMastro currentMastro = null;
		RigaConto currentConto = null;
		SaldoConto currentSottoconto = null;

		DefaultMutableTreeTableNode nodeMastro = null;
		DefaultMutableTreeTableNode nodeConto = null;
		DefaultMutableTreeTableNode nodeSottoConto = null;

		// scorro le righe bilancio
		for (SaldoConto sottoContoDTO : conti) {

			logger.debug("--> mastro corrente " + sottoContoDTO.getMastroCodice());
			// se la riga precedente ha lo stesso codice mastro aggiungo il dare e avere della riga corrente sul
			// 'old RigaMastro
			if (oldRiga != null && sottoContoDTO.getMastroCodice().equals(oldRiga.getMastroCodice())) {
				logger.debug("--> aggiorno mastro corrente");
				currentMastro.aggiungiImportoDare(sottoContoDTO.getImportoDare());
				currentMastro.aggiungiImportoAvere(sottoContoDTO.getImportoAvere());
			} else {
				// altrimenti creo una nuava riga mastro con le sole info del mastro e dare e avere e la aggiungo al
				// tree
				currentMastro = new RigaMastro(sottoContoDTO.getMastroCodice(), sottoContoDTO.getMastroDescrizione(),
						sottoContoDTO.getMastroId(), sottoContoDTO.getTipoConto(), sottoContoDTO.getSottoTipoConto(),
						sottoContoDTO.getImportoDare(), sottoContoDTO.getImportoAvere());
				nodeMastro = new DefaultMutableTreeTableNode(currentMastro);
				logger.debug("--> creo mastro nuovo e aggiungo a root il nodo " + nodeMastro);
				this.add(nodeMastro);
			}

			// se la riga precedente ha la stessa riga conto aggiungo dare e avere
			if (oldRiga != null && oldRiga.getContoCodice().equals(sottoContoDTO.getContoCodice())
					&& oldRiga.getMastroCodice().equals(sottoContoDTO.getMastroCodice())) {
				logger.debug("--> aggiorno conto corrente");
				currentConto.aggiungiImportoDare(sottoContoDTO.getImportoDare());
				currentConto.aggiungiImportoAvere(sottoContoDTO.getImportoAvere());
			} else {
				// altrimenti creo una nuova rigaConto da associare al mastro
				currentConto = new RigaConto(sottoContoDTO.getContoCodice(), sottoContoDTO.getContoDescrizione(),
						sottoContoDTO.getContoId(), sottoContoDTO.getTipoConto(), sottoContoDTO.getSottoTipoConto(),
						sottoContoDTO.getImportoDare(), sottoContoDTO.getImportoAvere());
				nodeConto = new DefaultMutableTreeTableNode(currentConto);
				logger.debug("--> creo conto nuovo e aggiungo node conto " + nodeConto + " a node mastro " + nodeMastro);
				nodeMastro.add(nodeConto);
			}
			// non aggiungo il sottoconto con sottoconto codice vuoto perche' e' un sottoconto cliente o fornitore
			// che non serve visualizzare
			if (!sottoContoDTO.getSottoContoCodice().equals(SottoConto.DEFAULT_CODICE)) {
				// altrimenti creo una nuova rigaConto da associare al mastro
				currentSottoconto = (SaldoConto) PanjeaSwingUtil.cloneObject(sottoContoDTO);
				// se non ho centro di costo devo mettere l'importo di controllo = all'importo
				nodeSottoConto = new DefaultMutableTreeTableNode(currentSottoconto);
				nodeConto.add(nodeSottoConto);
				if (currentSottoconto.getCentriCosto() != null) {
					for (SaldoConto centroCosto : currentSottoconto.getCentriCosto()) {
						RigaContoCentroCosto rigaContoCentroCosto = new RigaContoCentroCosto(
								centroCosto.getCentroCostoCodice(), centroCosto.getCentroCostoDescrizione(),
								centroCosto.getCentroCostoId(), centroCosto.getTipoConto(),
								centroCosto.getSottoTipoConto(), centroCosto.getImportoDare(),
								centroCosto.getImportoAvere());
						nodeSottoConto.add(new DefaultMutableTreeTableNode(rigaContoCentroCosto));
					}
				}
			}

			// aggiorno i riferimenti di old mastro e old conto per il sottoconto successivo
			oldRiga = (SaldoConto) PanjeaSwingUtil.cloneObject(sottoContoDTO);
		}
	}

}
