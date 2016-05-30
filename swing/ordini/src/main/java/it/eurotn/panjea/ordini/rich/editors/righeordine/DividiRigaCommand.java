package it.eurotn.panjea.ordini.rich.editors.righeordine;

import java.awt.Dimension;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.righeordine.divisioneriga.DividiRigaOrdineTablePage;
import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class DividiRigaCommand extends ApplicationWindowAwareCommand {

	private class DividiRigaDialog extends PanjeaTitledPageApplicationDialog {

		private RigaArticolo rigaSelezionata;
		
		public DividiRigaDialog(RigaArticolo rigaSelezionata) {
			super(new DividiRigaOrdineTablePage(rigaSelezionata));
			this.rigaSelezionata = rigaSelezionata;
			setPreferredSize(new Dimension(800, 300));
			setTitlePaneTitle("Inserisci la quantità e la data di consegna per le nuove righe");
		}

		@Override
		protected boolean onFinish() {
			ordiniDocumentoBD.dividiRiga(rigaSelezionata.getId(),
					((DividiRigaOrdineTablePage) getDialogPage()).getTable().getRows());
			return true;
		}

	}

	public static final String RIGA_SELEZIONATA = "rigaSelezionata";
	private static Logger logger = Logger.getLogger(DividiRigaCommand.class);
	private IOrdiniDocumentoBD ordiniDocumentoBD;

	public DividiRigaCommand(IOrdiniDocumentoBD ordiniDocumentoBD) {
		super("dividiRigaCommand");
		this.ordiniDocumentoBD = ordiniDocumentoBD;
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		RigaArticoloDTO rigaArticolo = (RigaArticoloDTO) getParameter(RIGA_SELEZIONATA);
		RigaOrdine ro = new RigaArticolo();
		ro.setId(rigaArticolo.getId());
		DividiRigaDialog dialog = new DividiRigaDialog((RigaArticolo) ordiniDocumentoBD.caricaRigaOrdine(ro));
		dialog.showDialog();
		logger.debug("--> Exit doExecuteCommand");
	}

}