package it.eurotn.panjea.ordini.rich.editors.areaordine;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.rich.control.table.JideTableWidget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class CambiaStatoAreaOrdineCommand extends ApplicationWindowAwareCommand implements Observer {

	private static final String COMMAND_ID = "cambiaStatoAreaOrdineCommand";
	private static final String COMMAND_BLOCCA_ID = "bloccaAreaOrdineCommand";
	private static final String COMMAND_SBLOCCA_ID = "sbloccaAreaOrdineCommand";
	private final JideTableWidget<AreaOrdineRicerca> jideTableWidget;
	private CommandFaceDescriptor faceBloccaDescriptor;
	private CommandFaceDescriptor faceSbloccaDescriptor;
	private Map<StatoAreaOrdine, CommandFaceDescriptor> facesButton;
	private final IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 * @param jideTableWidget
	 *            table dei risultati ricerca
	 * @param ordiniDocumentoBD
	 *            bd gestione ordini
	 */
	public CambiaStatoAreaOrdineCommand(final JideTableWidget<AreaOrdineRicerca> jideTableWidget,
			final IOrdiniDocumentoBD ordiniDocumentoBD) {
		super(COMMAND_ID);
		this.jideTableWidget = jideTableWidget;
		this.ordiniDocumentoBD = ordiniDocumentoBD;
		this.setSecurityControllerId(COMMAND_ID);
		RcpSupport.configure(this);
		faceBloccaDescriptor = new CommandFaceDescriptor(RcpSupport.getMessage(COMMAND_BLOCCA_ID),
				RcpSupport.getIcon(COMMAND_BLOCCA_ID), null);
		faceSbloccaDescriptor = new CommandFaceDescriptor(RcpSupport.getMessage(COMMAND_SBLOCCA_ID),
				RcpSupport.getIcon(COMMAND_SBLOCCA_ID), null);
		setFaceDescriptor(faceBloccaDescriptor);

		facesButton = new HashMap<AreaOrdine.StatoAreaOrdine, CommandFaceDescriptor>();
		facesButton.put(StatoAreaOrdine.PROVVISORIO, faceBloccaDescriptor);
		facesButton.put(StatoAreaOrdine.CONFERMATO, faceBloccaDescriptor);
		facesButton.put(StatoAreaOrdine.BLOCCATO, faceSbloccaDescriptor);
		facesButton.put(null, faceSbloccaDescriptor);
	}

	@Override
	protected void doExecuteCommand() {
		if (jideTableWidget.getSelectedObjects().size() == 0) {
			return;
		}

		Collection<Integer> idOrdini = new ArrayList<Integer>();
		StatoAreaOrdine stato = jideTableWidget.getSelectedObject().getStatoAreaOrdine();
		for (AreaOrdineRicerca areaOrdineRicerca : jideTableWidget.getSelectedObjects()) {
			idOrdini.add(areaOrdineRicerca.getIdAreaOrdine());
		}
		List<AreaOrdineRicerca> aree = ordiniDocumentoBD.bloccaOrdini(idOrdini,
				(stato == StatoAreaOrdine.CONFERMATO || stato == StatoAreaOrdine.PROVVISORIO));

		for (AreaOrdineRicerca areaOrdineRicerca : aree) {
			jideTableWidget.replaceRowObject(areaOrdineRicerca, areaOrdineRicerca, null);
		}
		update(null, null);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (jideTableWidget.getSelectedObject() == null) {
			return;
		}
		StatoAreaOrdine statoAreaOrdine = jideTableWidget.getSelectedObject().getStatoAreaOrdine();
		for (AreaOrdineRicerca areaOrdineRicerca : jideTableWidget.getSelectedObjects()) {
			if (!statoAreaOrdine.equals(areaOrdineRicerca.getStatoAreaOrdine())) {
				statoAreaOrdine = null;
				break;
			}
		}
		setEnabled(statoAreaOrdine != null);
		setFaceDescriptor(facesButton.get(statoAreaOrdine));
	}
}