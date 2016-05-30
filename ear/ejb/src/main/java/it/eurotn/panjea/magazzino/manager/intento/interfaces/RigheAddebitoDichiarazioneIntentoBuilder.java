/**
 *
 */
package it.eurotn.panjea.magazzino.manager.intento.interfaces;

import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.IGeneratoreRigheArticolo;

import javax.ejb.Local;

/**
 * Genera la riga per l'addebito del bollo per la dichiarazione di intento.<br/>
 * L'articolo e il prezzo per generare la riga vengono caricati dal magazzinoSettings, dove vengono registrate le righe
 * per definire articolo, data vigore e prezzo.
 * 
 * @author leonardo
 */
@Local
public interface RigheAddebitoDichiarazioneIntentoBuilder extends IGeneratoreRigheArticolo {

}
