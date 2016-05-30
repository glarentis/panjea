package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.util.RigaTestataDTO;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ApriDocumentoCollegatoCommand extends ApplicationWindowAwareCommand {

	private final RigheOrdineTablePage righeOrdineTablePage;

	/**
	 * Apre l'area collegata della riga testata selezionata.
	 * 
	 * @param righeOrdineTablePage
	 *            pagina delle righe ordine
	 */
	public ApriDocumentoCollegatoCommand(final RigheOrdineTablePage righeOrdineTablePage) {
		super("Apri documento collegato");
		RcpSupport.configure(this);
		setIcon(RcpSupport.getIcon(LivelloRigaOrdineCellRenderer.RIGA_TESTATA_ICON));
		this.righeOrdineTablePage = righeOrdineTablePage;
	}

	@Override
	public void doExecuteCommand() {
		if (righeOrdineTablePage.getTable().getSelectedObject() instanceof RigaTestataDTO) {
			RigaTestataDTO rigaTestata = (RigaTestataDTO) righeOrdineTablePage.getTable().getSelectedObject();

			LifecycleApplicationEvent event = new OpenEditorEvent(rigaTestata.getAreaDocumentoCollegata());
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}
}