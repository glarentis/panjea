package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.datiaccompagnatori.DatiAccompagnatoriDialog;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.util.Assert;

public class CloseRigheMagazzinoCommand extends ActionCommand {

	private static Logger logger = Logger.getLogger(CloseRigheMagazzinoCommand.class);
	private static final String COMMAND_ID = "confermaAreaMagazzinoCommand";
	private AreaMagazzinoFullDTO areaMagazzinoFullDTO;
	private final IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 * @param magazzinoDocumentoBD
	 *            magazzinoDocumentoBD
	 * @param pageId
	 *            id della pagina
	 */
	public CloseRigheMagazzinoCommand(final IMagazzinoDocumentoBD magazzinoDocumentoBD, final String pageId) {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(pageId + ".controller");
		c.configure(this);
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * Conferma le righe magazzino dell'area.
	 * 
	 * @param forzaStato
	 *            indica se forzare lo stato
	 * @throws TotaleDocumentoNonCoerenteException
	 *             TotaleDocumentoNonCoerenteException
	 */
	protected void confermaRighe(boolean forzaStato) throws TotaleDocumentoNonCoerenteException {
		// dialog per inserimento dati accompagnatori; valido prima le righe in modo da avere l'area magazzino
		// confermata con eventuali dati accompagnatori calcolati, poi salvo i dati se confermo
		SortedSet<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatoriRichiesti = new TreeSet<DatoAccompagnatorioMagazzinoMetaData>(
				areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino().getDatiAccompagnatoriMetaData());

		// attenzione, i dati accompagnatori non vengono salvati nel dialogo, vengono solo modificati;
		// è la valida righe che si preoccupa di salvare le informazioni della testata; in questo modo l'area magazzino
		// viene salvata una volta sola
		if (!datiAccompagnatoriRichiesti.isEmpty()) {
			DatiAccompagnatoriDialog datiAccompagnatoriDialog = new DatiAccompagnatoriDialog(areaMagazzinoFullDTO);
			datiAccompagnatoriDialog.showDialog();
			areaMagazzinoFullDTO = datiAccompagnatoriDialog.getAreaMagazzinoFullDTO();
		}

		if (forzaStato) {
			try {
				areaMagazzinoFullDTO = magazzinoDocumentoBD.validaRigheMagazzino(
						areaMagazzinoFullDTO.getAreaMagazzino(), areaMagazzinoFullDTO.getAreaRate(),
						areaMagazzinoFullDTO.getAreaContabileLite(), true);
			} catch (Exception e) {
				// EXCEPTION MAIL verifico i dati passati alla conferma righe magazzino
				logger.error(
						"--> Errore nella conferma delle righe magazzino, areaMagazzino "
								+ areaMagazzinoFullDTO.getAreaMagazzino() + ", areaRate "
								+ areaMagazzinoFullDTO.getAreaRate() + ", areaContabile "
								+ areaMagazzinoFullDTO.getAreaContabileLite(), e);
				throw new PanjeaRuntimeException(e);
			}
		} else {
			try {
				areaMagazzinoFullDTO = magazzinoDocumentoBD.validaRigheMagazzino(
						areaMagazzinoFullDTO.getAreaMagazzino(), areaMagazzinoFullDTO.getAreaRate(),
						areaMagazzinoFullDTO.getAreaContabileLite(), false);
			} catch (TotaleDocumentoNonCoerenteException e) {
				// EXCEPTION MAIL verifico i dati passati alla conferma righe magazzino
				logger.error("--> TotaleDocumentoNonCoerenteException nella conferma delle righe magazzino, areaMagazzino "
						+ areaMagazzinoFullDTO.getAreaMagazzino()
						+ ", areaRate "
						+ areaMagazzinoFullDTO.getAreaRate()
						+ ", areaContabile " + areaMagazzinoFullDTO.getAreaContabileLite());
				throw e;
			}
		}
	}

	@Override
	protected void doExecuteCommand() {
		Assert.notNull(areaMagazzinoFullDTO);
		try {
			confermaRighe(false);
		} catch (TotaleDocumentoNonCoerenteException e) {
			TotaleDocumentoNonCoerenteDialog dialog = new TotaleDocumentoNonCoerenteDialog(e);
			dialog.showDialog();
			if (dialog.isCambiaStato()) {
				try {
					confermaRighe(true);
				} catch (TotaleDocumentoNonCoerenteException e1) {
					// qui non deve darmi un error
					throw new PanjeaRuntimeException(
							"Generato un errore TotaleDocumentoNonCoerenteException che in questo punto non deve essere generato",
							e1);
				}
			}
		}
	}

	/**
	 * @return areaMagazzinoFullDTO
	 */
	public AreaMagazzinoFullDTO getAreaMagazzinoFullDTO() {
		return areaMagazzinoFullDTO;
	}

	/**
	 * @param areaMagazzinoFullDTO
	 *            areaMagazzinoFullDTO
	 */
	public void setAreaMagazzinoFullDTO(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
		this.areaMagazzinoFullDTO = areaMagazzinoFullDTO;
	}
}