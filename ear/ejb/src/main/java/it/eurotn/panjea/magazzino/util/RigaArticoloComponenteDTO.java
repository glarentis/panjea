package it.eurotn.panjea.magazzino.util;

import java.math.BigDecimal;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

public class RigaArticoloComponenteDTO extends RigaArticoloDistintaDTO {

    private static final long serialVersionUID = -6528336619203988032L;

    private Integer idAreaMagazzino;

    /**
     * Presupponendo che this sia un articolo padre, aggiunge a this la qta e la descrizione aggiornando di conseguenza
     * il prezzo totale.
     * 
     * @param rigaArticoloDTO
     *            i dati del figlio da aggiungere al padre (this)
     */
    public void aggiungiArticoloDTO(RigaArticoloDTO rigaArticoloDTO) {
        setQta(getQta() + rigaArticoloDTO.getQta());
        setQtaChiusa(getQtaChiusa() + rigaArticoloDTO.getQtaChiusa());
        setPrezzoTotale(getPrezzoNetto().multiply(BigDecimal.valueOf(getQta()), getNumeroDecimaliPrezzo()));
    }

    /**
     * @return the idAreaMagazzino
     */
    public Integer getIdAreaMagazzino() {
        return idAreaMagazzino;
    }

    @Override
    public RigaMagazzino getRigaMagazzino() {
        RigaArticoloComponente riga = new RigaArticoloComponente();
        riga.setId(this.getId());

        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(this.idAreaMagazzino);
        riga.setAreaMagazzino(areaMagazzino);

        ArticoloLite articolo = new ArticoloLite();
        articolo.setId(this.getArticolo().getId());
        riga.setArticolo(articolo);

        riga.setPrezzoUnitario(this.getPrezzoUnitario());

        riga.setCodiceIva(this.getCodiceIva());

        if (getIdAreaMagazzinoCollegata() != null) {
            AreaMagazzino areaMagazzinoCollegata = new AreaMagazzino();
            areaMagazzinoCollegata.setId(getIdAreaMagazzinoCollegata());
            riga.setAreaMagazzinoCollegata(areaMagazzinoCollegata);
        }
        if (getIdAreaOrdineCollegata() != null) {
            AreaOrdine areaOrdine = new AreaOrdine();
            areaOrdine.setId(getIdAreaOrdineCollegata());
            riga.setAreaOrdineCollegata(areaOrdine);
        }
        return riga;
    }

    /**
     * @param idAreaMagazzino
     *            the idAreaMagazzino to set
     */
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {
        this.idAreaMagazzino = idAreaMagazzino;
    }
}
