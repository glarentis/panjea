package it.eurotn.panjea.ordini.rich.editors.evasione;

import java.util.Comparator;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.util.CRC;

public class RigaComparator implements Comparator<RigaDistintaCarico> {
    @Override
    public int compare(RigaDistintaCarico o1, RigaDistintaCarico o2) {
        return getCheckSum(o1).compareTo(getCheckSum(o2));
    }

    /**
     *
     * @param riga
     *            riga per la quale creare il checksum
     * @return checksum della riga
     */
    private Integer getCheckSum(RigaDistintaCarico riga) {
        CRC checksum = new CRC();
        checksum.update(riga.getEntita().getCodice());
        checksum.update(riga.getNumeroDocumento().getCodiceOrder());
        checksum.update(riga.getDataRegistrazione().toString());
        checksum.update(riga.getSedeEntita());

        Integer idDocEvasione = null;
        if (riga.getDatiEvasioneDocumento().getTipoAreaEvasione() != null
                && !riga.getDatiEvasioneDocumento().getTipoAreaEvasione().isNew()) {
            idDocEvasione = riga.getDatiEvasioneDocumento().getTipoAreaEvasione().getId();
        }
        checksum.update(idDocEvasione);
        return checksum.getInt31();
    }
}