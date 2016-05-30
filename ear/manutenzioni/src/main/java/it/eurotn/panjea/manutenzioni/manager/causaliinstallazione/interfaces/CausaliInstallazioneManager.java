package it.eurotn.panjea.manutenzioni.manager.causaliinstallazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;

@Local
public interface CausaliInstallazioneManager extends CrudManager<CausaleInstallazione> {
}