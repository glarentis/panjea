package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;

import java.util.Set;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class AumentaLivelloRigaOrdineCommand extends ActionCommand {

	private static final String COMMAND_ID = "aumentaLivelloRigaOrdineCommand";
	public static final String RIGHE_ORDINE_PARAM = "rigaOrdine";
	private final IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 * @param ordiniDocumentoBD
	 *            {@link IOrdiniDocumentoBD}
	 */
	public AumentaLivelloRigaOrdineCommand(final IOrdiniDocumentoBD ordiniDocumentoBD) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

	@Override
	protected void doExecuteCommand() {
		@SuppressWarnings("unchecked")
		Set<Integer> righeOrdineDaCambiare = (Set<Integer>) getParameter(RIGHE_ORDINE_PARAM);
		ordiniDocumentoBD.collegaTestata(righeOrdineDaCambiare);
	}

}
