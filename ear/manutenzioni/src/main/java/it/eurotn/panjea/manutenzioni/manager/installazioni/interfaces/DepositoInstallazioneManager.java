package it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.DepositoInstallazione;

@Local
public interface DepositoInstallazioneManager extends CrudManager<DepositoInstallazione> {

    /**
     * Carica il deposito installazione per la sede entità indicata.
     *
     * @param sedeEntita
     *            sede entià
     * @return deposito caricato, <code>null</code> se non esiste
     */
    DepositoInstallazione caricaDeposito(SedeEntita sedeEntita);

    /**
     *
     * @param sedeEntita
     *            sedeEntita associata al deposito da creare
     * @param sedeAzienda
     *            sedeAzienda associata al deposito da creare
     * @return nuovo deposito o depostio già esistente per la sede.
     */
    DepositoInstallazione caricaOCreaDeposito(SedeEntita sedeEntita, SedeAzienda sedeAzienda);
}
