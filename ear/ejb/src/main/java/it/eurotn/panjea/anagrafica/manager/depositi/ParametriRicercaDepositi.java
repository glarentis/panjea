package it.eurotn.panjea.anagrafica.manager.depositi;

import java.io.Serializable;

public class ParametriRicercaDepositi implements Serializable {

    private static final long serialVersionUID = -7506230880085453231L;

    private Integer idSedeAzienda;

    private Integer idEntita;

    private boolean loadDepositiInstallazione;

    public Integer getIdEntita() {
        return idEntita;
    }

    public Integer getIdSedeAzienda() {
        return idSedeAzienda;
    }

    public boolean isLoadDepositiInstallazione() {
        return loadDepositiInstallazione;
    }

    public void setIdEntita(Integer idEntita) {
        this.idEntita = idEntita;
    }

    public void setIdSedeAzienda(Integer idSedeAzienda) {
        this.idSedeAzienda = idSedeAzienda;
    }

    public void setLoadDepositiInstallazione(boolean loadDepositiInstallazione) {
        this.loadDepositiInstallazione = loadDepositiInstallazione;
    }
}
