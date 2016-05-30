package it.eurotn.panjea.magazzino.util.ricercaarticoli;

import java.text.NumberFormat;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;

/**
 * @author leonardo
 */
public class ParametroRicercaAttributoNumerico extends ParametroRicercaAttributo {

    private static final long serialVersionUID = 4503094494275680810L;

    @Override
    public ParametroRicercaAttributo getNewInstance() {
        return new ParametroRicercaAttributoNumerico();
    }

    @Override
    public ETipoDatoTipoAttributo getTipoDato() {
        return ETipoDatoTipoAttributo.NUMERICO;
    }

    @Override
    public String getTipoDatoString() {
        return "integer";
    }

    @Override
    public Object getValore() {
        Object valore = super.getValore();
        if (valore != null) {
            try {
                valore = StringUtils.replace((String) valore, ".", ",");
                Number dec = NumberFormat.getInstance().parse((String) valore);
                valore = dec.intValue();
            } catch (Exception e) {
                // se c'è un errore imposto null
                valore = null;
            }
        }
        return valore;
    }

}
