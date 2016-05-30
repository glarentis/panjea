package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.exception.EsportaDocumentoCassaException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class EsportaDocumentoCommand extends ActionCommand {

	public static final String PARAM_AREA_MAGAZZINO = "paramAreaMagazzino";

	private final IMagazzinoDocumentoBD magazzinoDocumentoBD;

	public static final String COMMAND_ID = "esportaDocumentoCommand";

	/**
	 * Costruttore.
	 * 
	 * @param magazzinoDocumentoBD
	 *            magazzinoDocumentoBD
	 */
	public EsportaDocumentoCommand(final IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	@Override
	protected void doExecuteCommand() {

		AreaMagazzino areaMagazzino = (AreaMagazzino) getParameter(PARAM_AREA_MAGAZZINO, null);

		if (areaMagazzino != null) {
			try {
				magazzinoDocumentoBD.esportaDocumento(areaMagazzino);
			} catch (EsportaDocumentoCassaException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
