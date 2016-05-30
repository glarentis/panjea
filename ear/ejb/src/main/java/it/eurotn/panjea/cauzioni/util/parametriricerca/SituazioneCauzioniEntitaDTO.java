package it.eurotn.panjea.cauzioni.util.parametriricerca;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

public class SituazioneCauzioniEntitaDTO extends SituazioneCauzioniDTO {

    private static final long serialVersionUID = 6768618574718463179L;

    private ArticoloLite articolo;

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    @Override
    public void initialize() {
        super.initialize();

        this.articolo = new ArticoloLite();
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.articolo.setCodice(codiceArticolo);
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articolo.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articolo.setId(idArticolo);
    }
}
