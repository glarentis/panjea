package it.eurotn.panjea.tesoreria.rich.statusbaritems;

import java.util.HashMap;
import java.util.Map;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.tesoreria.manager.LettoreAssegniManager;
import it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand.DisableLettoreAssegniEventCommand;
import it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand.EnableLettoreAssegniEventCommand;
import it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand.InitializedLettoreAssegniCommand;
import it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand.LettoreAssegniEventCommand;
import it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand.NotCalibratedLettoreAssegniCommand;
import it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand.NotInitializedLettoreAssegniCommand;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.status.LabelStatusBarItem;

public class LettoreAssegniStatusBarItem extends LabelStatusBarItem implements ApplicationListener {

	private static final long serialVersionUID = 7762482399815233655L;
	
	private Map<String, LettoreAssegniEventCommand> eventsMap;

	/**
	 * Costruttore.
	 * 
	 */
	public LettoreAssegniStatusBarItem() {
		super();
		
		initEventsMap();

		setIcon(RcpSupport.getIcon("assegniStatusBarIcon"));
		setToolTipText("");
		setText("");
	}
	
	/**
	 * Inizializza la mappa che contiene i command per gli eventi.
	 */
	private void initEventsMap() {
		eventsMap = new HashMap<String, LettoreAssegniEventCommand>();
		eventsMap.put(LettoreAssegniManager.ENABLE_LETTORE_ASSEGNI_EVENT, new EnableLettoreAssegniEventCommand(this));
		eventsMap.put(LettoreAssegniManager.DISABLE_LETTORE_ASSEGNI_EVENT, new DisableLettoreAssegniEventCommand(this));
		eventsMap.put(LettoreAssegniManager.LETTORE_ASSEGNI_NOT_INITIALIZED, new NotInitializedLettoreAssegniCommand(this));
		eventsMap.put(LettoreAssegniManager.LETTORE_ASSEGNI_INITIALIZED, new InitializedLettoreAssegniCommand(this));
		eventsMap.put(LettoreAssegniManager.LETTORE_ASSEGNI_NOT_CALIBRATED, new NotCalibratedLettoreAssegniCommand(this));
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {

		if (event instanceof PanjeaLifecycleApplicationEvent) {

			LettoreAssegniEventCommand command = eventsMap.get(((PanjeaLifecycleApplicationEvent) event).getEventType());
			
			if (command != null) {
				command.execute(event.getSource());
			}
		}
	}
}
