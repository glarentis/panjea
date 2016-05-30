package it.eurotn.panjea.magazzino.util;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;

public class RigaArticoloDistintaDTO extends RigaArticoloDTO {
    private static final long serialVersionUID = 1L;

    private final ArticoloLite articoloPadre;

    @SuppressWarnings("unused")
    private String codiceEntitaPadre;

    /**
     * Costruttore.
     */
    public RigaArticoloDistintaDTO() {
        articoloPadre = new ArticoloLite();
    }

    /**
     * @return articoloPadre sulla riga
     */
    public ArticoloLite getArticoloPadre() {
        return articoloPadre;
    }

    @Override
    public RigaMagazzino getRigaMagazzino() {
        RigaMagazzino riga = new RigaArticoloDistinta();

        // mi basta solo l'id sia per il caricamento che per la cancellazione, la riga distinta esiste (a differenza
        // della riga padre) quindi impostando l'id posso caricare la riga e avere quindi tutte i dati che mi servono
        riga.setId(getId());
        return riga;
    }

    /**
     * @param codiceEntitaPadre
     *            the codiceEntitaPadre to set
     */
    public void setCodiceEntitaPadre(String codiceEntitaPadre) {
        this.codiceEntitaPadre = codiceEntitaPadre;
    }

    /**
     * @param codicePadre
     *            the codicePadre to set
     */
    public void setCodicePadre(String codicePadre) {
        this.articoloPadre.setCodice(codicePadre);
    }

    /**
     * @param descrizioneArticoloPadre
     *            descrizioneArticoloPadre dell'articoloPadre
     */
    public void setDescrizioneArticoloPadre(String descrizioneArticoloPadre) {
        this.articoloPadre.setDescrizione(descrizioneArticoloPadre);
    }

    /**
     * @param idArticoloPadre
     *            the idArticoloPadre to set
     */
    public void setIdArticoloPadre(Integer idArticoloPadre) {
        this.articoloPadre.setId(idArticoloPadre);
    }

}
