package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;

import java.util.Set;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class AumentaLivelloRigaMagazzinoCommand extends ActionCommand {

	private static final String COMMAND_ID = "aumentaLivelloRigaOrdineCommand";
	public static final String RIGHE_ORDINE_PARAM = "rigaOrdine";
	private final IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 * @param ordiniDocumentoBD
	 *            {@link IOrdiniDocumentoBD}
	 */
	public AumentaLivelloRigaMagazzinoCommand(final IMagazzinoDocumentoBD ordiniDocumentoBD) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.magazzinoDocumentoBD = ordiniDocumentoBD;
	}

	@Override
	protected void doExecuteCommand() {
		@SuppressWarnings("unchecked")
		Set<Integer> righeMagazzinoDaCambiare = (Set<Integer>) getParameter(RIGHE_ORDINE_PARAM);
		magazzinoDocumentoBD.collegaTestata(righeMagazzinoDaCambiare);
	}

}
