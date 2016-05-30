package it.eurotn.panjea.lotti.rich.editors.articolo.movimentazione;

import it.eurotn.panjea.lotti.util.MovimentazioneLotto;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenDocumentoCommand extends ActionCommand {

	public static final String RIGA_MOVIMENTAZIONE_PARAM = "areaMagazzino";

	@Override
	protected void doExecuteCommand() {

		MovimentazioneLotto movimentazioneSelezionato = (MovimentazioneLotto) getParameter(RIGA_MOVIMENTAZIONE_PARAM);
		if (movimentazioneSelezionato != null) {
			AreaMagazzino am = new AreaMagazzino();
			am.setId(movimentazioneSelezionato.getIdAreaMagazzino());
			LifecycleApplicationEvent event = new OpenEditorEvent(am);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}
}