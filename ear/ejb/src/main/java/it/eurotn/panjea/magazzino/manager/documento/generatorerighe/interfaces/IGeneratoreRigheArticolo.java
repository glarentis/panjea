/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces;

import java.util.List;

import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

/**
 * Interfaccia che identifica un generatore di righe articolo.
 *
 * @author leonardo
 */
public interface IGeneratoreRigheArticolo {

    /**
     * Genera una lista di righe articolo generate automaticamente per l'area magazzino specificata.
     * 
     * @param areaMagazzino
     *            l'area magazzino di cui generare le righe
     * @return List<RigaArticoloGenerata>
     */
    List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino);

}
