package it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand;

import it.eurotn.panjea.tesoreria.rich.statusbaritems.LettoreAssegniStatusBarItem;

import javax.swing.Icon;

import org.springframework.richclient.util.RcpSupport;

public class InitializedLettoreAssegniCommand extends LettoreAssegniEventCommand {

	private Icon disableIcon;

	/**
	 * Costruttore.
	 * 
	 * @param lettoreAssegniStatusBarItem
	 *            lettoreAssegniStatusBarItem
	 */
	public InitializedLettoreAssegniCommand(final LettoreAssegniStatusBarItem lettoreAssegniStatusBarItem) {
		super(lettoreAssegniStatusBarItem);
		disableIcon = RcpSupport.getIcon("assegniEnableStatusBarIcon");
	}

	@Override
	public void execute(Object object) {
		lettoreAssegniStatusBarItem.setIcon(disableIcon);
		lettoreAssegniStatusBarItem.setToolTipText("Lettore assegni pronto");
		lettoreAssegniStatusBarItem.setText("");
	}

}
