package it.eurotn.panjea.manutenzioni.manager.ubicazioniinstallazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;

@Local
public interface UbicazioniInstallazioneManager extends CrudManager<UbicazioneInstallazione> {
}