package it.eurotn.panjea.magazzino.manager.intento.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

/**
 * @author fattazzo
 *
 */
@Local
public interface DichiarazioneIntentoManager {

    /**
     * Genera una lista di righe articolo generate automaticamente per l'area magazzino specificata.
     *
     * @param areaMagazzino
     *            l'area magazzino di cui generare le righe
     * @return List<RigaArticoloGenerata>
     */
    List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino);

}
