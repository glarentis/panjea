package it.eurotn.panjea.magazzino.rich.commands.documento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.rulesvalidation.FatturazioneRulesChecker;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRegoleValidazioneRighe;

/**
 * @author Leonardo
 */
public class GestisciRigheNonValideCommand extends ActionCommand {

	public static final String COMMAND_ID = "gestisciRigheNonValideCommand";
	private Collection<AreaMagazzinoLite> areeMagazzino = null;

	/**
	 * Costruttore di default.
	 *
	 * @param pageId
	 *            id della pagina.
	 */
	public GestisciRigheNonValideCommand(final String pageId) {
		super(COMMAND_ID);
		this.setSecurityControllerId(pageId + ".controller");
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		ParametriRegoleValidazioneRighe parametriRegoleValidazioneRighe = new ParametriRegoleValidazioneRighe();
		parametriRegoleValidazioneRighe.addAllRegole(new FatturazioneRulesChecker().getRules());
		List<AreaMagazzinoLite> aree = new ArrayList<AreaMagazzinoLite>();
		if (areeMagazzino != null) {
			aree.addAll(areeMagazzino);
		}
		parametriRegoleValidazioneRighe.setAreeMagazzino(aree);
		LifecycleApplicationEvent event = new OpenEditorEvent(parametriRegoleValidazioneRighe);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param areeMagazzino
	 *            areeMagazzino to set.
	 */
	public void setAreeMagazzino(Collection<AreaMagazzinoLite> areeMagazzino) {
		this.areeMagazzino = areeMagazzino;
	}

}