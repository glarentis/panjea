package it.eurotn.panjea.magazzino.util;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class RigaTestataDTO extends RigaMagazzinoDTO {

    private static final long serialVersionUID = 2361141194408649798L;

    private String codiceTipoDocumentoCollegato;

    private Integer idAreaMagazzinoCollegata;

    private Integer idAreaOrdineCollegata;

    private IAreaDocumento areaDocumentoCollegata;

    /**
     * @return restituisce l'area documento collegata
     */
    public IAreaDocumento getAreaDocumentoCollegata() {
        if (areaDocumentoCollegata == null) {
            if (idAreaMagazzinoCollegata != null) {
                AreaMagazzino areaMagazzino = new AreaMagazzino();
                areaMagazzino.setId(idAreaMagazzinoCollegata);
                areaDocumentoCollegata = areaMagazzino;
            } else if (idAreaOrdineCollegata != null) {
                AreaOrdine areaOrdine = new AreaOrdine();
                areaOrdine.setId(idAreaOrdineCollegata);
                areaDocumentoCollegata = areaOrdine;
            }
        }

        return areaDocumentoCollegata;
    }

    /**
     * @return the codiceTipoDocumentoCollegato
     * @uml.property name="codiceTipoDocumentoCollegato"
     */
    public String getCodiceTipoDocumentoCollegato() {
        return codiceTipoDocumentoCollegato;
    }

    @Override
    public RigaMagazzino getRigaMagazzino() {
        RigaMagazzino riga = new RigaTestata();
        PanjeaEJBUtil.copyProperties(riga, this);
        return riga;
    }

    /**
     * 
     * @return true se la riga testata è una riga Testata di un documento di origine
     */
    public boolean isRigaTestataDocumento() {
        return codiceTipoDocumentoCollegato != null;
    }

    /**
     * @param areaDocumento
     *            the areaDocumentoCollegata to set
     */
    public void setAreaDocumentoCollegata(IAreaDocumento areaDocumento) {
        this.areaDocumentoCollegata = areaDocumento;
    }

    /**
     * @param codiceTipoDocumentoCollegato
     *            the codiceTipoDocumentoCollegato to set
     * @uml.property name="codiceTipoDocumentoCollegato"
     */
    public void setCodiceTipoDocumentoCollegato(String codiceTipoDocumentoCollegato) {
        this.codiceTipoDocumentoCollegato = codiceTipoDocumentoCollegato;
    }

    /**
     * @param idAreaMagazzinoCollegata
     *            the idAreaMagazzinoCollegata to set
     */
    public void setIdAreaMagazzinoCollegata(Integer idAreaMagazzinoCollegata) {
        this.idAreaMagazzinoCollegata = idAreaMagazzinoCollegata;
    }

    /**
     * @param idAreaOrdineCollegata
     *            the idAreaOrdineCollegata to set
     */
    public void setIdAreaOrdineCollegata(Integer idAreaOrdineCollegata) {
        this.idAreaOrdineCollegata = idAreaOrdineCollegata;
    }
}
