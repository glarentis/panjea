package it.eurotn.panjea.magazzino.util.ricercaarticoli;

import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;

/**
 * @author leonardo
 */
public class ParametroRicercaAttributoBooleano extends ParametroRicercaAttributo {

    private static final long serialVersionUID = 4503094494275680810L;

    @Override
    public ParametroRicercaAttributo getNewInstance() {
        return new ParametroRicercaAttributoBooleano();
    }

    @Override
    public String getOperatore() {
        return "=";
    }

    @Override
    public ETipoDatoTipoAttributo getTipoDato() {
        return ETipoDatoTipoAttributo.BOOLEANO;
    }

    @Override
    public String getTipoDatoString() {
        return "string";
    }

    @Override
    public Object getValore() {
        Object valore = super.getValore();
        if (valore != null && "S".equals(valore)) {
            valore = "true";
        } else {
            valore = "false";
        }
        return valore;
    }

}
