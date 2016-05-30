package it.eurotn.panjea.cosaro.sync.bilance;

import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

public class RigaTestataGammaMeat {
    private AreaOrdine areaOrdine;
    private SedeEntita sedePrincipale;

    /**
     *
     * @param areaOrdine
     *            araeOrdine wrappara
     * @param sedePrincipale
     *            sede Principale entita
     */
    public RigaTestataGammaMeat(final AreaOrdine areaOrdine, SedeEntita sedePrincipale) {
        this.areaOrdine = areaOrdine;
        this.sedePrincipale = sedePrincipale;
    }

    public String getCap() {
        return areaOrdine.getDocumento().getSedeEntita().getSede().getDatiGeografici().getCap() == null ? ""
                : areaOrdine.getDocumento().getSedeEntita().getSede().getDatiGeografici().getCap().getDescrizione();
    }

    public String getCodiceDestinazione() {
        return areaOrdine.getDocumento().getSedeEntita().getCodice();
    }

    /**
     *
     * @return codice ent.
     */
    public Integer getCodiceEntita() {
        return areaOrdine.getDocumento().getEntita().getCodice();
    }

    public String getCodiceFiscale() {
        return areaOrdine.getDocumento().getEntita().getAnagrafica().getCodiceFiscale();
    }

    /**
     *
     * @return codice sede==codice gito
     */
    public Integer getCodiceGiro() {
        if (NumberUtils.isNumber(areaOrdine.getDocumento().getSedeEntita().getOrdinamento())) {
            return Integer.parseInt(areaOrdine.getDocumento().getSedeEntita().getOrdinamento());
        }
        return -1;
    }

    /**
     *
     * @return cod td
     */
    public String getCodiceTipodocumento() {
        return areaOrdine.getDocumento().getTipoDocumento().getCodice();
    }

    /**
     *
     * @return codice zona
     */
    public Integer getCodiceZona() {
        if (areaOrdine.getDocumento().getSedeEntita().getZonaGeografica() != null
                && NumberUtils.isNumber(areaOrdine.getDocumento().getSedeEntita().getZonaGeografica().getCodice())) {
            return Integer.parseInt(areaOrdine.getDocumento().getSedeEntita().getZonaGeografica().getCodice());
        }
        return -1;
    }

    /**
     *
     * @return data reg
     */
    public Date getDataRegistrazione() {
        return areaOrdine.getDataRegistrazione();
    }

    public String getDenominazione() {
        return areaOrdine.getDocumento().getEntita().getAnagrafica().getDenominazione();
    }

    public String getIndirizzo() {
        return areaOrdine.getDocumento().getSedeEntita().getSede().getIndirizzo();
    }

    public String getLocalita() {
        return areaOrdine.getDocumento().getSedeEntita().getSede().getDatiGeografici().getLocalita() == null ? ""
                : areaOrdine.getDocumento().getSedeEntita().getSede().getDatiGeografici().getLocalita()
                        .getDescrizione();
    }

    public String getNazione() {
        return areaOrdine.getDocumento().getSedeEntita().getSede().getDatiGeografici().getNazione() == null ? ""
                : areaOrdine.getDocumento().getSedeEntita().getSede().getDatiGeografici().getNazione().getCodice();
    }

    /**
     *
     * @return num documento. -1 se numero documento non è numerico
     */
    public Integer getNumeroDocumento() {
        if (NumberUtils.isNumber(areaOrdine.getDocumento().getCodice().getCodice())) {
            return Integer.parseInt(areaOrdine.getDocumento().getCodice().getCodice());
        }
        return -1;
    }

    public String getPiva() {
        return areaOrdine.getDocumento().getEntita().getAnagrafica().getPartiteIVA();
    }

    public String getSpCap() {
        return sedePrincipale.getSede().getDatiGeografici().getCap() == null ? ""
                : sedePrincipale.getSede().getDatiGeografici().getCap().getDescrizione();
    }

    public String getSpCodice() {
        return sedePrincipale.getCodice();
    }

    public String getSpIndirizzo() {
        return sedePrincipale.getSede().getIndirizzo();
    }

    public String getSpLocalita() {
        return sedePrincipale.getSede().getDatiGeografici().getLocalita() == null ? ""
                : sedePrincipale.getSede().getDatiGeografici().getLocalita().getDescrizione();
    }

    public String getSpNazione() {
        return sedePrincipale.getSede().getDatiGeografici().getNazione() == null ? ""
                : sedePrincipale.getSede().getDatiGeografici().getNazione().getCodice();
    }

    /**
     *
     * @return tipo Riga
     */
    public String getTipoRiga() {
        return "T";
    }
}
