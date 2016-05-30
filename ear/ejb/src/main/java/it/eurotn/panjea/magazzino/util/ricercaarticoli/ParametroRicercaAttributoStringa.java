package it.eurotn.panjea.magazzino.util.ricercaarticoli;

import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;

/**
 * @author leonardo
 */
public class ParametroRicercaAttributoStringa extends ParametroRicercaAttributo {

    private static final long serialVersionUID = 4503094494275680810L;

    @Override
    public ParametroRicercaAttributo getNewInstance() {
        return new ParametroRicercaAttributoStringa();
    }

    @Override
    public String getOperatore() {
        if ("=".equals(super.getOperatore())) {
            return " LIKE ";
        }
        return super.getOperatore();
    }

    @Override
    public ETipoDatoTipoAttributo getTipoDato() {
        return ETipoDatoTipoAttributo.STRINGA;
    }

    @Override
    public String getTipoDatoString() {
        return "string";
    }

    @Override
    public Object getValore() {
        return ((String) super.getValore()).replaceAll("\\*", "%");
    }
}
