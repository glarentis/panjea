package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenAreaMagazzinoEditorCommand extends ApplicationWindowAwareCommand {

	public static final String PARAM_AREA_MAGAZZINO_RICERCA = "paramAreaMagazzinoRicerca";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 */
	public OpenAreaMagazzinoEditorCommand() {
		super();
		this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {

		AreaMagazzinoRicerca areaMagazzinoRicerca = (AreaMagazzinoRicerca) getParameter(PARAM_AREA_MAGAZZINO_RICERCA,
				null);
		if (areaMagazzinoRicerca == null) {
			return;
		}
		AreaMagazzino areaMagazzino = new AreaMagazzino();
		areaMagazzino.setId(areaMagazzinoRicerca.getIdAreaMagazzino());

		AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
		LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
		Application.instance().getApplicationContext().publishEvent(event);

	}

}
