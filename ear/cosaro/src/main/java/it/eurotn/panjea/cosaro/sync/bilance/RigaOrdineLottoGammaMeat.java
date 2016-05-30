package it.eurotn.panjea.cosaro.sync.bilance;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;

public class RigaOrdineLottoGammaMeat {

    private RigaDistintaCarico rigaDistintaCarico;

    /**
     *
     * @param rigaDistintaCarico
     */
    public RigaOrdineLottoGammaMeat(RigaDistintaCarico rigaDistintaCarico) {
        this.rigaDistintaCarico = rigaDistintaCarico;
    }

    private String getAttributoValue(String codiceAttributo) {
        for (AttributoRiga attributo : rigaDistintaCarico.getRigaArticolo().getAttributi()) {
            if (attributo.getTipoAttributo().getCodice().equals(codiceAttributo)) {
                return attributo.getValore();
            }
        }
        return "att " + codiceAttributo + " non trovato";
    }

    public String getCodiceArticolo() {
        return rigaDistintaCarico.getArticolo().getCodice();
    }

    public String getCodiceLotto() {
        return null;
    }

    public String getColli() {
        return getAttributoValue("colli");
    }

    public String getConfezioni() {
        return getAttributoValue("pezzi");
    }

    public Date getDataScadenzaLotto() {
        return null;
    }

    public String getDescrizione() {
        return rigaDistintaCarico.getArticolo().getDescrizione();
    }

    public String getNumeroRiga() {
        return rigaDistintaCarico.getId() + StringUtils.leftPad(
                rigaDistintaCarico.getRigaArticolo().getAreaOrdine().getDocumento().getCodice().getCodice(), 10, '0');
    }

    public BigDecimal getPrezzoNettoUnitario() {
        return rigaDistintaCarico.getPrezzoNetto();
    }

    public Double getQuantita() {
        return rigaDistintaCarico.getQtaDaEvadere();
    }

    public String getTipoRiga() {
        return "C";
    }

    public String getUnitaMisura() {
        return rigaDistintaCarico.getArticolo().getUnitaMisura().getCodice();
    }

}
