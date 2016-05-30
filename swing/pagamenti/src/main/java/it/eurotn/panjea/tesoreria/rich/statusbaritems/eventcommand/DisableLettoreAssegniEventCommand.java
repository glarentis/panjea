package it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand;

import it.eurotn.panjea.tesoreria.rich.statusbaritems.LettoreAssegniStatusBarItem;

public class DisableLettoreAssegniEventCommand extends LettoreAssegniEventCommand {

	/**
	 * Costruttore.
	 * 
	 * @param lettoreAssegniStatusBarItem
	 *            lettoreAssegniStatusBarItem
	 */
	public DisableLettoreAssegniEventCommand(final LettoreAssegniStatusBarItem lettoreAssegniStatusBarItem) {
		super(lettoreAssegniStatusBarItem);
	}

	@Override
	public void execute(Object object) {
		lettoreAssegniStatusBarItem.setVisible(false);
	}

}
