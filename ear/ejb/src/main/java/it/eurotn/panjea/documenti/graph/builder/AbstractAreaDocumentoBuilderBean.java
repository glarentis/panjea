package it.eurotn.panjea.documenti.graph.builder;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.builder.interfaces.AreaDocumentoBuilder;

public abstract class AbstractAreaDocumentoBuilderBean implements AreaDocumentoBuilder {

    public static final int MAX_DOCUMENT_SIZE_NODE = 20;

    /**
     * Crea un documento che rappresenta il raggruppamento di più documenti.
     *
     * @param numeroDocumenti
     *            numero di documenti raggruppati
     * @param descrizione
     *            descrizione dei documenti
     * @return documento
     */
    public static Documento createGruppoDocumento(int numeroDocumenti, String descrizione) {
        Documento documento = new Documento();
        documento.getCodice().setCodice(String.valueOf(numeroDocumenti));
        documento.setDescrizioneTipoDocumento(descrizione);

        return documento;
    }
}
