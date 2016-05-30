package it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand;

import it.eurotn.panjea.tesoreria.rich.statusbaritems.LettoreAssegniStatusBarItem;

public class EnableLettoreAssegniEventCommand extends LettoreAssegniEventCommand {

	/**
	 * Costruttore.
	 * 
	 * @param lettoreAssegniStatusBarItem
	 *            lettoreAssegniStatusBarItem
	 */
	public EnableLettoreAssegniEventCommand(final LettoreAssegniStatusBarItem lettoreAssegniStatusBarItem) {
		super(lettoreAssegniStatusBarItem);
	}

	@Override
	public void execute(Object object) {
		lettoreAssegniStatusBarItem.setVisible(true);
	}

}
