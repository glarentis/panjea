package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class ForzaRimuoviSelezionateCommand extends ApplicationWindowAwareCommand {

	private static final String COMMAND_ID = "forzaRimuoviSelezionateCommand";

	public static final Object SELECTED_ROW = "selectedRow";

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 * @param ordiniDocumentoBD
	 *            degli ordini.
	 */
	public ForzaRimuoviSelezionateCommand(final IOrdiniDocumentoBD ordiniDocumentoBD) {
		super(COMMAND_ID);
		this.ordiniDocumentoBD = ordiniDocumentoBD;
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		@SuppressWarnings("unchecked")
		List<RigaDistintaCarico> righeSelezionate = (List<RigaDistintaCarico>) getParameter(SELECTED_ROW,
				new ArrayList<RigaDistintaCarico>());
		List<Integer> righeDaForzare = new ArrayList<Integer>();
		for (RigaDistintaCarico rigaEvasione : righeSelezionate) {
			righeDaForzare.add(rigaEvasione.getRigaArticolo().getId());
		}
		if (!righeDaForzare.isEmpty()) {
			ordiniDocumentoBD.forzaRigheOrdine(righeDaForzare);
		}
	}
}