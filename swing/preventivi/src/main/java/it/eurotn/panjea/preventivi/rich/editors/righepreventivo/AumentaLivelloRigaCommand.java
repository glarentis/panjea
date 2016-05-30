package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.preventivi.rich.bd.IRigheBD;

import java.util.Set;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class AumentaLivelloRigaCommand extends ActionCommand {

	private static final String COMMAND_ID = "aumentaLivelloRigaCommand";
	public static final String RIGHE_DA_SPOSTARE = "righeDaSpostare";
	private final IRigheBD<?> righeBD;

	/**
	 * Costruttore.
	 * 
	 * @param ordiniDocumentoBD
	 *            {@link IOrdiniDocumentoBD}
	 */
	public AumentaLivelloRigaCommand(final IRigheBD<?> ordiniDocumentoBD) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.righeBD = ordiniDocumentoBD;
	}

	@Override
	protected void doExecuteCommand() {
		@SuppressWarnings("unchecked")
		Set<Integer> idRighe = (Set<Integer>) getParameter(RIGHE_DA_SPOSTARE);
		righeBD.collegaTestata(idRighe);
	}

}
