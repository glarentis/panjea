package it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.ParametriRicercaAreeInstallazione;

@Local
public interface AreeInstallazioniManager extends CrudManager<AreaInstallazione> {

    /**
     *
     * @param parametri
     *            parametri di ricerca
     * @return parametri di ricerca
     */
    List<AreaInstallazione> ricercaAreeInstallazioni(ParametriRicercaAreeInstallazione parametri);
}