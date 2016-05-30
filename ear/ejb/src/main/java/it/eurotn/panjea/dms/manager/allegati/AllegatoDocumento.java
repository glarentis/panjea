package it.eurotn.panjea.dms.manager.allegati;

import java.io.Serializable;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegato;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoCodiceDocumento;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoEntita;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoTipoDocumento;

public class AllegatoDocumento extends AllegatoDMS implements Serializable {
    private static final long serialVersionUID = -4728660112639741487L;
    private static final String TEMPLATE = "DOCUMENTOPANJEA";

    /**
     * @param documento
     *            documento dell'allegato
     * @param codiceAzienda
     *            codice azienda
     */
    public AllegatoDocumento(final Documento documento, final String codiceAzienda) {
        super(TEMPLATE,
                new AttributoAllegato[] {
                        new AttributoAllegatoCodiceDocumento(String.valueOf(documento.getCodice().getCodice())),
                        new AttributoAllegatoEntita(documento.getEntitaDocumento(), codiceAzienda),
                        new AttributoAllegatoTipoDocumento(documento.getTipoDocumento().getCodice(),
                                documento.getTipoDocumento().getDescrizione(), documento.getTipoDocumento().getId(),
                                codiceAzienda) },
                documento.getDataDocumento());
    }

}
