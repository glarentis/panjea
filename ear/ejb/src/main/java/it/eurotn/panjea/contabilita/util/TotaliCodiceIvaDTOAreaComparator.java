package it.eurotn.panjea.contabilita.util;

import java.util.Comparator;

/**
 * Comparator per ordinare la lista di totaliCodiceIvaDTO secondo il codice areaContabile e ordinamento della riga.
 *
 * @author Leonardo
 */
public class TotaliCodiceIvaDTOAreaComparator implements Comparator<TotaliCodiceIvaDTO> {

    @Override
    public int compare(TotaliCodiceIvaDTO totaliCodiceIvaDTO1, TotaliCodiceIvaDTO totaliCodiceIvaDTO2) {

        String protocollo1 = totaliCodiceIvaDTO1.getAreaContabile() != null
                ? totaliCodiceIvaDTO1.getAreaContabile().getNumeroProtocollo().getCodiceOrder() : "";
        String protocollo2 = totaliCodiceIvaDTO2.getAreaContabile() != null
                ? totaliCodiceIvaDTO2.getAreaContabile().getNumeroProtocollo().getCodiceOrder() : "";

        int protocolloComparator = protocollo1.compareTo(protocollo2);
        int ordineComparator = totaliCodiceIvaDTO1.getOrdinamento().compareTo(totaliCodiceIvaDTO2.getOrdinamento());
        return protocolloComparator < ordineComparator ? -1 : (protocolloComparator == ordineComparator ? 0 : 1);
    }

}
