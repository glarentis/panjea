package it.eurotn.panjea.protocolli.rich.search;

import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.protocolli.rich.bd.IProtocolliBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * 
 * @author adriano
 */
public class ProtocolloSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "protocolloSearchObject";

	private IProtocolliBD protocolliBD;

	/**
	 * Costruttore.
	 */
	public ProtocolloSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		List<Protocollo> protocollos = protocolliBD.caricaProtocolli(valueSearch);
		return protocollos;
	}

	@Override
	public void openDialogPage(Object object) {
	}

	@Override
	public void openEditor(Object object) {
		LifecycleApplicationEvent event = new OpenEditorEvent("protocolliEditor");
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param protocolliBD
	 *            The protocolliBD to set.
	 */
	public void setProtocolliBD(IProtocolliBD protocolliBD) {
		this.protocolliBD = protocolliBD;
	}

}
