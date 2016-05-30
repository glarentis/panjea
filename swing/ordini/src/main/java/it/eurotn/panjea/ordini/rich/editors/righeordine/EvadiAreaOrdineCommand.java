/**
 * 
 */
package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;

import java.util.ArrayList;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 * 
 */
public class EvadiAreaOrdineCommand extends ApplicationWindowAwareCommand {

	public static final String PARAM_AREA_ORDINE = "areaOrdine";

	/**
	 * Costruttore.
	 */
	public EvadiAreaOrdineCommand() {
		super("evadiAreaOrdineCommand");
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		AreaOrdine areaOrdine = (AreaOrdine) getParameter(PARAM_AREA_ORDINE, null);

		if (areaOrdine == null || areaOrdine.isNew()) {
			return;
		}

		boolean isOrdineCliente = areaOrdine.getDocumento().getEntita() != null
				&& areaOrdine.getDocumento().getEntitaDocumento().getTipoEntita() == TipoEntita.CLIENTE;

		if (isOrdineCliente && areaOrdine.getStatoAreaOrdine() == StatoAreaOrdine.CONFERMATO) {
			ParametriRicercaEvasione parametri = new ParametriRicercaEvasione();
			parametri.getDataRegistrazione().setDataIniziale(areaOrdine.getDataRegistrazione());
			parametri.getDataRegistrazione().setDataFinale(areaOrdine.getDataRegistrazione());
			parametri.setNumeroDocumentoIniziale(areaOrdine.getDocumento().getCodice());
			parametri.setNumeroDocumentoFinale(areaOrdine.getDocumento().getCodice());
			parametri.getTipiAreaOrdine().add(areaOrdine.getTipoAreaOrdine());
			parametri.setEntita(areaOrdine.getDocumento().getEntita());
			parametri.setSedeEntita(areaOrdine.getDocumento().getSedeEntita());
			parametri.setTipoEntita(TipoEntita.CLIENTE);
			ArrayList<AreaOrdine> areeOrdine = new ArrayList<AreaOrdine>();
			areeOrdine.add(areaOrdine);
			parametri.setAreeOrdine(areeOrdine);
			parametri.setEvadiOrdini(true);
			parametri.setEffettuaRicerca(true);
			OpenEditorEvent event = new OpenEditorEvent(parametri);
			Application.instance().getApplicationContext().publishEvent(event);
		} else {
			Message message = new DefaultMessage(
					"E' possibile creare la missione solo per ordini a cliente confermati.", Severity.INFO);
			new MessageDialog("Attenzione", message).showDialog();
		}
	}

}
