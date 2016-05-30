/**
 *
 */
package it.eurotn.panjea.magazzino.rich.commands;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.rich.editors.documento.AbstractEliminaDocumentoCommand;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Leonardo
 */
public class EliminaAreaMagazzinoCommand extends AbstractEliminaDocumentoCommand {

	private static Logger logger = Logger.getLogger(EliminaAreaMagazzinoCommand.class);
	private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
	private List<AreaMagazzinoRicerca> areeMagazzinoRicerca = Collections.emptyList();

	public static final String PARAM_AREA_MAGAZZINO = "areaMagazzinoParam";
	public static final String PARAM_AREA_CONTABILE = "areaContabileParam";

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 */
	public EliminaAreaMagazzinoCommand(final String pageId) {
		super(pageId + ".controller");
	}

	@Override
	public Object doDelete(boolean deleteAreeCollegate) {
		logger.debug("--> Enter doDelete");

		AreaMagazzino areaMagazzino = (AreaMagazzino) getParameter(PARAM_AREA_MAGAZZINO, null);

		if (areaMagazzino == null) {
			List<AreaMagazzino> areeMagazzinoDaCancellare = new ArrayList<AreaMagazzino>();

			for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzinoRicerca) {
				AreaMagazzino areaMagazzinoCorrente = new AreaMagazzino();
				areaMagazzinoCorrente.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
				areaMagazzinoCorrente.setDocumento(areaMagazzinoRicerca.getDocumento());
				if (areaMagazzinoRicerca.getEntitaDocumento() != null
						&& (TipoEntita.CLIENTE.equals(areaMagazzinoRicerca.getEntitaDocumento().getTipoEntita()) || TipoEntita.FORNITORE
								.equals(areaMagazzinoRicerca.getEntitaDocumento().getTipoEntita()))) {
					areaMagazzinoCorrente.getDocumento().setIdEntita(areaMagazzinoRicerca.getEntitaDocumento().getId());
					areaMagazzinoCorrente.getDocumento().setCodiceEntita(
							areaMagazzinoRicerca.getEntitaDocumento().getCodice());
					areaMagazzinoCorrente.getDocumento().setDenominazioneAnagrafica(
							areaMagazzinoRicerca.getEntitaDocumento().getDescrizione());
				}
				areaMagazzinoCorrente.setTipoAreaMagazzino(areaMagazzinoRicerca.getTipoAreaMagazzino());
				areaMagazzinoCorrente.setStatoAreaMagazzino(areaMagazzinoRicerca.getStato());
				areeMagazzinoDaCancellare.add(areaMagazzinoCorrente);
			}
			// cancello
			magazzinoDocumentoBD.cancellaAreeMagazzino(areeMagazzinoDaCancellare, deleteAreeCollegate);
			return areeMagazzinoDaCancellare;
		} else {
			if (areaMagazzino.getId() != null) {
				try {
					magazzinoDocumentoBD.cancellaAreaMagazzino(areaMagazzino, deleteAreeCollegate, deleteAreeCollegate);
				} catch (AreeCollegatePresentiException e) {
					// non deve arrivare
					throw new RuntimeException(e);
				}
				AreaMagazzinoFullDTO areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
				areaMagazzinoFullDTO.setAreaMagazzino(areaMagazzino);
				return areaMagazzinoFullDTO;
			}
		}

		return null;
	}

	/**
	 * @return the areeMagazzinoRicerca
	 */
	public List<AreaMagazzinoRicerca> getAreeMagazzinoRicerca() {
		return areeMagazzinoRicerca;
	}

	@Override
	protected boolean isAreaCollegataPresente() {
		boolean isAreaContabilePresente = (Boolean) getParameter(PARAM_AREA_CONTABILE, true);
		return isAreaContabilePresente;
	}

	/**
	 * @param areeMagazzinoRicerca
	 *            the areeMagazzinoRicerca to set
	 */
	public void setAreeMagazzinoRicerca(List<AreaMagazzinoRicerca> areeMagazzinoRicerca) {
		this.areeMagazzinoRicerca = areeMagazzinoRicerca;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
