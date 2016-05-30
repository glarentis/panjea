package it.eurotn.panjea.shortcut;

import java.nio.file.Path;

public interface ShortCutCreator {
	/**
	 * Crea lo shortcut sul desktop
	 *
	 * @param path
	 *            di partenza di paneja
	 */
	void create(Path path);
}
