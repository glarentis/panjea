package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.sicurezza.domain.Utente;

public class SpedizioneMovimenti implements Serializable {

    private static final long serialVersionUID = 4618646107998273174L;

    private List<MovimentoSpedizioneDTO> movimenti;

    private TipoLayout tipoLayout;

    private Utente utente;

    /**
     * Costruttore.
     */
    public SpedizioneMovimenti() {
        super();
        movimenti = new ArrayList<MovimentoSpedizioneDTO>();
    }

    /**
     * @return the movimenti
     */
    public List<MovimentoSpedizioneDTO> getMovimenti() {
        return movimenti;
    }

    /**
     * @return the tipoLayout
     */
    public TipoLayout getTipoLayout() {
        return tipoLayout;
    }

    /**
     * @return the utente
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * @param movimenti
     *            the movimenti to set
     */
    public void setMovimenti(List<MovimentoSpedizioneDTO> movimenti) {
        this.movimenti = movimenti;
    }

    /**
     * @param tipoLayout
     *            the tipoLayout to set
     */
    public void setTipoLayout(TipoLayout tipoLayout) {
        this.tipoLayout = tipoLayout;
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
