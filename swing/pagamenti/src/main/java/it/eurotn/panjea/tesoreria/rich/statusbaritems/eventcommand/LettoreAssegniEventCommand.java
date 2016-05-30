package it.eurotn.panjea.tesoreria.rich.statusbaritems.eventcommand;

import it.eurotn.panjea.tesoreria.rich.statusbaritems.LettoreAssegniStatusBarItem;

public abstract class LettoreAssegniEventCommand {

	protected LettoreAssegniStatusBarItem lettoreAssegniStatusBarItem;

	/**
	 * Costruttore.
	 * 
	 * @param lettoreAssegniStatusBarItem
	 *            lettoreAssegniStatusBarItem
	 */
	public LettoreAssegniEventCommand(final LettoreAssegniStatusBarItem lettoreAssegniStatusBarItem) {
		super();
		this.lettoreAssegniStatusBarItem = lettoreAssegniStatusBarItem;
	}

	/**
	 * Execute action.
	 * 
	 * @param object
	 *            object
	 */
	public abstract void execute(Object object);
}
