package it.eurotn.panjea.giroclienti.util;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.util.Giorni;

public class SchedaGiroClientiDTO implements Serializable {

    private static final long serialVersionUID = -451539989895706758L;

    private Giorni giorno;

    private Date data;

    private List<RigaGiroCliente> righeGiroCliente;

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the giorno
     */
    public Giorni getGiorno() {
        return giorno;
    }

    /**
     * @return ordini inevasi presenti nella scheda
     */
    public int getOrdiniInevasi() {
        /***
         * int ordini = 0;
         *
         * for (RigaGiroCliente rigaGiroCliente : righeGiroCliente) { ordini = ordini + (rigaGiroCliente.getAreaOrdine()
         * != null && !rigaGiroCliente.getAreaOrdine().isEvaso() ? 1 : 0); }
         *
         * return ordini;
         *
         */
        return 0;
    }

    /**
     * @return ordini presenti nella scheda
     */
    public int getOrdiniPresenti() {
        int ordini = 0;

        for (RigaGiroCliente rigaGiroCliente : righeGiroCliente) {
            ordini = ordini + (rigaGiroCliente.getAreaOrdine() == null ? 0 : 1);
        }

        return ordini;
    }

    /**
     * @return the righeGiroCliente
     */
    public List<RigaGiroCliente> getRigheGiroCliente() {
        return righeGiroCliente;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param giorno
     *            the giorno to set
     */
    public void setGiorno(Giorni giorno) {
        this.giorno = giorno;
    }

    /**
     * @param righeGiroCliente
     *            the righeGiroCliente to set
     */
    public void setRigheGiroCliente(List<RigaGiroCliente> righeGiroCliente) {
        this.righeGiroCliente = righeGiroCliente;
    }
}
