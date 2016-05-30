package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.List;

public class PreFatturazioneDTO implements Serializable {

    private static final long serialVersionUID = 4090826303382023995L;

    private List<MovimentoPreFatturazioneDTO> movimenti;

    /**
     * Costruttore.
     */
    public PreFatturazioneDTO() {
        super();
    }

    /**
     * @return the movimenti
     */
    public List<MovimentoPreFatturazioneDTO> getMovimenti() {
        return movimenti;
    }

    /**
     * @param movimenti
     *            the movimenti to set
     */
    public void setMovimenti(List<MovimentoPreFatturazioneDTO> movimenti) {
        this.movimenti = movimenti;
    }

}
