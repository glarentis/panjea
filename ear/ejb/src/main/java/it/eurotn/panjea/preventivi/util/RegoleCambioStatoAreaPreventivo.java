package it.eurotn.panjea.preventivi.util;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.util.PanjeaEJBUtil;

public class RegoleCambioStatoAreaPreventivo
        extends RegoleCambioStatoAreaDocumento<AreaPreventivo, StatoAreaPreventivo> {

    @Override
    public boolean isCambioStatoPossibile(StatoAreaPreventivo statoAttuale, StatoAreaPreventivo statoNuovo) {

        boolean cambioStato = false;

        switch (statoAttuale) {
        case ACCETTATO:
            cambioStato = (statoNuovo == StatoAreaPreventivo.RIFIUTATO || statoNuovo == StatoAreaPreventivo.IN_ATTESA);
            break;
        case RIFIUTATO:
            cambioStato = (statoNuovo == StatoAreaPreventivo.ACCETTATO || statoNuovo == StatoAreaPreventivo.IN_ATTESA);
            break;
        case IN_ATTESA:
            cambioStato = (statoNuovo == StatoAreaPreventivo.ACCETTATO || statoNuovo == StatoAreaPreventivo.RIFIUTATO);
            break;
        default:
            cambioStato = false;
        }

        return cambioStato;
    }

    @Override
    public boolean isCambioStatoPossibileSuDocumento(AreaPreventivo areaDocumento) {
        return super.isCambioStatoPossibileSuDocumento(areaDocumento) && !areaDocumento.isProcessato();
    }

    @Override
    public void postCambioStato(AreaPreventivo areaDocumento, StatoAreaPreventivo statoPrecedente) {
        switch (areaDocumento.getStatoAreaPreventivo()) {
        case IN_ATTESA:
            areaDocumento.setDataAccettazione(null);
            break;
        case RIFIUTATO:
            if (statoPrecedente == StatoAreaPreventivo.ACCETTATO || areaDocumento.getDataAccettazione() == null) {
                areaDocumento.setDataAccettazione(PanjeaEJBUtil.getToday());
            }
            break;
        case ACCETTATO:
            if (areaDocumento.getDataAccettazione() == null) {
                areaDocumento.setDataAccettazione(PanjeaEJBUtil.getToday());
            }
            break;
        default:

        }
    }

}
