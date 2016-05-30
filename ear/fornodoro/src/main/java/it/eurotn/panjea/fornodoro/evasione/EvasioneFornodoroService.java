package it.eurotn.panjea.fornodoro.evasione;

import java.io.File;

import javax.ejb.Local;

@Local
public interface EvasioneFornodoroService {
	/**
	 * Se esiste il file semaforo nella cartella.
	 * 
	 * @param fileSemaforo
	 *            file semaforo, mi serve per sapere quando avviare l'importazione del file generato dalla bilancia
	 */
	void evadi(File fileSemaforo);
}
