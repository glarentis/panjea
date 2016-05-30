package it.eurotn.panjea.contabilita.util;

import java.util.Comparator;

/**
 * Comparator per ordinare la lista di totaliCodiceIvaDTO secondo il codice iva.
 *
 * @author Leonardo
 */
public class TotaliCodiceIvaDTOComparator implements Comparator<TotaliCodiceIvaDTO> {

    @Override
    public int compare(TotaliCodiceIvaDTO totaliCodiceIvaDTO1, TotaliCodiceIvaDTO totaliCodiceIvaDTO2) {
        return totaliCodiceIvaDTO1.getCodiceIva().compareTo(totaliCodiceIvaDTO2.getCodiceIva());
    }
}
