package it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand;

import it.eurotn.panjea.tesoreria.rich.statusbaritems.LettoreAssegniStatusBarItem;

import javax.swing.Icon;

import org.springframework.richclient.util.RcpSupport;

public class NotCalibratedLettoreAssegniCommand extends LettoreAssegniEventCommand {

	private Icon disableIcon;

	/**
	 * Costruttore.
	 * 
	 * @param lettoreAssegniStatusBarItem
	 *            statusBar Item che descrive lo stato del lettore
	 */
	public NotCalibratedLettoreAssegniCommand(final LettoreAssegniStatusBarItem lettoreAssegniStatusBarItem) {
		super(lettoreAssegniStatusBarItem);
		disableIcon = RcpSupport.getIcon("assegniDisableStatusBarIcon");
	}

	@Override
	public void execute(Object object) {
		lettoreAssegniStatusBarItem.setIcon(disableIcon);
		lettoreAssegniStatusBarItem.setToolTipText("Lettore assegni non calibrato");
		lettoreAssegniStatusBarItem.setText("");
	}

}
